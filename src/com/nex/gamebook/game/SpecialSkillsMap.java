package com.nex.gamebook.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	public static String NO_SKILL = "no_skill";
	
	public static String TWICE_ATTACK = "twiceAttack";
	public static String CRUSH_STRIKE="crushStrike";
	public static String DISARM="disarm";
	public static String RAGE="rage";
	public static String BLEEDING="bleeding";
	public static String REFLECT_DAMAGE="reflectDamage";
	public static String LETHAL_STRIKE="lethalStrike";
	public static String LIFE_LEECH="lifeLeech";
	public static String QUICK_REACTION="quickReaction";
	public static String HEAL="heal";
	public static String GREATER_HEAL="greaterHeal";
	
//	private Map<String, SpecialSkill> playerAttacks = new HashMap<>();
	private Map<String, Class<? extends SpecialSkill>> skills = new HashMap<>();
	private static SpecialSkillsMap instance;
	static {
		instance = new SpecialSkillsMap();
		instance.init();
	}

	private void init() {
		skills.put(TWICE_ATTACK, TwiceAttack.class);
		skills.put(CRUSH_STRIKE, CrushStrike.class);
		skills.put(DISARM, Disarm.class);
		skills.put(RAGE, Rage.class);
		skills.put(BLEEDING, Bleeding.class);
		skills.put(REFLECT_DAMAGE, ReflectDamage.class);
		skills.put(LETHAL_STRIKE, LethalStrike.class);
		skills.put(LIFE_LEECH, LifeLeech.class);
		skills.put(QUICK_REACTION, QuickReaction.class);
		skills.put(HEAL, Heal.class);
		skills.put(GREATER_HEAL, GreaterHeal.class);
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

	public static List<SpecialSkill> getSkills(List<String> names) {
		List<SpecialSkill> s = new ArrayList<SpecialSkill>();
		for(String n:names) {
			s.add(get(n));
		}
		return s;
	}
}
