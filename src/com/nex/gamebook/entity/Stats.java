package com.nex.gamebook.entity;

import java.io.Serializable;
import java.lang.reflect.Method;

import android.util.Log;

import com.nex.gamebook.entity.Bonus.StatType;
import com.nex.gamebook.entity.io.GameBookUtils;

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
	private Player player;
	private Stats holder;

	public Stats() {
	}
	public Stats(Stats stats,int positive) {
		this.health = positive*stats.health;
		this.defense = positive*stats.defense;
		this.skill = positive*stats.skill;
		this.luck = positive*stats.luck;
		this.attack = positive*stats.attack;
		this.damage = positive*stats.damage;
		this.skillpower = positive*stats.skillpower;
	}
	public Stats(Stats stats) {
		this(stats, 1);
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

	public int getSkillpower() {
		return skillpower;
	}

	public int setSkillpower(int skillpower) {
		return this.skillpower = skillpower;
	}

	public int setDefense(int defense) {
		int total = getTotalStat(TOTAL_ARMOR_FOR_CALC);
		int perc = Stats.getPercentage(defense, total);
		if (perc > MAX_DEFENSE_PERCENTAGE) {
			defense = getValuePerc(total, MAX_DEFENSE_PERCENTAGE);
		}
		return this.defense = defense;
	}

	public int getSkill() {
		return skill;
	}

	public int setSkill(int skill) {
		int total = getTotalStat(TOTAL_SKILL_FOR_CALC);
		int perc = Stats.getPercentage(skill, total);
		if (perc > MAX_DEFENSE_PERCENTAGE) {
			skill = getValuePerc(total, MAX_SKILL_PERCENTAGE);
		}
		return this.skill = skill;
	}

	public int getLuck() {
		return luck;
	}

	public int setLuck(int luck) {
		int total = getTotalStat(TOTAL_LUCK_FOR_CALC);
		int perc = Stats.getPercentage(luck, total);
		if (perc > MAX_DEFENSE_PERCENTAGE) {
			luck = getValuePerc(total, MAX_LUCK_PERCENTAGE);
		}
		return this.luck = luck;
	}

	public int getLuckPercentage() {
		return Stats.getPercentage(luck, getTotalStat(TOTAL_LUCK_FOR_CALC));
	}

	public int getSkillPercentage() {
		return Stats.getPercentage(skill, getTotalStat(TOTAL_SKILL_FOR_CALC));
	}

	public int getDefensePercentage() {
		return Stats.getPercentage(defense, getTotalStat(TOTAL_ARMOR_FOR_CALC));
	}

	private int getTotalStat(int stat) {
		if (player != null) {
			stat += player.getLevel();
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

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
