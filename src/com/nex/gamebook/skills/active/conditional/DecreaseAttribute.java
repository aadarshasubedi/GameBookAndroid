package com.nex.gamebook.skills.active.conditional;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.skills.active.ActiveConditionalSkill;

public class DecreaseAttribute extends ActiveConditionalSkill {

	private StatType type;

	public DecreaseAttribute(StatType type, int constantValue) {
		super(constantValue);
		this.type = type;
	}

	public DecreaseAttribute(StatType type) {
		super(NO_VALUE);
		this.type = type;
	}

	@Override
	public String getDescription(Context ctx, Character attacker) {
		if(type.equals(StatType.HEALTH))
			return ctx.getString(R.string.damage_attribute);
		return ctx.getString(R.string.decrease_attribute, ctx.getString(type.getText()).toString());
	}

	@Override
	public StatType getType() {
		return type;
	}

	@Override
	public boolean isCondition() {
		return true;
	}

}
