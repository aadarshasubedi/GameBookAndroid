package com.nex.gamebook.attack.special.skill.attack;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialCancelationSkill;

public class CancelNegativeOvertimeSkill extends SpecialCancelationSkill {

	@Override
	public String getDescription(Context ctx) {
		return ctx.getString(R.string.cancel_negative_description);
	}

	@Override
	public boolean isCancelPositive() {
		return false;
	}

	@Override
	public boolean isCondition() {
		return false;
	}
	
}
