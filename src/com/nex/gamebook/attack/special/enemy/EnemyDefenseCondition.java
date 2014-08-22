package com.nex.gamebook.attack.special.enemy;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.AbstractEnemySpecialConditionalSkill;
import com.nex.gamebook.entity.Bonus.BonusType;
import com.nex.gamebook.entity.Character;

public class EnemyDefenseCondition extends AbstractEnemySpecialConditionalSkill {
	
	private static final long serialVersionUID = -7556224754917123550L;

	@Override
	public int getMinAttributeForStopAttack() {
		return -1;
	}

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
		int value = 1;
		if(character.hasLuck()) {
			value = 2;
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
		return false;
	}
	@Override
	public int getTypeId() {
		return R.string.special_skill_debuf_temp;
	}
	@Override
	public boolean isTriggerEnemy() {
		return false;
	}
}
