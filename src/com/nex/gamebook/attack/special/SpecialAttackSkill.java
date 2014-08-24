package com.nex.gamebook.attack.special;

import java.util.List;

import com.nex.gamebook.entity.Bonus;
import com.nex.gamebook.entity.Bonus.BonusType;
import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.CharacterType;
import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.playground.AttackCallback;

public abstract class SpecialAttackSkill implements SpecialSkill {
	private int cycles = 0;
	private static final long serialVersionUID = -7422695719062137022L;

	public Enemy resolveEnemy(Character applicationChar, Character character) {
		if(applicationChar instanceof Enemy) return (Enemy) applicationChar;
		return (Enemy) character;
	}
	
	public ResultCombat createBasicResult(int value, CharacterType type) {
		ResultCombat result = new ResultCombat();
		result.setSpecialAttack(this);
		result.setDamage(value);
		result.setType(type);
		return result;
	}
	public Bonus createSpecialAttack(int coeff , int value, BonusType type) {
		Bonus bonus = new Bonus();
		bonus.setValue(value);
		bonus.setType(type);
		bonus.setCoeff(coeff);
		bonus.setPermanent(true);
		bonus.setCondition(true);
		bonus.setOverflowed(true);
		return bonus;
	}
	
	@Override
	public boolean doAttack(Character attacker, Character attacked,
			AttackCallback callback, ResultCombat resultCombat) {
		int max = attemptsPerFight();
		if (max > 0 && cycles >= max)
			return true;
		cycles++;
		return doAttackOnce(attacker, attacked, callback, resultCombat);
	}
	public abstract boolean doAttackOnce(Character attacker, Character attacked, AttackCallback callback, ResultCombat cm);
	public void addTemporalBonus(Character applicationChar, Bonus bonus, String conditionid) {
		List<Bonus> tattacks = applicationChar.getConditions();
		Bonus b = applicationChar.findConditionById(conditionid);
		if (b==null) {
			b = createSpecialAttack(bonus.getCoeff()*-1, bonus.getValue(), bonus.getType());
			b.setConditionId(conditionid);
			tattacks.add(b);
		} else {
			b.setValue(b.getValue() + bonus.getValue());
		}
	}
	
	public int getResultValuePercentage(int value, int perc) {
		float res = ((float) value / 100f);
		return (int) (res * perc);
	}
	
	public int getRealValue(Character c) {
		int value = getValue(c);
		if(c.hasLuck()) {
			value = getValueWhenLuck(c);
		}
		return value;
	}
	
	public String createConditionId(Character attacker) {
		String conditionid = attacker.getSpecialSkill().getClass().getName() 
				+ "_" + attacker.getClass().getName();
		return conditionid;
	}
	
	@Override
	public boolean isTriggerBeforeEnemyAttack() {
		return false;
	}

	@Override
	public boolean isTriggerAfterEnemyAttack() {
		return false;
	}
	
	@Override
	public boolean afterNormalAttack() {
		return false;
	}
	@Override
	public void clean() {
		this.cycles = 0;
	}
	@Override
	public int attemptsPerFight() {
		return -1;
	}
	
	public int calcDynamicValue(int base, float coeff, int specialSkillPower) {
		return (int) (base * (coeff + specialSkillPower / 100));
	}
	
}
