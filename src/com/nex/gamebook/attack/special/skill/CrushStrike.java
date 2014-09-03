package com.nex.gamebook.attack.special.skill;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.game.Bonus.StatType;
public class CrushStrike extends SpecialConditionalSkill {
	private static final long serialVersionUID = -1804300495308316474L;

	@Override
	public int getTextId() {
		return R.string.attr_defense;
	}

	@Override
	public int getDescriptionId() {
		return R.string.defense_condition_description;
	}

	@Override
	public int getNameId() {
		return R.string.defense_condition_name;
	}

	@Override
	public StatType getType() {
		return StatType.DEFENSE;
	}

	@Override
	public boolean isCondition() {
		return true;
	}

	@Override
	public int attemptsPerFight() {
		return 2;
	}
	@Override
	public boolean isPermanent() {
		return false;
	}
	@Override
	public int getTypeId() {
		return R.string.special_skill_type_debuff;
	}

}
