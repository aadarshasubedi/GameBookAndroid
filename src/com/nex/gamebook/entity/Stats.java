package com.nex.gamebook.entity;

import java.io.Serializable;

public class Stats implements Serializable {
	private static final long serialVersionUID = 5967013219649795912L;
	
	public static int MAX_LUCK_OF_CHARACTER = 23;//66%
	public static int MAX_SKILL_OF_CHARACTER = 23;//65%
	public static int MAX_DEFENSE_OF_CHARACTER = 24;//68%
	
	public static int TOTAL_LUCK_FOR_CALC = 35;
	public static int TOTAL_SKILL_FOR_CALC = 35;
	public static int TOTAL_ARMOR_FOR_CALC = 35;
	private int health;
	private int defense;
	private int skill;
	private int luck;
	private int attack;
	private int damage = 1;

	public Stats() {
	}

	public Stats(Stats stats) {
		this.health = stats.health;
		this.defense = stats.defense;
		this.skill = stats.skill;
		this.luck = stats.luck;
		this.attack = stats.attack;
		this.damage = stats.damage;
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
		if (defense > MAX_DEFENSE_OF_CHARACTER)
			defense = MAX_DEFENSE_OF_CHARACTER;
		this.defense = defense;
	}

	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		if (skill > MAX_SKILL_OF_CHARACTER)
			skill = MAX_SKILL_OF_CHARACTER;
		this.skill = skill;
	}

	public int getLuck() {
		return luck;
	}

	public void setLuck(int luck) {
		if (luck > MAX_LUCK_OF_CHARACTER)
			luck = MAX_LUCK_OF_CHARACTER;
		this.luck = luck;
	}

	public int getLuckPercentage() {
		return Stats.getPercentage(luck, TOTAL_LUCK_FOR_CALC);
	}

	public int getSkillPercentage() {
		return Stats.getPercentage(skill, TOTAL_SKILL_FOR_CALC);
	}

	public int getDefensePercentage() {
		return Stats.getPercentage(defense, TOTAL_ARMOR_FOR_CALC);
	}

	public static int getPercentage(int value, int what) {
		float res = ((float) value / (float) what);
		return (int) (res * 100);
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

}
