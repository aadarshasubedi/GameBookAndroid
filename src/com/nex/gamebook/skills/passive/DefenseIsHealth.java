package com.nex.gamebook.skills.passive;

import java.util.Properties;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;

public class DefenseIsHealth extends PassiveSkill {
	public static String ID = "passiveHealthIsDefense";
	
	@Override
	public String getName(Properties p) {
		return p.getProperty(ID);
	}
	
	public String getDescription(Context ctx, Character c) {
		return ctx.getString(R.string.passive_health_is_defense, power(c), "%");
	}
	@Override
	public int power(Character c) {
		return boostPower(c, c.getCurrentStats().getDefensePercentage());
	}
	
	@Override
	public void processWhenLearned(Character c) {
		int currentHealth = c.getCurrentStats().getPureHealth();
		int real = currentHealth + c.getCurrentStats().getValuePerc(currentHealth, power(c));
		c.getCurrentStats().setHealth(real);
	}
	
}
