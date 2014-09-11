package com.nex.gamebook.game;

import com.nex.gamebook.skills.active.Skill;

public class ResultCombat {
	private CharacterType type;
	private boolean critical;
	private double multiply;
	private Skill specialAttack;
	private boolean luck;
	private int damage;
	private String enemyName;
	private boolean cannotAttack;
	private boolean cannotCast;

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
		if (multiply == 1d) {
			return "2x";
		}
		return "1.5x";
	}

	public Skill getSpecialAttack() {
		return specialAttack;
	}

	public void setSpecialAttack(Skill specialAttack) {
		this.specialAttack = specialAttack;
	}

	public boolean isCannotAttack() {
		return cannotAttack;
	}

	public void setCannotAttack(boolean cannotAttack) {
		this.cannotAttack = cannotAttack;
	}

	public boolean isCannotCast() {
		return cannotCast;
	}

	public void setCannotCast(boolean cannotCast) {
		this.cannotCast = cannotCast;
	}

}
