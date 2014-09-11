package com.nex.gamebook.skills.passive;

import java.util.Properties;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.SkillMap;

public class HealthIncrease extends PassiveSkill {

	
	@Override
	public String getName(Properties p) {
		return p.getProperty(SkillMap.PASSIVE_HEALTH_INCREASE);
	}
	
	public String getDescription(Context ctx) {
		return ctx.getString(R.string.passive_health_increase, "30", "%");
	}
	
}
