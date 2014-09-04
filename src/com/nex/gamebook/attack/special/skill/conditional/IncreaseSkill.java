package com.nex.gamebook.attack.special.skill.conditional;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.game.Bonus.StatType;

public class IncreaseSkill extends SpecialConditionalSkill {
	
	@Override
	public int getDescriptionId() {
		return R.string.increase_skill;
	}

	@Override
	public StatType getType() {
		return StatType.SKILL;
	}

	@Override
	public boolean isCondition() {
		return false;
	}

	@Override
	public int attemptsPerFight() {
		return 2;
	}

	@Override
	public boolean inFight() {
		return false;
	}
	
	@Override
	public boolean isPermanent() {
		return false;
	}
}