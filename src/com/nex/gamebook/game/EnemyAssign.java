package com.nex.gamebook.game;

public class EnemyAssign {

	private String enemyKey;
	private float xpcoeff = 0f;

	public EnemyAssign(String enemyKey, float xpcoeff) {
		super();
		this.enemyKey = enemyKey;
		this.xpcoeff = xpcoeff;
	}

	public String getEnemyKey() {
		return enemyKey;
	}

	public double getXpcoeff() {
		return xpcoeff;
	}
}
