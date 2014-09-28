package com.nex.gamebook.skills.passive;

import java.util.Properties;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;

public class EnragedSummon extends PassiveSkill {
	public static String ID = "enragedSummon";
	@Override
	public String getName(Properties p) {
		return p.getProperty(ID);
	}
	
	public String getDescription(Context ctx, Character c) {
		return ctx.getString(R.string.enraged_summon, power(c), "%", String.valueOf(getCriticalPower(c)));
	}

	public float getCriticalPower(Character c) {
		return boostPower(c, 1.5f);
	}
	
	@Override
	public int power(Character c) {
		return boostPower(c, 20);
	}

}
