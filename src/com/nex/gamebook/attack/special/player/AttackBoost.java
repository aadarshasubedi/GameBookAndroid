package com.nex.gamebook.attack.special.player;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.AbstractPlayerSpecialConditionalSkill;
import com.nex.gamebook.entity.Bonus.BonusType;
import com.nex.gamebook.entity.Character;

public class AttackBoost extends AbstractPlayerSpecialConditionalSkill {


	private static final long serialVersionUID = -1804300495308316474L;
	@Override
	public int getTextId() {
		return R.string.attr_attack;
	}

	@Override
	public int getDescriptionId() {
		return R.string.attack_boost_description;
	}

	@Override
	public int getNameId() {
		return R.string.attack_boost_name;
	}

	@Override
	public int getValue(Character character) {
		int value = 1;
		if(character.hasLuck()) {
			value = 2;
		}
		return value;
	}

	@Override
	public BonusType getType() {
		return BonusType.ATTACK;
	}

	@Override
	public boolean isCondition() {
		return false;
	}

	@Override
	public int attemptsPerFight() {
		return 1;
	}
	@Override
	public boolean isPermanent() {
		return false;
	}
	@Override
	public int getTypeId() {
		return R.string.special_skill_buff_temp;
	}
	@Override
	public boolean isTriggerEnemy() {
		return false;
	}
}
