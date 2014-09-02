package com.nex.gamebook.attack.special.skill;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.game.Bonus.StatType;

public class Rage extends SpecialConditionalSkill {


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
	public StatType getType() {
		return StatType.ATTACK;
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
		return R.string.special_skill_type_buff;
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
	
	@Override
	public boolean inFight() {
		return false;
	}
	
}
