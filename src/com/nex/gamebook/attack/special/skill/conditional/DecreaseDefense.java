package com.nex.gamebook.attack.special.skill.conditional;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.game.Bonus.StatType;

public class DecreaseDefense extends SpecialConditionalSkill {
	@Override
	public int getDescriptionId() {
		return R.string.decrease_defense;
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

}
