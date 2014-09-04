package com.nex.gamebook.attack.special.skill.overtime;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialOvertimeSkill;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.skill.conditional.DecreaseAttribute;
import com.nex.gamebook.game.Bonus.StatType;

public class DecreaseHealthOvertime extends SpecialOvertimeSkill {

	public DecreaseHealthOvertime() {
		super(NO_VALUE);

	}

	@Override
	public String getDescription(Context ctx) {
		return ctx.getString(R.string.decrease_health_overtime);
	}

	public SpecialSkill getTargetSkill(com.nex.gamebook.game.Character character) {
		SpecialSkill skill = new DecreaseAttribute(StatType.HEALTH, getValue(character));
		skill.setData(properties, skillName);
		return skill;
	}

	@Override
	public boolean isCondition() {
		return true;
	}

}
