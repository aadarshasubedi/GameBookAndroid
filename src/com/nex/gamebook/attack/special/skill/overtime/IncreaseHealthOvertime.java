package com.nex.gamebook.attack.special.skill.overtime;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialOvertimeSkill;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.skill.conditional.IncreaseAttribute;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;

public class IncreaseHealthOvertime extends SpecialOvertimeSkill {

	public IncreaseHealthOvertime() {
		super(NO_VALUE);

	}

	@Override
	public String getDescription(Context ctx, Character attacker) {
		return ctx.getString(R.string.increase_health_overtime);
	}

	public SpecialSkill getTargetSkill(com.nex.gamebook.game.Character character) {
		SpecialSkill skill = new IncreaseAttribute(StatType.HEALTH, getValue(character));
		skill.setData(properties, skillName);
		return skill;
	}

	@Override
	public boolean isCondition() {
		return false;
	}

}
