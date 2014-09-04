package com.nex.gamebook.attack.special.skill.overtime;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.skill.conditional.IncreaseAttribute;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public class IncreaseHealthOvertimeGreater extends IncreaseHealthOvertime {

	private StatType type;

	public IncreaseHealthOvertimeGreater(StatType type) {
		this.type = type;
	}

	@Override
	public String getDescription(Context ctx) {
		return ctx.getString(R.string.increase_health_overtime_greater, ctx.getString(type.getText()).toLowerCase());
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		int skillvalue = getValue(attacker);
		IncreaseAttribute skill = new IncreaseAttribute(type, skillvalue);
		skill.setData(properties, skillName);
		skill.doAttackOnce(attacker, attacked, callback, cm);
		return super.doAttackOnce(attacker, attacked, callback, cm);
	}

}
