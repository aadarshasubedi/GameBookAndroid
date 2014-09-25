package com.nex.gamebook.skills.active.overtime;

import android.annotation.SuppressLint;
import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.skills.ResultCombatText;
import com.nex.gamebook.skills.active.ActiveOvertimeSkill;
import com.nex.gamebook.skills.active.Skill;
import com.nex.gamebook.skills.active.conditional.IncreaseAttribute;

public class IncreaseHealthOvertime extends ActiveOvertimeSkill {

	public IncreaseHealthOvertime() {
		super(NO_VALUE);

	}

	@Override
	public String getDescription(Context ctx, Character attacker) {
		return ctx.getString(R.string.increase_health_overtime);
	}

	public Skill getTargetSkill(com.nex.gamebook.game.Character character) {
		Skill skill = new IncreaseAttribute(StatType.HEALTH, getValue(character));
		skill.setData(properties, skillName);
		return skill;
	}

	@Override
	public boolean isCondition() {
		return false;
	}
	@SuppressLint("ResourceAsColor")
	@Override
	public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
		ResultCombatText text = super.getLogAttackOvertime(context, resultCombat);
		text.setColor(R.color.reset);
		return text;
	}
}
