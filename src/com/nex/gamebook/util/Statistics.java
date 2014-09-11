package com.nex.gamebook.util;

public class Statistics {

	private long maxAttackDamageGiven = 0;
	private long maxAttackDamageTaken = 0;
	private long maxSkillDamageTaken = 0;
	private long maxSkillDamageGiven = 0;
	private long totalAttackDamageTaken = 0;
	private long totalAttackDamageGiven = 0;
	private long totalSkillDamageTaken = 0;
	private long totalSkillDamageGiven = 0;
	private long totalDamageTaken = 0;
	private long totalDamageGiven = 0;
	private long totalHealthHealed = 0;
	private long maxHealthHealed = 0;
	private long totalDamageReduce = 0;
	private long totalAttackDamageReduce = 0;
	private long totalSkillDamageReduce = 0;

	private int criticalHits = 0;
	private int usedSkills = 0;
	private int sections = 0;
	private int visitedSections = 0;
	private int totalDodgedAttacks = 0;
	private int totalMissedAttacks = 0;
	private int totalKilledEnemies = 0;
	private int killedBosses = 0;
	private int killedMinions = 0;
	private int killedMobs = 0;

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

	public void addUsedSkill() {
		this.usedSkills++;
	}

	public void addCriticalHit() {
		criticalHits++;
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
			maxSkillDamageGiven = dmg;
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

	public long getMaxAttackDamageGiven() {
		return maxAttackDamageGiven;
	}

	public long getMaxAttackDamageTaken() {
		return maxAttackDamageTaken;
	}

	public long getMaxSkillDamageTaken() {
		return maxSkillDamageTaken;
	}

	public long getMaxSkillDamageGiven() {
		return maxSkillDamageGiven;
	}

	public long getTotalAttackDamageTaken() {
		return totalAttackDamageTaken;
	}

	public long getTotalAttackDamageGiven() {
		return totalAttackDamageGiven;
	}

	public long getTotalSkillDamageTaken() {
		return totalSkillDamageTaken;
	}

	public long getTotalSkillDamageGiven() {
		return totalSkillDamageGiven;
	}

	public long getTotalDamageTaken() {
		return totalDamageTaken;
	}

	public long getTotalDamageGiven() {
		return totalDamageGiven;
	}

	public int getCriticalHits() {
		return criticalHits;
	}

	public int getUsedSkills() {
		return usedSkills;
	}

	public int getSections() {
		return sections;
	}

	public int getVisitedSections() {
		return visitedSections;
	}

	public int getTotalDodgedAttacks() {
		return totalDodgedAttacks;
	}

	public int getTotalMissedAttacks() {
		return totalMissedAttacks;
	}

	public int getTotalKilledEnemies() {
		return totalKilledEnemies;
	}

	public int getKilledBosses() {
		return killedBosses;
	}

	public int getKilledMinions() {
		return killedMinions;
	}

	public int getKilledMobs() {
		return killedMobs;
	}

	public long getMaxHealthHealed() {
		return maxHealthHealed;
	}

	public long getTotalHealthHealed() {
		return totalHealthHealed;
	}

	public long getTotalDamageReduce() {
		return totalDamageReduce;
	}

	public long getTotalAttackDamageReduce() {
		return totalAttackDamageReduce;
	}

	public long getTotalSkillDamageReduce() {
		return totalSkillDamageReduce;
	}

}
