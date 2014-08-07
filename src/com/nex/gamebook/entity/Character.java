package com.nex.gamebook.entity;

import java.io.Serializable;
import java.util.Random;

public abstract class Character  implements Serializable {
	private static final long serialVersionUID = 214922718575334896L;
	public static int MAX_LUCK_OF_CHARACTER = 14;
	public static int MAX_SKILL_OF_CHARACTER = 15;
	public static int MAX_DEFENSE_OF_CHARACTER = 20;
	
	private Stats stats = new Stats();
	private Stats currentStats = new Stats(stats);
	private boolean fighting;

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public Stats getCurrentStats() {
		return currentStats;
	}

	public void setCurrentStats(Stats currentStats) {
		this.currentStats = currentStats;
	}

	public boolean hasLuck() {
		Random random = new Random();
		int res = random.nextInt(Stats.TOTAL_LUCK_FOR_CALC);
		return getCurrentStats().getLuck() >= res;
	}

	public boolean isCriticalChance() {
		Random random = new Random();
		int res = random.nextInt(Stats.TOTAL_SKILL_FOR_CALC);
		return getCurrentStats().getSkill() >= res;
	}

	public boolean isDefeated() {
		return getCurrentStats().getHealth() <= 0;
	}

	public abstract CharacterType getType();

	public boolean isFighting() {
		return fighting;
	}

	public void setFighting(boolean fighting) {
		this.fighting = fighting;
	}

}
