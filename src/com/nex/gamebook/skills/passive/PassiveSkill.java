package com.nex.gamebook.skills.passive;

import java.util.Properties;

import com.nex.gamebook.game.Character;

import android.content.Context;

public abstract class PassiveSkill {
	
	public abstract String getName(Properties p);
	public abstract String getDescription(Context ctx, Character c);
	public abstract int power(Character c);
	
	public int boostPower(Character c, int defaultPower) {
		return (int) boostPower(c, Float.valueOf(defaultPower));
	}
	
	public float boostPower(Character c, float defaultPower) {
		BoostPassiveSkill skill = (BoostPassiveSkill) c.findPassiveSkill(BoostPassiveSkill.class);
		if(skill!=null){
			defaultPower += ((double)defaultPower/100d) * skill.power(c);
		}
		return defaultPower;
	}
	
	public void processWhenLearned(Character c) {
		
	}
	
}
