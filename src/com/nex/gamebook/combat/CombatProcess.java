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

	public ResultCombat doNormalAttack(Character attacker, Character attacked, float modification) {
		ResultCombat resultCombat = new ResultCombat();
		resultCombat.setType(attacker.getType());
		resultCombat.setLuck(attacked.hasLuck());
		if (!resultCombat.isLuck()) {
			resultCombat.setCritical(attacker.isCriticalChance());
			int attack = attacker.getCurrentStats().getAttack() * attacker.getCurrentStats().getDamage();
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

	public ResultCombat doNormalAttack(Character attacker, Character attacked) {
		return doNormalAttack(attacker, attacked, 1);
	}

	public void fight(BattleLogCallback callback) {
		Player player = (Player) callback.getCharacter();
		
		long experience = enemy.getXp(player.getLevel());
		callback.divide(++turn);
		boolean enemyBegin = !player.hasLuck();
		if (enemyBegin) {
			if (!doSpecialAttack(enemy, player, callback)) {
				doSpecialAttack(player, enemy, callback);
			}
		} else {
			if (!doSpecialAttack(player, enemy, callback)) {
				doSpecialAttack(enemy, player, callback);
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
			attacker.chooseBestSkill(attacked);
		} else {
			attacked.chooseBestSkill(attacker);
		}
	}
	
	private boolean doSpecialAttack(Character attacker, Character attacked, BattleLogCallback callback) {
		choseSkillForAI(attacker, attacked);
		SpecialSkill attackerSkill = attacker.getSpecialSkill();
		SpecialSkill skill = attacked.getSpecialSkill();
		if (skill != null && skill.isTriggerBeforeEnemyAttack()) {
			skill.doAttack(attacked, attacker, callback, null);
			if (attacker.isDefeated()) {
				return true;
			}
		}
		
		if (attackerSkill != null && attackerSkill.afterNormalAttack()) {
			ResultCombat result = doNormalAttack(attacker, attacked);
			callback.logAttack(result);
			if (skill != null && skill.isTriggerAfterEnemyAttack()) {
				skill.doAttack(attacked, attacker, callback, result);
				if (attacker.isDefeated()) {
					return true;
				}
			}
			attackerSkill.doAttack(attacker, attacked, callback, result);
		} else {
			boolean doAttack = true;
			if (attackerSkill != null && !attackerSkill.isTriggerAfterEnemyAttack() && !attackerSkill.isTriggerBeforeEnemyAttack()) {
				doAttack = attackerSkill.doAttack(attacker, attacked, callback, null);
			}
			if (doAttack) {
				ResultCombat result = doNormalAttack(attacker, attacked);
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
