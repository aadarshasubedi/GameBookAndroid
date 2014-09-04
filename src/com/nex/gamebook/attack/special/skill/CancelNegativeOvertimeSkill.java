package com.nex.gamebook.attack.special.skill;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialCancelationSkill;

public class CancelNegativeOvertimeSkill extends SpecialCancelationSkill {

	@Override
	public int getDescriptionId() {
		return R.string.cancel_negative_description;
	}

	@Override
	public boolean isCancelPositive() {
		return false;
	}

}
