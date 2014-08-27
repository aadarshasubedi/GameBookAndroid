package com.nex.gamebook.entity;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.skill.Bleeding;
import com.nex.gamebook.attack.special.skill.CrushStrike;
import com.nex.gamebook.attack.special.skill.Disarm;
import com.nex.gamebook.attack.special.skill.GreaterHeal;
import com.nex.gamebook.attack.special.skill.Heal;
import com.nex.gamebook.attack.special.skill.LethalStrike;
import com.nex.gamebook.attack.special.skill.LifeLeech;
import com.nex.gamebook.attack.special.skill.QuickReaction;
import com.nex.gamebook.attack.special.skill.Rage;
import com.nex.gamebook.attack.special.skill.ReflectDamage;
import com.nex.gamebook.attack.special.skill.TwiceAttack;

public class SpecialSkillsMap {

//	private Map<String, SpecialSkill> playerAttacks = new HashMap<>();
	private Map<String, Class<? extends SpecialSkill>> skills = new HashMap<>();
	private static SpecialSkillsMap instance;
	static {
		instance = new SpecialSkillsMap();
		instance.init();
	}

	private void init() {
		skills.put("twiceAttack", TwiceAttack.class);
		skills.put("crushStrike", CrushStrike.class);
		skills.put("disarm", Disarm.class);
		skills.put("rage", Rage.class);
		skills.put("bleeding", Bleeding.class);
		skills.put("reflectDamage", ReflectDamage.class);
		skills.put("lethalStrike", LethalStrike.class);
		skills.put("lifeLeech", LifeLeech.class);
		skills.put("quickReaction", QuickReaction.class);
		skills.put("heal", Heal.class);
		skills.put("greaterHeal", GreaterHeal.class);
	}

	public static Map<String, Class<? extends SpecialSkill>> getSkills() {
		return instance.skills;
	}

	public static boolean isSpecialSkillEqualToName(Class<? extends SpecialSkill> skill, String name) {
		Class<? extends SpecialSkill> cls = instance.skills.get(name);
		if(cls!=null) {
			return skill.equals(cls);
		}
		return false;
	}
	
	public static SpecialSkill get(String skillName) {
		try {
			Class<? extends SpecialSkill> skill = instance.skills.get(skillName);
			if(skill!=null)
			return skill.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Log.e("", "SkillPull", e);
		}
		return null;
	}
	public static String getSkillId(Class<? extends SpecialSkill> cls) {
		for(Map.Entry<String, Class<? extends SpecialSkill>> entry: instance.skills.entrySet()){
			if(cls.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
}
