package com.nex.gamebook.attack.special.skill.overtime;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.skill.conditional.DecreaseAttribute;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.playground.BattleLogCallback;

public class DecreaseHealthOvertimeGreater extends DecreaseHealthOvertime {

	private StatType type;

	public DecreaseHealthOvertimeGreater(StatType type) {
		this.type = type;
	}

	@Override
	public String getDescription(Context ctx) {
		return ctx.getString(R.string.decrease_health_overtime_greater, ctx.getString(type.getText()));
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		int skillvalue = getValue(attacker);
		DecreaseAttribute skill = new DecreaseAttribute(type, skillvalue);
		skill.setData(properties, skillName);
		skill.doAttackOnce(attacker, attacked, callback, cm);
		return super.doAttackOnce(attacker, attacked, callback, cm);
	}

}
