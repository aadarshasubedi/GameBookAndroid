package com.nex.gamebook.entity;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.enemy.EnemyDefenseCondition;
import com.nex.gamebook.attack.special.enemy.EnemyBleedingSkill;
import com.nex.gamebook.attack.special.player.AttackBoost;
import com.nex.gamebook.attack.special.player.AttackCondition;
import com.nex.gamebook.attack.special.player.DefenseCondition;
import com.nex.gamebook.attack.special.player.TwiceAttack;

public class SpecialSkillsMap {

	private Map<String, SpecialSkill> playerAttacks = new HashMap<>();
	private Map<String, Class<? extends SpecialSkill>> enemyAttacks = new HashMap<>();
	private static SpecialSkillsMap instance;
	static {
		instance = new SpecialSkillsMap();
		instance.init();
	}

	private void init() {
		initEnemyAttacks();
		initPlayerAttacks();
	}

	private void initPlayerAttacks() {
		playerAttacks.put("twiceAttack", new TwiceAttack());
		playerAttacks.put("defenseCondition", new DefenseCondition());
		playerAttacks.put("attackCondition", new AttackCondition());
		playerAttacks.put("attackBoost", new AttackBoost());
	}

	private void initEnemyAttacks() {
		enemyAttacks.put("defenseCondition", EnemyDefenseCondition.class);
		enemyAttacks.put("healthCondition", EnemyBleedingSkill.class);
	}

	public static Map<String, Class<? extends SpecialSkill>> getEnemyAttacks() {
		return instance.enemyAttacks;
	}

	public static Map<String, SpecialSkill> getPlayerAttacks() {
		return instance.playerAttacks;
	}

	public static SpecialSkill getPlayersAttack(String attackType) {
		return instance.playerAttacks.get(attackType);
	}

	public static SpecialSkill getEnemiesAttack(String attackType) {
		try {
			Class<? extends SpecialSkill> skill = instance.enemyAttacks.get(attackType);
			if(skill!=null)
			return skill.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Log.e("", "SkillPull", e);
		}
		return null;
	}
}
