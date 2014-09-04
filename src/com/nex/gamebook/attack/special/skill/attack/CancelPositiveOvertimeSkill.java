package com.nex.gamebook.attack.special.skill.attack;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialCancelationSkill;

public class CancelPositiveOvertimeSkill extends SpecialCancelationSkill {

	
	@Override
	public String getDescription(Context ctx) {
		return ctx.getString(R.string.cancel_positive_description);
	}
	@Override
	public boolean isCancelPositive() {
		return true;
	}

	@Override
	public boolean isCondition() {
		return false;
	}

	
}
