package com.nex.gamebook.attack.special.skill;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.entity.Bonus.BonusType;

public class BleedingSkill extends SpecialConditionalSkill {
	
	private static final long serialVersionUID = 2040871476810819647L;

	@Override
	public int getMinAttributeForStopAttack() {
		return 5;
	}
	
	@Override
	public int getTextId() {
		return R.string.attr_health;
	}

	@Override
	public int getDescriptionId() {
		return R.string.health_condition_description;
	}

	@Override
	public int getNameId() {
		return R.string.health_condition_name;
	}

	@Override
	public BonusType getType() {
		return BonusType.HEALTH;
	}

	@Override
	public boolean isCondition() {
		return true;
	}

	@Override
	public int attemptsPerFight() {
		return -1;
	}
	
	@Override
	public boolean isPermanent() {
		return true;
	}
	
	@Override
	public int getTypeId() {
		return R.string.special_skill_type_debuff;
	}

	@Override
	public boolean isTriggerBeforeEnemyAttack() {
		return false;
	}

	@Override
	public boolean isTriggerAfterEnemyAttack() {
		return false;
	}
}
