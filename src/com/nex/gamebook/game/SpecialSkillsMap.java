package com.nex.gamebook.game;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.skill.CancelNegativeOvertimeSkill;
import com.nex.gamebook.attack.special.skill.CancelPositiveOvertimeSkill;
import com.nex.gamebook.attack.special.skill.attack.LifeLeech;
import com.nex.gamebook.attack.special.skill.attack.QuickReaction;
import com.nex.gamebook.attack.special.skill.attack.ReflectDamage;
import com.nex.gamebook.attack.special.skill.attack.TwiceAttack;
import com.nex.gamebook.attack.special.skill.conditional.DecreaseDefense;
import com.nex.gamebook.attack.special.skill.conditional.DecreaseAttack;
import com.nex.gamebook.attack.special.skill.conditional.DecreaseHealth;
import com.nex.gamebook.attack.special.skill.conditional.DecreaseHealthGreater;
import com.nex.gamebook.attack.special.skill.conditional.DecreaseSkill;
import com.nex.gamebook.attack.special.skill.conditional.IncreaseAttack;
import com.nex.gamebook.attack.special.skill.conditional.IncreaseDefense;
import com.nex.gamebook.attack.special.skill.conditional.IncreaseHealthGreater;
import com.nex.gamebook.attack.special.skill.conditional.IncreaseHealth;
import com.nex.gamebook.attack.special.skill.conditional.IncreaseSkill;
import com.nex.gamebook.attack.special.skill.overtime.DecreaseHealthOvertime;
import com.nex.gamebook.attack.special.skill.overtime.DecreaseHealthOvertimeGreater;
import com.nex.gamebook.attack.special.skill.overtime.IncreaseHealthOvertime;
import com.nex.gamebook.attack.special.skill.overtime.IncreaseHealthOvertimeGreater;

public class SpecialSkillsMap {
	public static String NO_SKILL = "no_skill";

	public static String DECREASE_DEFENSE = "decrease_defense";
	public static String INCREASE_DEFENSE = "increase_defense";

	public static String DECREASE_ATTACK = "decrease_attack";
	public static String INCREASE_ATTACK = "increase_attack";

	public static String DECREASE_SKILL = "decrease_skill";
	public static String INCREASE_SKILL = "increase_skill";

	public static String DECREASE_SKILLPOWER = "decrease_skillpower";
	public static String INCREASE_SKILLPOWER = "increase_skillpower";

	public static String DECREASE_HEALTH = "decrease_health";
	public static String INCREASE_HEALTH = "increase_health";

	public static String DECREASE_HEALTH_GREATER = "decrease_health_greater";
	public static String INCREASE_HEALTH_GREATER = "increase_health_greater";

	public static String DECREASE_HEALTH_OVERTIME = "decrease_health_overtime";
	public static String INCREASE_HEALTH_OVERTIME = "increase_health_overtime";

	public static String DECREASE_HEALTH_GREATER_OVERTIME = "decrease_health_greater_overtime";
	public static String INCREASE_HEALTH_GREATER_OVERTIME = "increase_health_greater_overtime";

	public static String TWICE_ATTACK = "twice_attack";
	public static String REFLECT_DAMAGE = "reflect_damage";
	public static String LIFE_LEECH = "life_leech";
	public static String QUICK_REACTION = "quick_reaction";
	public static String HEAL = "heal";
	public static String GREATER_HEAL = "greater_heal";
	public static String RENEW = "renew";
	public static String CANCEL_POSITIVE = "cancel_positive";
	public static String CANCEL_NEGATIVE = "cancel_negative";
	
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
		skills.put(CANCEL_POSITIVE, CancelPositiveOvertimeSkill.class);
		skills.put(CANCEL_NEGATIVE, CancelNegativeOvertimeSkill.class);

		skills.put(DECREASE_DEFENSE, DecreaseDefense.class);
		skills.put(INCREASE_DEFENSE, IncreaseDefense.class);

		skills.put(DECREASE_ATTACK, DecreaseAttack.class);
		skills.put(INCREASE_ATTACK, IncreaseAttack.class);

		skills.put(DECREASE_SKILL, DecreaseSkill.class);
		skills.put(INCREASE_SKILL, IncreaseSkill.class);

		skills.put(DECREASE_SKILL, DecreaseSkill.class);
		skills.put(INCREASE_SKILL, IncreaseSkill.class);

		skills.put(DECREASE_SKILLPOWER, DecreaseSkill.class);
		skills.put(INCREASE_SKILLPOWER, IncreaseSkill.class);
		
		skills.put(DECREASE_HEALTH, DecreaseHealth.class);
		skills.put(INCREASE_HEALTH, IncreaseHealth.class);

		skills.put(DECREASE_HEALTH_OVERTIME, DecreaseHealthOvertime.class);
		skills.put(INCREASE_HEALTH_OVERTIME, IncreaseHealthOvertime.class);

		skills.put(DECREASE_HEALTH_GREATER, DecreaseHealthGreater.class);
		skills.put(INCREASE_HEALTH_GREATER, IncreaseHealthGreater.class);

		skills.put(DECREASE_HEALTH_GREATER_OVERTIME, DecreaseHealthOvertimeGreater.class);
		skills.put(INCREASE_HEALTH_GREATER_OVERTIME, IncreaseHealthOvertimeGreater.class);

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
