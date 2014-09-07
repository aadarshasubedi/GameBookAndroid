package com.nex.gamebook.combat;

import java.util.ArrayList;
import java.util.List;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.game.ActiveOvertimeSkill;
import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.Enemy;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public class CombatProcess {

	private Enemy enemy;
	int turn = 0;

	public CombatProcess(Enemy enemy) {
		super();
		this.enemy = enemy;
	}

	// public ResultCombat doNormalAttack(Character attacker, Character
	// attacked, float modification, boolean allowLuck) {
	// doNormalAttack(attacker, attacked, modification, true);
	// }
	public ResultCombat doNormalAttack(Character attacker, Character attacked, float modification, boolean allowLuck) {
		ResultCombat resultCombat = new ResultCombat();
		resultCombat.setType(attacker.getType());
		if(!attacker.isCanAttack())  {
			resultCombat.setCannotAttack(true);
			attacker.setCanAttack(true);
			return resultCombat;
		}
		if (allowLuck)
			resultCombat.setLuck(attacked.hasLuck());
		if (!resultCombat.isLuck()) {
			resultCombat.setCritical(attacker.isCriticalChance());
			int attack = attacker.getCurrentStats().getTotalPureDamage();
			int defense = attacked.getCurrentStats().getDefensePercentage();
			int totalDamage = (attack - (int) (((double) attack / 100d) * defense));
			totalDamage *= modification;
			if (resultCombat.isCritical()) {
				double criticalMultiplier = attacker.hasLuck() ? 1 : 0.5;
				resultCombat.setMultiply(criticalMultiplier);
				totalDamage += totalDamage * criticalMultiplier;
			}
			resultCombat.setDamage(totalDamage);
			int attackedHealth = attacked.getCurrentStats().getRealHealth();
			attacked.getCurrentStats().setHealth(attackedHealth - resultCombat.getDamage());
		}
		resultCombat.setEnemyName(enemy.getName());
		return resultCombat;
	}

	public ResultCombat doNormalAttack(Character attacker, Character attacked, boolean allowLuck) {
		return doNormalAttack(attacker, attacked, 1, allowLuck);
	}

	public void fight(BattleLogCallback callback) {
		Player player = (Player) callback.getCharacter();

		callback.divide(++turn);
		boolean enemyBegin = !player.hasLuck();
		if (enemyBegin) {
			if (!doSpecialAttack(enemy, player, callback, true)) {
				doSpecialAttack(player, enemy, callback, false);
			}
			SpecialSkill playerSkill = player.getSelectedSkill();
			SpecialSkill enemySkill = enemy.getSelectedSkill();
			if(enemySkill!=null && enemySkill.isTriggerOnEndOfRound()) {
				doSkill(enemy, player, enemy, enemySkill, callback, null);
			}
			if(playerSkill!=null && playerSkill.isTriggerOnEndOfRound()) {
				doSkill(player, enemy, player, playerSkill, callback, null);
			}
		} else {
			if (!doSpecialAttack(player, enemy, callback, true)) {
				doSpecialAttack(enemy, player, callback, false);
			}
			SpecialSkill playerSkill = player.getSelectedSkill();
			SpecialSkill enemySkill = enemy.getSelectedSkill();
			if(playerSkill!=null && playerSkill.isTriggerOnEndOfRound()) {
				doSkill(player, enemy, player, playerSkill, callback, null);
			}
			if(enemySkill!=null && enemySkill.isTriggerOnEndOfRound()) {
				doSkill(enemy, player, enemy, enemySkill, callback, null);
			}
		}
		
		
		doOvertimeSkills(player, enemy, callback);
		doConditionEffects(player, enemy);
		
		if (enemy.isDefeated() || player.isDefeated()) {
			player.cleanActiveSkillsAfterFightEnd();
			long exp = enemy.getXp(player.getLevel());
			if (player.isDefeated()) {
				exp = 0;
			}
			callback.fightEnd(exp);
		}
	}

	private void doConditionEffects(Character c1, Character c2) {
		doConditionEffects(c1);
		doConditionEffects(c2);
	}
	
	private void doConditionEffects(Character c) {
		List<Bonus> releaseThese = new ArrayList<Bonus>();
		for(Bonus b: c.getConditions()) {
			if(b.isExhausted()) {
				releaseThese.add(b);
			}
			b.setCurrentTurn(b.getCurrentTurn() + 1);
		}
		c.getConditions().removeAll(releaseThese);
	}
	
	private void doOvertimeSkills(Character attacker, Character attacked, BattleLogCallback callback) {
		doOvertimeSkill(attacker, attacked, callback);
		doOvertimeSkill(attacked, attacker, callback);
	}

	private void doOvertimeSkill(Character attacker, Character attacked, BattleLogCallback callback) {
		List<ActiveOvertimeSkill> releaseThese = new ArrayList<>();
		for (ActiveOvertimeSkill skill : attacker.getOvertimeSkills()) {
			boolean available = skill.execute(attacker, attacked, callback, null);
			if (!available)
				releaseThese.add(skill);
		}
		attacker.getOvertimeSkills().removeAll(releaseThese);
	}
	
	private void choseSkillForAI(Character attacker, Character attacked) {
		if (attacker instanceof Enemy) {
			attacker.chooseBestSkill(attacked, true);
		} else {
			attacked.chooseBestSkill(attacker, false);
		}
	}

	private boolean doSpecialAttack(Character attacker, Character attacked, BattleLogCallback callback, boolean canChooseAISkill) {
		if (canChooseAISkill)
			choseSkillForAI(attacker, attacked);
		SpecialSkill attackerSkill = attacker.getSelectedSkill();
		SpecialSkill skill = attacked.getSelectedSkill();
		if(attackerSkill!=null && attackerSkill.isTriggerOnEndOfRound()) {
			attackerSkill = null;
		}
		if(skill!=null && skill.isTriggerOnEndOfRound()) {
			skill = null;
		}
		boolean usedBeforeSkill = false;
		boolean doAttack = true;
		if (skill != null && (skill.isTriggerBeforeEnemyAttack() || skill.isTriggerBeforeEnemySpecialAttack())) {
			if (attackerSkill != null && attackerSkill.isTriggerBeforeEnemySpecialAttack() && !skill.isTriggerBeforeEnemySpecialAttack()) {
				doSkill(attacker, attacked, attacker, attackerSkill, callback, null);
				usedBeforeSkill = true;
			}
			doSkill(attacked, attacker, attacked, skill, callback, null);
			if (attacker.isDefeated()) {
				return true;
			}
		}

		if (!usedBeforeSkill && attackerSkill != null && attackerSkill.afterNormalAttack()) {
			ResultCombat result = doNormalAttack(attacker, attacked, true);
			callback.logAttack(result);
			if (skill != null && skill.isTriggerAfterEnemyAttack()) {
				doSkill(attacked, attacker, attacked, skill, callback, result);
				if (attacker.isDefeated()) {
					return true;
				}
			}
			doSkill(attacker, attacked, attacker, attackerSkill, callback, result);
		} else {

			if (!usedBeforeSkill && attackerSkill != null && !attackerSkill.isTriggerAfterEnemyAttack() && !attackerSkill.isTriggerBeforeEnemyAttack()) {
				doSkill(attacker, attacked, attacker, attackerSkill, callback, null);
			}
			if (doAttack) {
				ResultCombat result = doNormalAttack(attacker, attacked, true);
				callback.logAttack(result);
				if (skill != null && skill.isTriggerAfterEnemyAttack()) {
					doSkill(attacked, attacker, attacked, skill, callback, result);
					if (attacker.isDefeated()) {
						return true;
					}
				}
			}
		}

		return attacked.isDefeated();
	}
	
	private void doSkill(Character attacker, Character attacked, Character skillOwner, SpecialSkill skill, BattleLogCallback callback, ResultCombat resultCombat) {
		if(skillOwner.isCanCastSkill()) {
			skill.doAttack(attacker, attacked, callback, resultCombat);
		} else {
			logIfCannotCast(callback, skillOwner);
			skillOwner.setCanCastSkill(true);
		}
	}
	
	private void logIfCannotCast(BattleLogCallback callback, Character c) {
		ResultCombat interruptRC = new ResultCombat();
		SpecialSkill skill = c.getSelectedSkill();
		
		if(!c.isCanCastSkill() && skill!=null && skill.canUse()) {
			interruptRC.setType(c.getType());
			interruptRC.setCannotCast(true);
			callback.logAttack(interruptRC);
		}
	}
	
}
