package com.nex.gamebook.entity;

import com.nex.gamebook.R;
import com.nex.gamebook.entity.io.GameBookUtils;

public class Enemy extends com.nex.gamebook.entity.Character {

	public enum EnemyLevel {
		CREATURE(R.string.enemy_creature, 10), 
		MINION(R.string.enemy_minion, 15), 
		BOSS(R.string.enemy_boss, 20);
		private int code;
		private int baseXP;
		private EnemyLevel(int code, int baseXP) {
			this.code = code;
			this.baseXP = baseXP;
		}

		public int getCode() {
			return code;
		}

		public int getBaseXP() {
			return baseXP;
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
	private EnemyLevel enemyLevel;

	public Enemy() {
	}

	public Enemy(Enemy enemy) {
		super(enemy);
		this.name = enemy.name;
		this.enemyLevel = enemy.enemyLevel;
	}

	public boolean hasLuck() {
		return hasLuck(Stats.TOTAL_LUCK_FOR_CALC);
	}
	
	public boolean isCriticalChance() {
		return isCriticalChance(Stats.TOTAL_SKILL_FOR_CALC);
	}
	
	public String getName() {
		return GameBookUtils.getInstance().getText(name, getStory());
	}

	public void setName(String name) {
		this.name = name;
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

	public EnemyLevel getEnemyLevel() {
		return enemyLevel;
	}

	public void setEnemyLevel(EnemyLevel level) {
		this.enemyLevel = level;
	}
	@Override
	public long getExperience() {
		return enemyLevel.baseXP * getLevel();
	}
}
