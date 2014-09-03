package com.nex.gamebook.combat;

import com.nex.gamebook.attack.special.SpecialSkill;
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

//	public ResultCombat doNormalAttack(Character attacker, Character attacked, float modification, boolean allowLuck) {
//		doNormalAttack(attacker, attacked, modification, true);
//	}
	public ResultCombat doNormalAttack(Character attacker, Character attacked, float modification, boolean allowLuck) {
		ResultCombat resultCombat = new ResultCombat();
		resultCombat.setType(attacker.getType());
		if(allowLuck)
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
			int attackedHealth = attacked.getCurrentStats().getHealth();
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
		
		long experience = enemy.getXp(player.getLevel());
		callback.divide(++turn);
		boolean enemyBegin = !player.hasLuck();
		if (enemyBegin) {
			if (!doSpecialAttack(enemy, player, callback, true)) {
				doSpecialAttack(player, enemy, callback, false);
			}
		} else {
			if (!doSpecialAttack(player, enemy, callback, true)) {
				doSpecialAttack(enemy, player, callback, false);
			}
		}
		if (player.isDefeated()) {
			experience = 0;
		}
		if(enemy.isDefeated() || player.isDefeated()) {
			player.cleanActiveSkillsAfterFightEnd();
			callback.fightEnd(experience);
		}
	}
	
	private void choseSkillForAI(Character attacker, Character attacked) {
		if(attacker instanceof Enemy) {
			attacker.chooseBestSkill(attacked, true);
		} else {
			attacked.chooseBestSkill(attacker, false);
		}
	}
	
	private boolean doSpecialAttack(Character attacker, Character attacked, BattleLogCallback callback, boolean canChooseAISkill) {
		if(canChooseAISkill)
		choseSkillForAI(attacker, attacked);
		SpecialSkill attackerSkill = attacker.getSpecialSkill();
		SpecialSkill skill = attacked.getSpecialSkill();
		boolean usedBeforeSkill = false;
		boolean doAttack = true;
		if (skill != null && (skill.isTriggerBeforeEnemyAttack() || skill.isTriggerBeforeEnemySpecialAttack())) {
			if(attackerSkill!=null && attackerSkill.isTriggerBeforeEnemySpecialAttack() && !skill.isTriggerBeforeEnemySpecialAttack()) {
				doAttack = attackerSkill.doAttack(attacker, attacked, callback, null);
				usedBeforeSkill = true;
			}
			skill.doAttack(attacked, attacker, callback, null);
			if (attacker.isDefeated()) {
				return true;
			}
		}
		
		if (!usedBeforeSkill && attackerSkill != null && attackerSkill.afterNormalAttack()) {
			ResultCombat result = doNormalAttack(attacker, attacked, true);
			callback.logAttack(result);
			if (skill != null && skill.isTriggerAfterEnemyAttack()) {
				skill.doAttack(attacked, attacker, callback, result);
				if (attacker.isDefeated()) {
					return true;
				}
			}
			attackerSkill.doAttack(attacker, attacked, callback, result);
		} else {
			
			if (!usedBeforeSkill && attackerSkill != null && !attackerSkill.isTriggerAfterEnemyAttack() && !attackerSkill.isTriggerBeforeEnemyAttack()) {
				doAttack = attackerSkill.doAttack(attacker, attacked, callback, null);
			}
			if (doAttack) {
				ResultCombat result = doNormalAttack(attacker, attacked, true);
				callback.logAttack(result);
				if (skill != null && skill.isTriggerAfterEnemyAttack()) {
					skill.doAttack(attacked, attacker, callback, result);
					if (attacker.isDefeated()) {
						return true;
					}
				}
			}
		}

		return attacked.isDefeated();
	}
}
