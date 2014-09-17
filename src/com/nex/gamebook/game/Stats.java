package com.nex.gamebook.game;

import java.io.Serializable;
import java.lang.reflect.Method;

import android.util.Log;

import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.skills.passive.HealthIncrease;
import com.nex.gamebook.util.GameBookUtils;

public class Stats implements Serializable {
	private static final long serialVersionUID = 5967013219649795912L;
	public static int MAX_SKILL_PERCENTAGE = 70;
	public static int MAX_LUCK_PERCENTAGE = 55;
	public static int MAX_DEFENSE_PERCENTAGE = 75;

	public static int TOTAL_LUCK_FOR_CALC = 35;
	public static int TOTAL_SKILL_FOR_CALC = 35;
	public static int TOTAL_ARMOR_FOR_CALC = 35;
	private int health;
	private int defense;
	private int skill;
	private int luck;
	private int attack;
	private int damage = 1;
	private int skillpower = 1;
	private transient Character character;
	private transient Stats holder;
	private boolean isBase;

	public Stats(boolean isBase) {
		this.isBase = isBase;
	}

	public Stats(Stats stats, int positive, boolean isBase) {
		this(isBase);
		this.health = positive * stats.health;
		this.defense = positive * stats.defense;
		this.skill = positive * stats.skill;
		this.luck = positive * stats.luck;
		this.attack = positive * stats.attack;
		this.damage = positive * stats.damage;
		this.skillpower = positive * stats.skillpower;
	}

	public Stats(Stats stats, boolean isBase) {
		this(stats, 1, isBase);
	}

	public int getHealth() {
		int value = getRealHealth() + getConditionsByType(StatType.HEALTH);
		return value;
	}

	public int getRealHealth() {
		int value = this.health;
		HealthIncrease skill = (HealthIncrease) character.findPassiveSkill(HealthIncrease.class);
		if(skill!=null) {
			value += (value/100) * skill.power(character);
		}
		return value;
	}
	
	public int getRealDefense() {
		return this.defense;
	}
	
	public int getRealAttack() {
		return this.attack;
	}
	public int getRealSkill() {
		return this.skill;
	}
	public int getRealLuck() {
		return this.luck;
	}
	public int getRealDamage() {
		return this.damage;
	}
	public int getRealSkillpower() {
		return this.skillpower;
	}
	public int setHealth(int health) {
		return this.health = health;
	}

	public int getDefense() {
		int total = defense + getConditionsByType(StatType.DEFENSE);
		int max = getMaxValuebyPercentage(TOTAL_ARMOR_FOR_CALC, MAX_DEFENSE_PERCENTAGE, total);
		if (total > max)
			total = max;
		return total;
	}

	public int getSkillpower() {
		int total = skillpower + getConditionsByType(StatType.SKILLPOWER);
		if (total < 1) {
			total = 1;
		}
		return total;
	}

	public int setSkillpower(int skillpower) {
		return this.skillpower = skillpower;
	}

	public int setDefense(int defense) {
		return this.defense = getMaxValuebyPercentage(TOTAL_ARMOR_FOR_CALC, MAX_DEFENSE_PERCENTAGE, defense);
	}

	public int getMaxValuebyPercentage(int totalForCalc, int maxPerc, int value) {
		int total = getTotalStat(totalForCalc);
		int perc = Stats.getPercentage(defense, total);
		if (perc > maxPerc) {
			value = getValuePerc(total, maxPerc);
		}
		return value;
	}

	public int getSkill() {
		int total = skill + getConditionsByType(StatType.SKILL);
		int max = getMaxValuebyPercentage(TOTAL_SKILL_FOR_CALC, MAX_SKILL_PERCENTAGE, total);
		if (total > max)
			total = max;
		return total;
	}

	public int setSkill(int skill) {
		return this.skill = getMaxValuebyPercentage(TOTAL_SKILL_FOR_CALC, MAX_SKILL_PERCENTAGE, skill);
	}

	public int getLuck() {
		int total = luck + getConditionsByType(StatType.LUCK);
		int max = getMaxValuebyPercentage(TOTAL_LUCK_FOR_CALC, MAX_LUCK_PERCENTAGE, total);
		if (total > max)
			total = max;
		return total;
	}

	public int setLuck(int skill) {
		return this.luck = getMaxValuebyPercentage(TOTAL_LUCK_FOR_CALC, MAX_LUCK_PERCENTAGE, skill);
	}

	public int getLuckPercentage() {
		return Stats.getPercentage(getLuck(), getTotalStat(TOTAL_LUCK_FOR_CALC));
	}

	public int getSkillPercentage() {
		return Stats.getPercentage(getSkill(), getTotalStat(TOTAL_SKILL_FOR_CALC));
	}

	public int getDefensePercentage() {
		return Stats.getPercentage(getDefense(), getTotalStat(TOTAL_ARMOR_FOR_CALC));
	}

	private int getTotalStat(int stat) {
		if (character != null) {
			stat += character.getLevel();
		}
		return stat;
	}

	private int getValuePerc(int cap, int perc) {
		return (int) (((float) cap / 100f) * perc);
	}

	public static int getPercentage(int value, int what) {
		float res = ((float) value / (float) what);
		return (int) (res * 100);
	}

	public int getAttack() {
		int total = attack + getConditionsByType(StatType.ATTACK);
		if (total < 1) {
			total = 1;
		}
		return total;
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

	public int getValueByBonusType(StatType type) {
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
		if (resultHealth < 0) {
			resultHealth = 0;
		}
		releaseStats.setHealth(resultHealth);
		this.health -= releaseStats.health;
		this.attack -= releaseStats.attack;
		this.defense -= releaseStats.defense;
		this.skill -= releaseStats.skill;
		this.luck -= releaseStats.luck;
		this.damage -= releaseStats.damage;
		this.skillpower -= releaseStats.skillpower;
	}

	public void setHolder(Stats holder) {
		this.holder = holder;
	}

	public int getSpecialSkillPower() {
		int skillPower = getSkillpower();
		float one = (float) skillPower / 100f;
		int percCalc = TOTAL_SKILL_FOR_CALC - skillPower;
		if (percCalc <= 0) {
			percCalc = 1;
		}
		int addition = (int) (skillPower + one * Stats.getPercentage(skill, percCalc));
		return skillPower + addition;
	}

	public void nullAllAttributes() {
		this.health = 0;
		this.skill = 0;
		this.skillpower = 0;
		this.damage = 0;
		this.defense = 0;
		this.luck = 0;
		this.attack = 0;
	}

	public int getConditionsByType(StatType type) {
		int value = 0;
		if (isBase)
			return value;
		for (Bonus b : character.getConditions()) {
			
			if (b.getType().equals(type)) {
				value += b.getCoeff() * b.getValue();
			}
		}
		return value;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}

	public Integer getTotalPureDamage() {
		return getAttack() * getDamage();
	}
}
