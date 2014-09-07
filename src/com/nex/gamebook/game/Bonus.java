package com.nex.gamebook.game;

import java.io.Serializable;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;

public class Bonus implements Cancelable {
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
		HEALTH(R.string.attr_health, 30, 1), 
		LUCK(R.string.attr_luck, 2, 1), 
		SKILL(R.string.attr_skill, 2, 1), 
		DEFENSE(R.string.attr_defense, 1, 1), 
		ATTACK(R.string.attr_attack, 1, 1), 
		DAMAGE(R.string.attr_baseDmg, 2, 10), 
		SKILLPOWER(R.string.attr_skill_power, 2, 3);
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
	private boolean base;
	private int coeff;
	private boolean alreadyGained;
	private boolean permanent;
	private boolean condition;
	private int turns;
	private int currentTurn;

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

	public boolean isBase() {
		return base;
	}

	public void setBase(boolean base) {
		this.base = base;
	}

	public int getTurns() {
		return turns;
	}

	public void setTurns(int turns) {
		this.turns = turns;
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	public boolean isExhausted() {
		if (this.turns == SpecialAttackSkill.NO_VALUE)
			return false;
		return this.currentTurn == turns;
	}

	public int getRemainsTurns() {
		return turns - currentTurn;
	}
	@Override
	public boolean isNegative() {
		return coeff<0;
	}
}
