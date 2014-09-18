package com.nex.gamebook.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;

import com.nex.gamebook.skills.active.Skill;
import com.nex.gamebook.skills.active.proprietary.CancelBuff;
import com.nex.gamebook.skills.active.proprietary.CancelDebuff;
import com.nex.gamebook.skills.active.proprietary.CancelDot;
import com.nex.gamebook.skills.active.proprietary.CancelHot;
import com.nex.gamebook.skills.active.proprietary.Kick;
import com.nex.gamebook.skills.active.proprietary.LifeLeech;
import com.nex.gamebook.skills.active.proprietary.QuickReaction;
import com.nex.gamebook.skills.active.proprietary.ReflectDamage;
import com.nex.gamebook.skills.active.proprietary.Stun;
import com.nex.gamebook.skills.active.proprietary.TwiceAttack;
import com.nex.gamebook.skills.passive.AreaOfDamage;
import com.nex.gamebook.skills.passive.AttackBuff;
import com.nex.gamebook.skills.passive.BoostPassiveSkill;
import com.nex.gamebook.skills.passive.CriticalSkills;
import com.nex.gamebook.skills.passive.DefenseBuff;
import com.nex.gamebook.skills.passive.DefenseIsAttack;
import com.nex.gamebook.skills.passive.DefenseIsHealth;
import com.nex.gamebook.skills.passive.DodgeIsSkill;
import com.nex.gamebook.skills.passive.HealthForAttack;
import com.nex.gamebook.skills.passive.HealthForSkillPower;
import com.nex.gamebook.skills.passive.HealthIncrease;
import com.nex.gamebook.skills.passive.Leech;
import com.nex.gamebook.skills.passive.LuckIsSkillpower;
import com.nex.gamebook.skills.passive.PassiveSkill;

public class SkillMap {
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
	
	private Map<String, Class<? extends Skill>> skills = new HashMap<>();
	private Map<String, Class<? extends PassiveSkill>> passiveSkills = new HashMap<>();

	private static SkillMap instance;
	static {
		instance = new SkillMap();
		instance.init();
		instance.initPassive();
	}
	
	private void initPassive() {
		passiveSkills.put(HealthIncrease.ID, HealthIncrease.class);
		passiveSkills.put(AreaOfDamage.ID, AreaOfDamage.class);
		passiveSkills.put(Leech.ID, Leech.class);
		passiveSkills.put(LuckIsSkillpower.ID, LuckIsSkillpower.class);
		passiveSkills.put(DodgeIsSkill.ID, DodgeIsSkill.class);
		passiveSkills.put(AttackBuff.ID, AttackBuff.class);
		passiveSkills.put(DefenseBuff.ID, DefenseBuff.class);
		passiveSkills.put(BoostPassiveSkill.ID, BoostPassiveSkill.class);
		passiveSkills.put(HealthForAttack.ID, HealthForAttack.class);
		passiveSkills.put(HealthForSkillPower.ID, HealthForSkillPower.class);
		passiveSkills.put(CriticalSkills.ID, CriticalSkills.class);
		passiveSkills.put(DefenseIsHealth.ID, DefenseIsHealth.class);
		passiveSkills.put(DefenseIsAttack.ID, DefenseIsAttack.class);
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

//	public static void main(String[] args) {
//		for (String key : getSkills().keySet()) {
//			String xsdEnumeration = "<xs:enumeration value=\"{0}\" />";
//			System.out.println(MessageFormat.format(xsdEnumeration, key));
//		}
//	}

	public static Map<String, Class<? extends Skill>> getSkills() {
		return instance.skills;
	}

	public static boolean isSpecialSkillEqualToName(Class<? extends Skill> skill, String name) {
		Class<? extends Skill> cls = instance.skills.get(name);
		if (cls != null) {
			return skill.equals(cls);
		}
		return false;
	}

	public static Skill get(String skillName) {
		try {
			Class<? extends Skill> skill = instance.skills.get(skillName);
			if (skill != null)
				return skill.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Log.e("", "SkillPull", e);
		}
		return null;
	}
	
	public static List<String> getUnlearnedSkills(Set<String> learnedSkills) {
		List<String> skills = new ArrayList<String>();
		for(String s: instance.passiveSkills.keySet()) {
			if(!learnedSkills.contains(s))
				skills.add(s);
		}
		return skills;
	}
	
	public static String getSkillId(Class<? extends Skill> cls) {
		for (Map.Entry<String, Class<? extends Skill>> entry : instance.skills.entrySet()) {
			if (cls.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static String getSkillNameKey(Class<? extends Skill> cls) {
		return "skill_" + getSkillId(cls);
	}

	public static List<Skill> getSkills(List<String> names) {
		List<Skill> s = new ArrayList<Skill>();
		for (String n : names) {
			s.add(get(n));
		}
		return s;
	}
	public static PassiveSkill getPassive(String skillName) {
		try {
			Class<? extends PassiveSkill> skill = instance.passiveSkills.get(skillName);
			if (skill != null)
				return skill.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Log.e("", "SkillPull", e);
		}
		return null;
	}
}
