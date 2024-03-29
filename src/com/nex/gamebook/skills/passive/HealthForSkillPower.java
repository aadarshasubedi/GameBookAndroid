package com.nex.gamebook.skills.passive;

import java.util.Properties;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;

public class HealthForSkillPower extends PassiveSkill {
	public static String ID = "passiveHealthForSkillpower";
	
	@Override
	public String getName(Properties p) {
		return p.getProperty(ID);
	}

	@Override
	public String getDescription(Context ctx, Character c) {
		return ctx.getString(R.string.passive_health_for_skillpower, powerDown(c), "%", power(c),  "%");
	}

	@Override
	public int power(Character c) {
		return boostPower(c, 30);
	}
	public int powerDown(Character c) {
		return 30;
	}
	
	@Override
	public void processWhenLearned(Character c) {
		int currentHealth = c.getCurrentStats().getPureHealth();
		int real = currentHealth - c.getCurrentStats().getValuePerc(currentHealth, powerDown(c));
		c.getCurrentStats().setHealth(real);
	}
	
}
