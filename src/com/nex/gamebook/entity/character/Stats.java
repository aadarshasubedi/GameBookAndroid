package com.nex.gamebook.entity.character;

import java.io.Serializable;

public class Stats implements Serializable {
	private int health;
	private int defense;
	private int skill;
	private int luck;
	
	public Stats() {
	}

	public Stats(Stats stats) {
		this.health = stats.health;
		this.defense = stats.defense;
		this.skill = stats.skill;
		this.luck = stats.luck;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		if(luck > Character.MAX_LUCK_OF_CHARACTER) luck = Character.MAX_LUCK_OF_CHARACTER;
		this.luck = luck;
	}
	public int getLuckPercentage() {
		return Stats.getLuckPercentage(luck);
	}
	public static int getLuckPercentage(int luck) {
		float res = ((float)luck/(float)Character.TOTAL_LUCK_FOR_CALC);
		return (int) (res * 100);
	}
}
