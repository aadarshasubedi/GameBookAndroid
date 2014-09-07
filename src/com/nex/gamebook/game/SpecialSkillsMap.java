package com.nex.gamebook.game;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.skill.attack.CancelBuff;
import com.nex.gamebook.attack.special.skill.attack.CancelDebuff;
import com.nex.gamebook.attack.special.skill.attack.CancelDot;
import com.nex.gamebook.attack.special.skill.attack.CancelHot;
import com.nex.gamebook.attack.special.skill.attack.Kick;
import com.nex.gamebook.attack.special.skill.attack.LifeLeech;
import com.nex.gamebook.attack.special.skill.attack.QuickReaction;
import com.nex.gamebook.attack.special.skill.attack.ReflectDamage;
import com.nex.gamebook.attack.special.skill.attack.Stun;
import com.nex.gamebook.attack.special.skill.attack.TwiceAttack;

public class SpecialSkillsMap {
	public static String NO_SKILL = "no_skill";

	public static String TWICE_ATTACK = "twice_attack";
	public static String REFLECT_DAMAGE = "reflect_damage";
	public static String LIFE_LEECH = "life_leech";
	public static String QUICK_REACTION = "quick_reaction";
	public static String CANCEL_HOT = "cancel_hot";
	public static String CANCEL_DOT = "cancel_dot";
	public static String CANCEL_BUFF = "cancel_buff";
	public static String CANCEL_DEBUFF = "cancel_debuff";
	public static String STUN = "stun";
	public static String KICK = "kick";
	private Map<String, Class<? extends SpecialSkill>> skills = new HashMap<>();
	private static SpecialSkillsMap instance;
	static {
		instance = new SpecialSkillsMap();
		instance.init();
	}

	private void init() {
		skills.put(TWICE_ATTACK, TwiceAttack.class);
		skills.put(REFLECT_DAMAGE, ReflectDamage.class);
		skills.put(LIFE_LEECH, LifeLeech.class);
		skills.put(QUICK_REACTION, QuickReaction.class);
		skills.put(CANCEL_HOT, CancelHot.class);
		skills.put(CANCEL_DOT, CancelDot.class);
		skills.put(CANCEL_BUFF, CancelBuff.class);
		skills.put(CANCEL_DEBUFF, CancelDebuff.class);
		skills.put(STUN, Stun.class);
		skills.put(KICK, Kick.class);
	}
	
	public static void main(String[] args) {
		for(String key: getSkills().keySet()) {
			String xsdEnumeration = "<xs:enumeration value=\"{0}\" />";
			System.out.println(MessageFormat.format(xsdEnumeration, key));
		}
	}
	
	public static Map<String, Class<? extends SpecialSkill>> getSkills() {
		return instance.skills;
	}

	public static boolean isSpecialSkillEqualToName(Class<? extends SpecialSkill> skill, String name) {
		Class<? extends SpecialSkill> cls = instance.skills.get(name);
		if (cls != null) {
			return skill.equals(cls);
		}
		return false;
	}

	public static SpecialSkill get(String skillName) {
		try {
			Class<? extends SpecialSkill> skill = instance.skills.get(skillName);
			if (skill != null)
				return skill.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Log.e("", "SkillPull", e);
		}
		return null;
	}

	public static String getSkillId(Class<? extends SpecialSkill> cls) {
		for (Map.Entry<String, Class<? extends SpecialSkill>> entry : instance.skills.entrySet()) {
			if (cls.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static String getSkillNameKey(Class<? extends SpecialSkill> cls) {
		return "skill_" + getSkillId(cls);
	}

	public static List<SpecialSkill> getSkills(List<String> names) {
		List<SpecialSkill> s = new ArrayList<SpecialSkill>();
		for (String n : names) {
			s.add(get(n));
		}
		return s;
	}
}
