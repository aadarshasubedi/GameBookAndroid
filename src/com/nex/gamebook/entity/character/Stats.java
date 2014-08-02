package com.nex.gamebook.entity.character;

public class Stats {
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
		this.luck = luck;
	}

}
