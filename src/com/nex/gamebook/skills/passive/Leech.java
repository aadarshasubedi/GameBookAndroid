package com.nex.gamebook.skills.passive;

import java.util.Properties;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;

public class Leech extends PassiveSkill {
	public static String ID = "passiveLeech";
	@Override
	public String getName(Properties p) {
		return p.getProperty(ID);
	}
	
	public String getDescription(Context ctx, Character c) {
		return ctx.getString(R.string.passive_leech, power(c), "%");
	}

	@Override
	public int power(Character c) {
		return boostPower(c, 4);
	}

}
