package com.nex.gamebook.attack.special;

import java.util.List;

import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.Enemy;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.SkillBased;
import com.nex.gamebook.game.Story;
import com.nex.gamebook.playground.BattleLogCallback;

public abstract class SpecialAttackSkill implements SpecialSkill {

	public static int NO_VALUE = -1;
	protected int cycles = 0;
	protected int constantValue;
	protected SkillProperties properties = new SkillProperties();
	protected String skillName;

	public SpecialAttackSkill(int constantValue) {
		super();
		this.constantValue = constantValue;
	}

	// protected boolean used = false;

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

	@Override
	public int getValue(Character character) {
		if (constantValue != NO_VALUE)
			return constantValue;
		if (character.getSkillBased().equals(SkillBased.SKILL_POWER)) {
			return getValueBasedOnSkillPower(character);
		} else if (character.getSkillBased().equals(SkillBased.ATTACK)) {
			return getValueBasedOnAttack(character);
		} else if (character.getSkillBased().equals(SkillBased.SKILL)) {
			return getValueBasedOnSkill(character);
		}
		throw new IllegalArgumentException("Wrong skill based type");
	}

	public int getValueBasedOnAttack(Character c) {
		return (int) (c.getCurrentStats().getAttack() + (c.getCurrentStats().getAttack() * properties.getCoeff()));
	}

	public int getValueBasedOnSkillPower(Character c) {
		return (int) (c.getCurrentStats().getSkillpower() + (c.getCurrentStats().getSkillpower() * properties.getCoeff()));
	}

	public int getValueBasedOnSkill(Character c) {
		return (int) (c.getCurrentStats().getSkill() + (c.getCurrentStats().getSkill() * properties.getCoeff()));
	}

	public String getName() {
		return skillName;
	}

	public void setData(SkillProperties properties, String translatedSkillName) {
		this.properties = properties;
		this.skillName = translatedSkillName;
	}
}
