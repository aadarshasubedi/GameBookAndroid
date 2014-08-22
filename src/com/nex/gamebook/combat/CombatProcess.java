package com.nex.gamebook.combat;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.playground.AttackCallback;

public class CombatProcess {

	private Enemy enemy;

	public CombatProcess(Enemy enemy) {
		super();
		this.enemy = enemy;
	}

	public ResultCombat attack(Character attackChar, Character attackedCharacter) {
		ResultCombat resultCombat = new ResultCombat();
		resultCombat.setType(attackChar.getType());
		resultCombat.setLuck(attackedCharacter.hasLuck());
		if (!resultCombat.isLuck()) {
			resultCombat.setCritical(attackChar.isCriticalChance());
			int attack = attackChar.getCurrentStats().getAttack()
					* attackChar.getCurrentStats().getDamage();
			int defense = attackedCharacter.getCurrentStats()
					.getDefensePercentage();
			int totalDamage = (attack - (int) (((double) attack / 100d) * defense));
			if (resultCombat.isCritical()) {
				double criticalMultiplier = attackChar.hasLuck() ? 1 : 0.5;
				resultCombat.setMultiply(criticalMultiplier);
				totalDamage += totalDamage * criticalMultiplier;
			}
			resultCombat.setDamage(totalDamage);
			int attackedHealth = attackedCharacter.getCurrentStats()
					.getHealth();
			attackedCharacter.getCurrentStats().setHealth(
					attackedHealth - resultCombat.getDamage());
		}
		resultCombat.setEnemyName(enemy.getName());
		return resultCombat;
	}

	public void fight(AttackCallback callback) {
		Player character = (Player) callback.getCharacter();
		boolean enemyBegin = !character.hasLuck();
		while (enemy.getCurrentStats().getHealth() > 0) {
			if (enemyBegin) {
				SpecialSkill plskill = character.getSpecialSkill();
				if(plskill.isTriggerEnemy()) {
					plskill.doAttack(enemy, character, callback);
					if(enemy.isDefeated()) continue;
				}
				SpecialSkill specialAttack = enemy.getSpecialSkill();
				if (specialAttack != null) {
					specialAttack.doAttack(enemy, character, callback);
				}
				callback.attackCallBack(attack(enemy, character));
			} else {
				SpecialSkill eskill = character.getSpecialSkill();
				if(eskill.isTriggerEnemy()) {
					eskill.doAttack(enemy, character, callback);
					if(character.isDefeated()) {
						break;
					}
				}
				character.getSpecialSkill().doAttack(enemy, character, callback);
				callback.attackCallBack(attack(character, enemy));
			}
			enemyBegin = !enemyBegin;
			if (character.isDefeated()) {
				break;
			}
		}
		character.getSpecialSkill().clean();
		callback.fightEnd();
	}
}
