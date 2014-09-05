package com.nex.gamebook.attack.special.skill.attack;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialCancelationSkill;

public class CancelNegativeOvertimeSkill extends SpecialCancelationSkill {

	@Override
	public boolean isCancelPositive() {
		return false;
	}

	@Override
	public boolean isCondition() {
		return false;
	}

	@Override
	public String getDescription(Context ctx, com.nex.gamebook.game.Character attacker) {
		return ctx.getString(R.string.cancel_negative_description, getSumOfCanceledSkills(attacker));
	}
	
}
