package com.nex.gamebook.entity.character;

public class Stats {
	private int health;
	private int attack;
	private int defense;
	private int skill;

	public Stats() {
		// TODO Auto-generated constructor stub
	}
	
	public Stats(Stats stats) {
		this.health = stats.health;
		this.attack = stats.attack;
		this.defense = stats.defense;
		this.skill = stats.skill;
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
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

}
