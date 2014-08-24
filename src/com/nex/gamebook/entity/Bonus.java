package com.nex.gamebook.entity;

import java.io.Serializable;

import com.nex.gamebook.R;

public class Bonus implements Serializable, Mergable {
	private static final long serialVersionUID = 4130822562659948335L;

	private transient String conditionId;

	public enum BonusState implements Serializable {
		BEFORE_FIGHT, AFTER_FIGHT, NORMAL;

		public static BonusState getStateByString(String s) {
			if (s == null || "".equals(s)) {
				return BonusState.NORMAL;
			}
			return BonusState.valueOf(s.toUpperCase());
		}
	}

	public enum BonusType implements Serializable {
		HEALTH(R.string.attr_health), LUCK(R.string.attr_luck), SKILL(
				R.string.attr_skill), DEFENSE(R.string.attr_defense), ATTACK(
				R.string.attr_attack), DAMAGE(R.string.attr_baseDmg), SKILLPOWER(
				R.string.attr_skill_power);
		public int text;

		private BonusType(int text) {
			this.text = text;
		}

		public int getText() {
			return text;
		}
	}

	private BonusState state;
	private BonusType type;
	private int value;
	private boolean overflowed;
	private int coeff;
	private boolean alreadyGained;
	private boolean permanent;
	private boolean condition;
	private boolean base;

	public BonusType getType() {
		return type;
	}

	public void setType(BonusType type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isOverflowed() {
		return overflowed;
	}

	public void setOverflowed(boolean overflowed) {
		this.overflowed = overflowed;
	}

	public int getText() {
		return this.type.getText();
	}

	public int getCoeff() {
		return coeff;
	}

	public void setCoeff(int coeff) {
		this.coeff = coeff;
	}

	public BonusState getState() {
		return state;
	}

	public void setState(BonusState state) {
		this.state = state;
	}

	public boolean isAlreadyGained() {
		return alreadyGained;
	}

	public void setAlreadyGained(boolean alreadyGained) {
		this.alreadyGained = alreadyGained;
	}

	public boolean isPermanent() {
		return permanent;
	}

	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}

	public boolean isCondition() {
		return condition;
	}

	public void setCondition(boolean condition) {
		this.condition = condition;
	}

	public String getConditionId() {
		return conditionId;
	}

	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}

	public boolean isBase() {
		return base;
	}

	public void setBase(boolean base) {
		this.base = base;
	}

}
