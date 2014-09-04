package com.nex.gamebook.game;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.util.GameBookUtils;

public class Enemy extends com.nex.gamebook.game.Character {
	public static final float DEFAULT_COEFF = 0.1f;

	public enum EnemyLevel {
		CREATURE(R.string.enemy_creature, 10, new StatType[0]), MINION(R.string.enemy_minion, 30, new StatType[] { StatType.HEALTH }), BOSS(R.string.enemy_boss, 60, new StatType[] { StatType.HEALTH,
				StatType.ATTACK });
		private int code;
		private int baseXP;
		private StatType[] primaryAttributes;

		private EnemyLevel(int code, int baseXP, StatType[] attr) {
			this.code = code;
			this.baseXP = baseXP;
			this.primaryAttributes = attr;
		}

		public StatType[] getPrimaryAttributes() {
			return primaryAttributes;
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
	private double xpcoeff = DEFAULT_COEFF;
	private EnemyLevel enemyLevel;

	public Enemy() {
	}

	public Enemy(Enemy enemy) {
		super(enemy);
		this.name = enemy.name;
		this.enemyLevel = enemy.enemyLevel;
		this.xpcoeff = enemy.getXpcoeff();
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

	public long getXp(int playerLevel) {
		return ExperienceMap.getInstance().getGainedExperienceFromEnemy(this, playerLevel);
	}

	// @Override
	// public SpecialSkill getSpecialSkill(String skillName) {
	// if(specialSkill == null) {
	// specialSkill = SpecialSkillsMap.get(skillName);
	// }
	// return specialSkill;
	// }

	public double getXpcoeff() {
		return xpcoeff;
	}

	public void setXpcoeff(double xpcoeff) {
		this.xpcoeff = xpcoeff;
	}

	@Override
	public void chooseBestSkill(Character c, boolean enemyBegin) {
		setSelectedSkill(AISpecialSkillsUsage.getBestSpecialSkill((Player) c, this, enemyBegin));
	}

}
