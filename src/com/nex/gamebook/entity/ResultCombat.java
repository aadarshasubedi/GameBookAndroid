package com.nex.gamebook.entity;

public class ResultCombat {
	private CharacterType type;
	private boolean critical;
	private double multiply;
	private boolean luck;
	private int damage;
	private String enemyName;

	public boolean isCritical() {
		return critical;
	}

	public void setCritical(boolean critical) {
		this.critical = critical;
	}

	public CharacterType getType() {
		return type;
	}

	public void setType(CharacterType type) {
		this.type = type;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		if (damage < 0) {
			damage = 0;
		}
		this.damage = damage;
	}

	public boolean isLuck() {
		return luck;
	}

	public void setLuck(boolean luck) {
		this.luck = luck;
	}

	public String getEnemyName() {
		return enemyName;
	}

	public void setEnemyName(String enemyName) {
		this.enemyName = enemyName;
	}

	public double getMultiply() {
		return multiply;
	}

	public void setMultiply(double multiply) {
		this.multiply = multiply;
	}

	public String getMultiplyAsText() {
		if(multiply == 1d) {
			return "2x";
		}
		return "0.5x";
	}
	
}
