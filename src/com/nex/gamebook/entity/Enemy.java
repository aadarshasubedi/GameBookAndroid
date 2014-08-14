package com.nex.gamebook.entity;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.entity.io.GameBookUtils;
import com.nex.gamebook.playground.AttackCallback;

public class Enemy extends com.nex.gamebook.entity.Character {

	public enum EnemyLevel {
		CREATURE(R.string.enemy_creature), MINION(R.string.enemy_minion), BOSS(
				R.string.enemy_boss);
		private int code;

		private EnemyLevel(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
		
		public static EnemyLevel getLevelByString(String s) {
			if (s == null || "".equals(s)) {
				return EnemyLevel.CREATURE;
			}
			return EnemyLevel.valueOf(s.toUpperCase());
		}
	}

	private static final long serialVersionUID = -1969817378614053107L;
	private String name;
	private int index;
	private boolean affectPlayer;
	private EnemyLevel level;

	public Enemy() {
		// TODO Auto-generated constructor stub
	}

	public Enemy(Enemy enemy) {
		super(enemy);
		this.name = enemy.name;
		this.level = enemy.level;
	}

	public String getName() {
		return GameBookUtils.getInstance().getText(name, getStory());
	}

	public void setName(String name) {
		this.name = name;
	}

	private ResultCombat attack(Character attackChar,
			Character attackedCharacter) {
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
		resultCombat.setEnemyName(getName());
		return resultCombat;
	}

	public void fight(AttackCallback callback) {
		Character character = callback.getCharacter();
		boolean enemyBegin = !character.hasLuck();

		while (getCurrentStats().getHealth() > 0) {

			if (enemyBegin) {
				callback.attackCallBack(attack(this, character));
			} else {
				callback.attackCallBack(attack(character, this));
			}
			enemyBegin = !enemyBegin;
			if (character.isDefeated()) {
				break;
			}
			// try {
			// Thread.sleep(700);
			// } catch (InterruptedException e) {
			// Log.e("GameBookFighting", "", e);
			// }
		}
		callback.fightEnd();
	}

	public boolean isAffectPlayer() {
		return affectPlayer;
	}

	public void setAffectPlayer(boolean affectPlayer) {
		this.affectPlayer = affectPlayer;
	}

	@Override
	public CharacterType getType() {
		return CharacterType.ENEMY;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public EnemyLevel getLevel() {
		return level;
	}

	public void setLevel(EnemyLevel level) {
		this.level = level;
	}

}
