package com.nex.gamebook.entity;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.skill.AttackBoost;
import com.nex.gamebook.attack.special.skill.AttackCondition;
import com.nex.gamebook.attack.special.skill.BleedingSkill;
import com.nex.gamebook.attack.special.skill.DefenseCondition;
import com.nex.gamebook.attack.special.skill.Heal;
import com.nex.gamebook.attack.special.skill.LethalStrike;
import com.nex.gamebook.attack.special.skill.LifeLeech;
import com.nex.gamebook.attack.special.skill.QuickReaction;
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
		skills.put("defenseCondition", DefenseCondition.class);
		skills.put("attackCondition", AttackCondition.class);
		skills.put("attackBoost", AttackBoost.class);
		skills.put("healthCondition", BleedingSkill.class);
		skills.put("reflectDamage", ReflectDamage.class);
		skills.put("lethalStrike", LethalStrike.class);
		skills.put("lifeLeech", LifeLeech.class);
		skills.put("quickReaction", QuickReaction.class);
		skills.put("heal", Heal.class);
	}

	public static Map<String, Class<? extends SpecialSkill>> getSkills() {
		return instance.skills;
	}

	public static SpecialSkill get(String attackType) {
		try {
			Class<? extends SpecialSkill> skill = instance.skills.get(attackType);
			if(skill!=null)
			return skill.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Log.e("", "SkillPull", e);
		}
		return null;
	}
}
