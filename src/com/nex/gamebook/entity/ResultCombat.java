package com.nex.gamebook.entity;

public class ResultCombat {
	private CharacterType type;
	private boolean critical;
	private boolean luck;
	private int damage;

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
		if(damage < 0) {
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
}
