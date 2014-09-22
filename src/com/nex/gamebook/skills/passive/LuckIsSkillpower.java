package com.nex.gamebook.skills.passive;

import java.util.Properties;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;

public class LuckIsSkillpower extends PassiveConditionalSkill {
	public static String ID = "passiveLuckIsSkillpower";
	@Override
	public String getName(Properties p) {
		return p.getProperty(ID);
	}
	
	public String getDescription(Context ctx, Character c) {
		return ctx.getString(R.string.passive_luck_is_skillpower,c.getCurrentStats().getLuckPercentage(), "%", power(c), getTurns(c));
	}

	@Override
	public int power(Character c) {
		return boostPower(c, c.getCurrentStats().getSpecialSkillPower());
	}
	
	public int getTurns(Character ch) {
		return 2;
	}
	
}
