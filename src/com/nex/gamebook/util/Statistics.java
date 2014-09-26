package com.nex.gamebook.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.nex.gamebook.R;

public class Statistics {
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD })
	public @interface Position {
		int value();
	}

	@Position(1)
	private int killedMobs = 0;
	@Position(2)
	private int killedMinions = 0;
	@Position(3)
	private int killedBosses = 0;
	@Position(4)
	private int totalKilledEnemies = 0;
	@Position(5)
	private int totalTurns = 0;
	@Position(6)
	private int totalBattles = 0;

	@Position(7)
	private int criticalHits = 0;
	@Position(7)
	private int obtainedCriticalHits = 0;
	@Position(8)
	private int usedSkills = 0;
	@Position(9)
	private long maxAttackDamageGiven = 0;
	@Position(10)
	private long maxSkillDamageGiven = 0;

	@Position(11)
	private long maxSkillDamageTaken = 0;
	@Position(12)
	private long maxAttackDamageTaken = 0;

	@Position(13)
	private long totalAttackDamageTaken = 0;
	@Position(14)
	private long totalSkillDamageTaken = 0;
	@Position(15)
	private long totalDamageTaken = 0;

	@Position(16)
	private long totalAttackDamageGiven = 0;
	@Position(17)
	private long totalSkillDamageGiven = 0;
	@Position(18)
	private long totalDamageGiven = 0;

	@Position(19)
	private long maxHealthHealed = 0;
	@Position(20)
	private long totalHealthHealed = 0;

	@Position(21)
	private int totalDodgedAttacks = 0;
	@Position(22)
	private int totalMissedAttacks = 0;

	@Position(23)
	private long totalAttackDamageReduce = 0;
	@Position(24)
	private long totalSkillDamageReduce = 0;
	@Position(25)
	private long totalDamageReduce = 0;

	@Position(26)
	private long totalObtainedAttackDamageReduce = 0;
	@Position(27)
	private long totalObtainedSkillDamageReduce = 0;
	@Position(28)
	private long totalObtainedDamageReduce = 0;

	
	@Position(29)
	private int visitedSections = 0;
	@Position(20)
	private int sections = 0;

	public Statistics() {
		// TODO Auto-generated constructor stub
	}
	
	public Statistics(Statistics s) throws IllegalAccessException, IllegalArgumentException {
		for(Field f: s.getClass().getDeclaredFields()) {
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
				StatisticItem i = new StatisticItem();
				i.id = R.string.class.getDeclaredField(f.getName()).getInt(null);
				i.value = f.get(this);
				i.position = getPosition(f);
				stats.add(i);
			} catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
				Log.w("Statistics", f.getName() + " no exist in " + R.string.class.getName());
			}

		}
		Collections.sort(stats);
		return stats;
	}

	private int getPosition(Field f) {
		for (Annotation ann : f.getDeclaredAnnotations()) {
			if (ann instanceof Position) {
				return ((Position) ann).value();
			}
		}
		return 0;
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

}
