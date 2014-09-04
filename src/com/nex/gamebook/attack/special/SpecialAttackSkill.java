package com.nex.gamebook.attack.special;

import java.util.Collections;
import java.util.List;

import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.Enemy;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.SpecialSkillsMap;
import com.nex.gamebook.playground.BattleLogCallback;

public abstract class SpecialAttackSkill implements SpecialSkill {
	
	protected int cycles = 0;
//	protected boolean used = false;

	public Enemy resolveEnemy(Character applicationChar, Character character) {
		if (applicationChar instanceof Enemy)
			return (Enemy) applicationChar;
		return (Enemy) character;
	}

	@Override
	public boolean canUse() {
		int max = attemptsPerFight();
		if ((max > 0 && cycles >= max))
			return false;
		return true;
	}

	public ResultCombat createBasicResult(int value, CharacterType type, Enemy enemy) {
		ResultCombat result = new ResultCombat();
		result.setSpecialAttack(this);
		result.setDamage(value);
		result.setType(type);
		result.setEnemyName(enemy.getName());
		return result;
	}

	public Bonus createSpecialAttack(int coeff, int value, StatType type) {
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
	public boolean doAttack(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat resultCombat) {

		if (!canUse())
			return true;
		cycles++;
		return doAttackOnce(attacker, attacked, callback, resultCombat);
	}
	
	public abstract boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm);

	public void addTemporalBonus(Character applicationChar, Bonus bonus, String conditionid) {
		List<Bonus> tattacks = applicationChar.getConditions();
		Bonus b = applicationChar.findConditionById(conditionid);
		if (b == null) {
			b = createSpecialAttack(1, (bonus.getCoeff() * -1) * bonus.getValue(), bonus.getType());
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
		if (c.hasLuck()) {
			value = getValueWhenLuck(c);
		}
		return value;
	}

	public String createConditionId(Character attacker) {
		String conditionid = this.getClass().getName() + "_" + attacker.getClass().getName();
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
	public void cleanAfterFightEnd() {
		if(inFight())
		this.cycles = 0;
	}

	@Override
	public void cleanAfterBattleEnd() {
		this.cycles = 0;
	}

	@Override
	public int attemptsPerFight() {
		return -1;
	}

	public int calcDynamicValue(int base, float coeff, int specialSkillPower) {
		return (int) (base * (coeff + specialSkillPower / 10));
	}

	@Override
	public boolean inFight() {
		return true;
	}

	@Override
	public boolean isDebuff() {
		return false;
	}

	@Override
	public List<String> getBestAgainstSkill() {
		return Collections.emptyList();
	}

	@Override
	public List<String> getBestInterceptSkills() {
		return Collections.emptyList();
	}

	public int getCountOfUsed() {
		return cycles;
	}

	@Override
	public boolean isTriggerBeforeEnemySpecialAttack() {
		return false;
	}

	@Override
	public boolean doSomething(Character attacked, Character attacker) {
		return true;
	}
	@Override
	public boolean isOverTime() {
		return true;
	}
	@Override
	public String getSkillNameKey() {
		return SpecialSkillsMap.getSkillNameKey(getClass());
	}
	
	@Override
	public int getOvertimeTurns() {
		return -1;
	}
	@Override
	public boolean causeDamage() {
		return true;
	}
}
