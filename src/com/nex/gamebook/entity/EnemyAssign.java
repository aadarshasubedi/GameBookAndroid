package com.nex.gamebook.entity;

public class EnemyAssign {

	private String enemyKey;
	private String enemySkill;

	public EnemyAssign(String enemyKey, String enemySkill) {
		super();
		this.enemyKey = enemyKey;
		this.enemySkill = enemySkill;
	}

	public String getEnemyKey() {
		return enemyKey;
	}

	public String getEnemySkill() {
		return enemySkill;
	}
}
