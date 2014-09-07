package com.nex.gamebook.attack.special;

import java.util.List;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.Enemy;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public abstract class SpecialAttackSkill implements SpecialSkill, CombatTextDispatcher {

	public static int NO_VALUE = -1;
	protected int cycles = 0;
	protected int constantValue;
	protected SkillProperties properties = new SkillProperties();
	protected String skillName;

	public SpecialAttackSkill(int constantValue) {
		super();
		this.constantValue = constantValue;
		setCombatTextDispatcher(this);
	}

	// protected boolean used = false;

	public Enemy resolveEnemy(Character attacker, Character attacked) {
		if (attacker instanceof Enemy)
			return (Enemy) attacker;
		return (Enemy) attacked;
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
//		if (coeff > 0 && type.equals(StatType.HEALTH))
//			bonus.setOverflowed(false);
//		else
			bonus.setOverflowed(false);
//		is
		bonus.setCondition(true);
		return bonus;
	}
	/**
	 * Make reduction if skill is conditional.
	 * @param attacker
	 * @param attacked
	 * @return
	 */
	protected Bonus createReductedBonus(Character attacker, Character attacked) {
		Bonus bonus = createSpecialAttack(isCondition() ? -1 : 1, getValue(attacker), getType());
		if(isCondition()) {
			//process reduction
			int bonusValue = bonus.getValue();
			int defense = attacked.getCurrentStats().getDefensePercentage();
			bonus.setValue((int) (bonusValue - ((double) bonusValue / 100d) * defense));
		} 
		bonus.setTurns(getOvertimeTurns());
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


	public int getResultValuePercentage(int value, int perc) {
		float res = ((float) value / 100f);
		return (int) (res * perc);
	}

	public String createConditionId(Character attacker) {
		String conditionid = this.getClass().getName() + "_" + attacker.getClass().getName();
		return conditionid;
	}

	@Override
	public boolean isTriggerBeforeEnemyAttack() {
		return properties.isBeforeEnemyAttack();
	}

	@Override
	public boolean isTriggerAfterEnemyAttack() {
		return properties.isAfterEnemyAttack();
	}

	@Override
	public boolean afterNormalAttack() {
		return properties.isAfterNormalAttack();
	}

	@Override
	public boolean isTriggerBeforeEnemySpecialAttack() {
		return properties.isBeforeEnemySkill();
	}

	@Override
	public boolean isPermanent() {
		return properties.isPermanent();
	}

	@Override
	public void cleanAfterFightEnd() {
		if (!resetAtBattleEnd())
			cleanAfterBattleEnd();
	}

	@Override
	public void cleanAfterBattleEnd() {
		this.cycles = 0;
	}

	@Override
	public int attemptsPerFight() {
		return properties.getAttempts();
	}

	@Override
	public boolean resetAtBattleEnd() {
		return properties.isResetAtBattleEnd();
	}

	public int getCountOfUsed() {
		return cycles;
	}

	@Override
	public boolean doSomething(Character attacked, Character attacker) {
		return true;
	}

	@Override
	public boolean isOverTime() {
		return false;
	}

	@Override
	public int getOvertimeTurns() {
		return this.properties.getTurns();
	}

	@Override
	public boolean causeDamage() {
		return true;
	}
	public int calcDynamicValue(int base, int value, Character attacker) {
		return (int) (base * (properties.getCoeff() + value / 10));
	}
	@Override
	public int getValue(Character character) {
		if (constantValue != NO_VALUE)
			return constantValue;

		return getValueBasedOnSkillPower(character);
	}

	public int getValueBasedOnSkillPower(Character c) {
		return (int) (c.getCurrentStats().getSpecialSkillPower() + (c.getCurrentStats().getSpecialSkillPower() * properties.getCoeff()));
	}

	public String getName() {
		return skillName;
	}

	public void setData(SkillProperties properties, String translatedSkillName) {
		redefineProperties(properties);
		this.properties = properties;
		this.skillName = translatedSkillName;
	}
	protected void redefineProperties(SkillProperties properties) {
		
	}
	
	CombatTextDispatcher dispatcher;
	
	@Override
	public void setCombatTextDispatcher(CombatTextDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	public CombatTextDispatcher getCombatTextDispatcher() {
		return dispatcher;
	}
	
	@Override
	public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
		SpecialSkill skill = resultCombat.getSpecialAttack();
		String enemyName = "";
		int who = R.string.you_use;
		int color = R.color.positive;
		if (CharacterType.ENEMY.equals(resultCombat.getType())) {
			who = R.string.enemy_use;
			color = R.color.negative;
			enemyName = resultCombat.getEnemyName() + " ";
		}
		String text = enemyName;
		text += context.getString(who);
		text += " " + skill.getName().toLowerCase();
		if(skill.causeDamage()) {
			text += " " + context.getString(R.string.for_word);		
			text += " " + resultCombat.getDamage();
		}
		text += " " + context.getString(skill.getType().getText()).toLowerCase();
		return new ResultCombatText(color, text);
	}
	public SkillProperties getProperties() {
		return properties;
	}
	@Override
	public boolean isTriggerOnEndOfRound() {
		return properties.isOnEndOfRound();
	}
}
