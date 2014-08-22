package com.nex.gamebook.entity;

import java.io.Serializable;
import java.lang.reflect.Method;

import android.util.Log;

import com.nex.gamebook.entity.Bonus.BonusType;
import com.nex.gamebook.entity.io.GameBookUtils;

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

	private transient Stats holder;
	
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

	public int setHealth(int health) {
		return this.health = health;
	}

	public int getDefense() {
		return defense;
	}

	public int setDefense(int defense) {
		if (defense > MAX_DEFENSE_OF_CHARACTER)
			defense = MAX_DEFENSE_OF_CHARACTER;
		return this.defense = defense;
	}

	
	
	public int getSkill() {
		return skill;
	}

	public int setSkill(int skill) {
		if (skill > MAX_SKILL_OF_CHARACTER)
			skill = MAX_SKILL_OF_CHARACTER;
		return this.skill = skill;
	}

	public int getLuck() {
		return luck;
	}

	public int setLuck(int luck) {
		if (luck > MAX_LUCK_OF_CHARACTER)
			luck = MAX_LUCK_OF_CHARACTER;
		return this.luck = luck;
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

	public int setAttack(int attack) {
		return this.attack = attack;
	}

	public int getDamage() {
		return damage;
	}

	public int setDamage(int damage) {
		return this.damage = damage;
	}

	public int getValueByBonusType(BonusType type) {
		Method currentAttr;
		try {
			currentAttr = Stats.class.getDeclaredMethod(GameBookUtils.createMethodName("get", type.name().toLowerCase()), new Class[0]);
			int currentValue = (int) currentAttr.invoke(this, new Object[0]);
			return currentValue;
		} catch (Exception e) {
			Log.e("Stats", "", e);
		}
		return 0;
	}
	
	public void releaseTemporalStats(Stats releaseStats) {
		int damage = releaseStats.holder.health - this.health;
		int resultHealth = releaseStats.health - damage;
		if(resultHealth < 0) {
			resultHealth = 0;
		}
		releaseStats.setHealth(resultHealth);
		this.health -= releaseStats.health;
		this.attack -= releaseStats.attack;
		this.defense -= releaseStats.defense;
		this.skill -= releaseStats.skill;
		this.luck -= releaseStats.luck;
		this.damage -= releaseStats.damage;
	}
	public void setHolder(Stats holder) {
		this.holder = holder;
	}
}
