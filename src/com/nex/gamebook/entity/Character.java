package com.nex.gamebook.entity;

import java.util.Random;

public abstract class Character {
	public static int MAX_LUCK_OF_CHARACTER = 14;
	public static int MAX_SKILL_OF_CHARACTER = 15;

	public static int TOTAL_LUCK_FOR_CALC = 20;
	public static int TOTAL_SKILL_FOR_CALC = 25;
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
		int res = random.nextInt(Character.TOTAL_LUCK_FOR_CALC);
		return getCurrentStats().getLuck() >= res;
	}

	public boolean isCriticalChance() {
		Random random = new Random();
		int res = random.nextInt(Character.TOTAL_SKILL_FOR_CALC);
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
