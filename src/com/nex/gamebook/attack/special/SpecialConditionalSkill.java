package com.nex.gamebook.attack.special;

import java.util.ArrayList;
import java.util.List;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.SpecialSkillsMap;
import com.nex.gamebook.game.Stats;
import com.nex.gamebook.playground.BattleLogCallback;

public abstract class SpecialConditionalSkill extends SpecialAttackSkill {

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		Character applicationChar = resolveCharacterForApplication(attacked, attacker);
		Bonus bonus = createSpecialAttack(isCondition() ? -1 : 1, getRealValue(attacker), getType());
		int value = applicationChar.getCurrentStats().getValueByBonusType(getType());
		int res = value - bonus.getValue();
		if (isCondition() && res <= getMinAttributeForStopAttack()) {
			int bonusValue = value - getMinAttributeForStopAttack();
			if (bonusValue < 0)
				bonusValue = 0;
			bonus.setValue(bonusValue);
		}
		int realValue = applicationChar.addBonus(bonus);
		if(realValue<0)
			realValue *= -1;
		bonus.setValue(realValue);
		if (!isPermanent()) {
			addTemporalBonus(applicationChar, bonus, createConditionId(attacker));
		}
		ResultCombat result = createBasicResult(bonus.getValue(), attacker.getType(), resolveEnemy(attacker, attacked));
		callback.logAttack(result);
		return true;
	}

	// public abstract StatType getType();

	public abstract boolean isCondition();

	@Override
	public boolean isDebuff() {
		return isCondition();
	}

	public int getMinAttributeForStopAttack() {
		return -1;
	}

	public Character resolveCharacterForApplication(Character attacked, Character attacker) {
		if (isCondition()) {
			return attacked;
		}
		return attacker;
	}

	public int getValue(Character c) {
		return c.getCurrentStats().getSpecialSkillPower();
	}

	@Override
	public int getValueWhenLuck(Character character) {
		return getValue(character) * 2;
	}

	@Override
	public int getAspectId() {
		return ASPECT_POWER;
	}

	@Override
	public boolean showPercentage() {
		return false;
	}

	@Override
	public boolean afterNormalAttack() {
		return false;
	}

	@Override
	public boolean doSomething(Character attacked, Character attacker) {
		if(isDebuff()) {
			Player test = new Player();
			test.setCurrentStats(new Stats(attacked.getCurrentStats()));
			test.setStats(new Stats(attacked.getCurrentStats()));
			Bonus bonus = createSpecialAttack(isCondition() ? -1 : 1, getRealValue(attacker), getType());
			int realValue = test.addBonus(bonus);
			if(realValue==0) return false;
		}
		return true;
	}
	
	@Override
	public List<String> getBestInterceptSkills() {
		List<String> s = new ArrayList<String>();
		s.add(SpecialSkillsMap.INCREASE_SKILLPOWER);
		return s;
	}
	@Override
	public List<String> getBestAgainstSkill() {
		List<String> s = new ArrayList<String>();
		s.add(SpecialSkillsMap.DECREASE_SKILLPOWER);
		return s;
	}
}
