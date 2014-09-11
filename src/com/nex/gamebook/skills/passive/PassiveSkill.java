package com.nex.gamebook.skills.passive;

import java.util.Properties;

import android.content.Context;

public abstract class PassiveSkill {
	
	public abstract String getName(Properties p);
	public abstract String getDescription(Context ctx);
}
