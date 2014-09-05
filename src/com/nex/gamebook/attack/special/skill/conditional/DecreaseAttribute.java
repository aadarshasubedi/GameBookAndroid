package com.nex.gamebook.attack.special.skill.conditional;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;

public class DecreaseAttribute extends SpecialConditionalSkill {

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
		return ctx.getString(R.string.decrease_attribute, ctx.getString(type.getText()));
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
