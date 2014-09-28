package com.nex.gamebook.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.util.Log;
import android.util.TimeUtils;

import com.nex.gamebook.R;

public class Statistics {
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD })
	public @interface Data {
		int value();

		boolean ignore() default false;

		boolean format() default false;
	}

	@Data(1)
	private int killedMobs = 0;
	@Data(2)
	private int killedMinions = 0;
	@Data(3)
	private int killedBosses = 0;
	@Data(4)
	private int totalKilledEnemies = 0;
	@Data(5)
	private int totalTurns = 0;
	@Data(6)
	private int totalBattles = 0;

	@Data(7)
	private int criticalHits = 0;
	@Data(7)
	private int obtainedCriticalHits = 0;
	@Data(8)
	private int usedSkills = 0;
	@Data(9)
	private long maxAttackDamageGiven = 0;
	@Data(10)
	private long maxSkillDamageGiven = 0;

	@Data(11)
	private long maxSkillDamageTaken = 0;
	@Data(12)
	private long maxAttackDamageTaken = 0;

	@Data(13)
	private long totalAttackDamageTaken = 0;
	@Data(14)
	private long totalSkillDamageTaken = 0;
	@Data(15)
	private long totalDamageTaken = 0;

	@Data(16)
	private long totalAttackDamageGiven = 0;
	@Data(17)
	private long totalSkillDamageGiven = 0;
	@Data(18)
	private long totalDamageGiven = 0;

	@Data(19)
	private long maxHealthHealed = 0;
	@Data(20)
	private long totalHealthHealed = 0;

	@Data(21)
	private int totalDodgedAttacks = 0;
	@Data(22)
	private int totalMissedAttacks = 0;

	@Data(23)
	private long totalAttackDamageReduce = 0;
	@Data(24)
	private long totalSkillDamageReduce = 0;
	@Data(25)
	private long totalDamageReduce = 0;

	@Data(26)
	private long totalObtainedAttackDamageReduce = 0;
	@Data(27)
	private long totalObtainedSkillDamageReduce = 0;
	@Data(28)
	private long totalObtainedDamageReduce = 0;

	@Data(29)
	private int visitedSections = 0;
	@Data(30)
	private int sections = 0;

	@Data(value = 31, format = true)
	private long timespent = 0;

	public Statistics() {
		// TODO Auto-generated constructor stub
	}

	public Statistics(Statistics s) throws IllegalAccessException, IllegalArgumentException {
		for (Field f : s.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			f.set(this, f.get(s));
			f.setAccessible(false);
		}
	}

	public void addHealedHealth(long val) {
		totalHealthHealed += val;
		if (maxHealthHealed < val) {
			maxHealthHealed = val;
		}
	}

	public void addKilledEnemy() {
		this.totalKilledEnemies += 1;
	}

	public void addKilledBoss() {
		this.killedBosses += 1;
	}

	public void addKilledMinion() {
		this.killedMinions += 1;
	}

	public void addKilledMob() {
		this.killedMobs += 1;
	}

	public void addDodgedAttack() {
		this.totalDodgedAttacks += 1;
	}

	public void addMissedAttack() {
		this.totalMissedAttacks += 1;
	}

	public void addAttackReducedDamage(long dmg) {
		this.totalDamageReduce += dmg;
		this.totalAttackDamageReduce += dmg;
	}

	public void addSkillReducedDamage(long dmg) {
		this.totalDamageReduce += dmg;
		this.totalSkillDamageReduce += dmg;
	}

	public void addObtainedAttackReducedDamage(long dmg) {
		this.totalObtainedDamageReduce += dmg;
		this.totalObtainedAttackDamageReduce += dmg;
	}

	public void addObtainedSkillReducedDamage(long dmg) {
		this.totalObtainedDamageReduce += dmg;
		this.totalObtainedSkillDamageReduce += dmg;
	}

	public void addUsedSkill() {
		this.usedSkills++;
	}

	public void addCriticalHit() {
		criticalHits++;
	}

	public void addObtainedCriticalHit() {
		this.obtainedCriticalHits++;
	}

	public void addSkillGivenDamage(long dmg) {
		totalDamageGiven += dmg;
		totalSkillDamageGiven += dmg;
		if (maxSkillDamageGiven < dmg) {
			maxSkillDamageGiven = dmg;
		}
	}

	public void addSkillTakenDamage(long dmg) {
		totalDamageTaken += dmg;
		totalSkillDamageTaken += dmg;
		if (maxSkillDamageTaken < dmg) {
			maxSkillDamageTaken = dmg;
		}
	}

	public void addAttackGivenDamage(long dmg) {
		totalDamageGiven += dmg;
		totalAttackDamageGiven += dmg;
		if (maxAttackDamageGiven < dmg) {
			maxAttackDamageGiven = dmg;
		}
	}

	public void addAttackTakenDamage(long dmg) {
		totalDamageTaken += dmg;
		totalAttackDamageTaken += dmg;
		if (maxAttackDamageTaken < dmg) {
			maxAttackDamageTaken = dmg;
		}
	}

	public void addSection() {
		this.sections++;
	}

	public void addVisitedSection() {
		this.visitedSections++;
	}

	public void addTurn() {
		this.totalTurns++;
	}

	public void addBattle() {
		this.totalBattles++;
	}

	public List<StatisticItem> asList() {
		List<StatisticItem> stats = new ArrayList<>();
		for (Field f : getClass().getDeclaredFields()) {
			try {
				Data d = getData(f);
				if (d == null || d.ignore())
					continue;
				StatisticItem i = new StatisticItem();
				i.id = R.string.class.getDeclaredField(f.getName()).getInt(null);
				i.value = convert(f.get(this), d.format());
				i.position = d.value();
				stats.add(i);
			} catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
				Log.w("Statistics", f.getName() + " no exist in " + R.string.class.getName());
			}

		}
		Collections.sort(stats);
		return stats;
	}

	public Object convert(Object value, boolean format) {
		if(!format) return value;
		Long millis = Long.valueOf(value.toString());
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) -  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
		String formatedTime = String.format("%dh %dm %ds", hours, minutes, seconds);
		return formatedTime;
	}

	public static void main(String[] args) {

		long res = 1000 * 60 * 60 * 24 * 3;
		res += 1000 * 60 * 60 * 6;
		res += 1000 * 60 * 5;
		res += 10000;

		System.out.println(new Statistics().convert(res, true));

	}

	private Data getData(Field f) {
		for (Annotation ann : f.getDeclaredAnnotations()) {
			if (ann instanceof Data) {
				return (Data) ann;
			}
		}
		return null;
	}

	public static class StatisticItem implements Comparable<StatisticItem> {
		private int id;
		private int position;
		private Object value;

		public int getId() {
			return id;
		}

		public Object getValue() {
			return value;
		}

		public int getPosition() {
			return position;
		}

		@Override
		public int compareTo(StatisticItem another) {

			return -Integer.valueOf(another.position).compareTo(position);
		}
	}

	public void addTimeSpent(long l) {
		this.timespent += l;
	}

	public int getKilledMobs() {
		return killedMobs;
	}

	public int getKilledMinions() {
		return killedMinions;
	}

	public int getKilledBosses() {
		return killedBosses;
	}

	public int getTotalKilledEnemies() {
		return totalKilledEnemies;
	}

	public int getTotalTurns() {
		return totalTurns;
	}

	public int getTotalBattles() {
		return totalBattles;
	}

	public int getCriticalHits() {
		return criticalHits;
	}

	public int getObtainedCriticalHits() {
		return obtainedCriticalHits;
	}

	public int getUsedSkills() {
		return usedSkills;
	}

	public long getMaxAttackDamageGiven() {
		return maxAttackDamageGiven;
	}

	public long getMaxSkillDamageGiven() {
		return maxSkillDamageGiven;
	}

	public long getMaxSkillDamageTaken() {
		return maxSkillDamageTaken;
	}

	public long getMaxAttackDamageTaken() {
		return maxAttackDamageTaken;
	}

	public long getTotalAttackDamageTaken() {
		return totalAttackDamageTaken;
	}

	public long getTotalSkillDamageTaken() {
		return totalSkillDamageTaken;
	}

	public long getTotalDamageTaken() {
		return totalDamageTaken;
	}

	public long getTotalAttackDamageGiven() {
		return totalAttackDamageGiven;
	}

	public long getTotalSkillDamageGiven() {
		return totalSkillDamageGiven;
	}

	public long getTotalDamageGiven() {
		return totalDamageGiven;
	}

	public long getMaxHealthHealed() {
		return maxHealthHealed;
	}

	public long getTotalHealthHealed() {
		return totalHealthHealed;
	}

	public int getTotalDodgedAttacks() {
		return totalDodgedAttacks;
	}

	public int getTotalMissedAttacks() {
		return totalMissedAttacks;
	}

	public long getTotalAttackDamageReduce() {
		return totalAttackDamageReduce;
	}

	public long getTotalSkillDamageReduce() {
		return totalSkillDamageReduce;
	}

	public long getTotalDamageReduce() {
		return totalDamageReduce;
	}

	public long getTotalObtainedAttackDamageReduce() {
		return totalObtainedAttackDamageReduce;
	}

	public long getTotalObtainedSkillDamageReduce() {
		return totalObtainedSkillDamageReduce;
	}

	public long getTotalObtainedDamageReduce() {
		return totalObtainedDamageReduce;
	}

	public int getVisitedSections() {
		return visitedSections;
	}

	public int getSections() {
		return sections;
	}

	public long getTimespent() {
		return timespent;
	}

}
