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

	public enum StatType implements Serializable {
		HEALTH(R.string.attr_health, 5, 1), 
		LUCK(R.string.attr_luck, 1, 2), 
		SKILL(R.string.attr_skill, 1, 2), 
		DEFENSE(R.string.attr_defense, 1, 2), 
		ATTACK(R.string.attr_attack, 1, 2), 
		DAMAGE(R.string.attr_baseDmg, 1, 10), 
		SKILLPOWER(R.string.attr_skill_power, 1, 5);
		public int text;
		public int baseValue;
		public int increaseByLevel;
		private StatType(int text, int baseValue, int incBylvl) {
			this.text = text;
			this.baseValue = baseValue;
			this.increaseByLevel = incBylvl;
		}

		public int getBaseValue() {
			return baseValue;
		}

		public int getText() {
			return text;
		}
		public int getIncreaseByLevel() {
			return increaseByLevel;
		}
	}

	private BonusState state;
	private StatType type;
	private int value;
	private boolean overflowed;
	private int coeff;
	private boolean alreadyGained;
	private boolean permanent;
	private boolean condition;
	private boolean base;

	public StatType getType() {
		return type;
	}

	public void setType(StatType type) {
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
