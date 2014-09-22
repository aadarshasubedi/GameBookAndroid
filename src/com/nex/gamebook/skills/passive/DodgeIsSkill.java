package com.nex.gamebook.skills.passive;

import java.util.Properties;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;

public class DodgeIsSkill extends PassiveConditionalSkill {
	public static String ID = "passiveDodgeIsSkill";
	@Override
	public String getName(Properties p) {
		return p.getProperty(ID);
	}
	
	public String getDescription(Context ctx, Character c) {
		return ctx.getString(R.string.passive_dodge_is_skill, c.getCurrentStats().getSpecialSkillPower(), getTurns(c));
	}

	@Override
	public int power(Character c) {
		return boostPower(c, c.getCurrentStats().getSpecialSkillPower());
	}
	public int getTurns(Character ch) {
		return 4;
	}
}
