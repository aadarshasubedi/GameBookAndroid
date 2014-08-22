package com.nex.gamebook.attack.special.player;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.AbstractPlayerSpecialConditionalSkill;
import com.nex.gamebook.entity.Bonus.BonusType;
import com.nex.gamebook.entity.Character;

public class DefenseCondition extends AbstractPlayerSpecialConditionalSkill {
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
	public int getValue(Character character) {
		int value = character.getCurrentStats().getSkill();
		if (character.hasLuck()) {
			value = value * 2;
		}
		return value;
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
		return 1;
	}
	@Override
	public boolean isPermanent() {
		return true;
	}
	@Override
	public int getTypeId() {
		return R.string.special_skill_debuf_perm;
	}
	@Override
	public boolean isTriggerEnemy() {
		return false;
	}
}
