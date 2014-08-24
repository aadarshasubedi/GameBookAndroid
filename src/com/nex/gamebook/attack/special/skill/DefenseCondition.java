package com.nex.gamebook.attack.special.skill;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.entity.Bonus.BonusType;

public class DefenseCondition extends SpecialConditionalSkill {
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
	public BonusType getType() {
		return BonusType.DEFENSE;
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

	@Override
	public boolean isTriggerBeforeEnemyAttack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTriggerAfterEnemyAttack() {
		// TODO Auto-generated method stub
		return false;
	}
}
