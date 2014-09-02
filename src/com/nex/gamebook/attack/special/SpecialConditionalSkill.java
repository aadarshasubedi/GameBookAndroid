package com.nex.gamebook.attack.special;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.playground.BattleLogCallback;

public abstract class SpecialConditionalSkill extends SpecialAttackSkill {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked,
			BattleLogCallback callback, ResultCombat cm) {
		Character applicationChar = resolveCharacterForApplication(attacked,
				attacker);
		Bonus bonus = createSpecialAttack(isCondition() ? -1 : 1,
				getRealValue(attacker), getType());
		int value = applicationChar.getCurrentStats().getValueByBonusType(
				getType());
		int res = value - bonus.getValue();
		if (res <= getMinAttributeForStopAttack()) {
			int bonusValue = value - getMinAttributeForStopAttack();
			if (bonusValue < 0)
				bonusValue = 0;
			bonus.setValue(bonusValue);
		}
		applicationChar.addBonus(bonus);
		if (!isPermanent()) {
			addTemporalBonus(applicationChar, bonus,
					createConditionId(attacker));
		}
		ResultCombat result = createBasicResult(bonus.getValue(),
				attacker.getType());
		result.setEnemyName(resolveEnemy(attacker, attacked).getName());
		callback.logAttack(result);
		return true;
	}

	public abstract StatType getType();

	public abstract boolean isCondition();

	public int getMinAttributeForStopAttack() {
		return -1;
	}

	public Character resolveCharacterForApplication(Character attacked,
			Character attacker) {
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
		return R.string.special_skill_aspect_power;
	}

	@Override
	public boolean showPercentage() {
		return false;
	}

	@Override
	public boolean afterNormalAttack() {
		return false;
	}
}
