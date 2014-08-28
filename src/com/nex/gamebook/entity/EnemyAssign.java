package com.nex.gamebook.entity;

public class EnemyAssign {

	private String enemyKey;
	private String enemySkill;
	private double xpcoeff = 0f;

	public EnemyAssign(String enemyKey, String enemySkill, double xpcoeff) {
		super();
		this.enemyKey = enemyKey;
		this.enemySkill = enemySkill;
		this.xpcoeff = xpcoeff;
	}

	public String getEnemyKey() {
		return enemyKey;
	}

	public String getEnemySkill() {
		return enemySkill;
	}

	public double getXpcoeff() {
		return xpcoeff;
	}
}
