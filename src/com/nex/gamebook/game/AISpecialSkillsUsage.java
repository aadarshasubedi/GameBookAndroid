package com.nex.gamebook.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.skill.LethalStrike;
import com.nex.gamebook.attack.special.skill.Rage;

import static com.nex.gamebook.game.Bonus.StatType.*;

public class AISpecialSkillsUsage {

	public static String getBestSpecialSkill(Player player, Enemy enemy, boolean enemyBegin) {
		if (enemy.getAvailableSkills().isEmpty())
			return null;
		List<SpecialSkill> enemyActiveSkills = sortSkills(player, enemy);
		List<String> candidates = getBestCandidateSkills(enemyActiveSkills, player);
		if (candidates == null || candidates.isEmpty()) {
			return findSkillIfNoCandidates(enemyActiveSkills, player, enemy, enemyBegin);
		}
		for (String s : candidates) {
			for (SpecialSkill activeSkill : enemyActiveSkills) {
				if (enemyBegin && activeSkill.isTriggerBeforeEnemyAttack())
					continue;
				if (SpecialSkillsMap.isSpecialSkillEqualToName(activeSkill.getClass(), s) && activeSkill.canUse()) {
					return s;
				}
			}
		}
		return null;
	}

	public static String findSkillIfNoCandidates(List<SpecialSkill> sortedSkills,  Player player, final Enemy enemy, boolean enemyBegin) {
		Stats c = enemy.getCurrentStats();
		Stats b = enemy.getStats();
		SpecialSkill plSkill = player.getSpecialSkill();
		//sekundarni logika
		for (SpecialSkill s : sortedSkills) {
			if(plSkill!=null)
			for(String a: s.getBestAgainstSkill()) {
				if(SpecialSkillsMap.isSpecialSkillEqualToName(plSkill.getClass(), a) && plSkill.canUse()) continue;
			}
			if (!s.canUse() || (s.isTriggerBeforeEnemyAttack() && enemyBegin))
				continue;
			String skillId = SpecialSkillsMap.getSkillId(s.getClass());
			if (c.getHealth() < b.getHealth() && s.getType().equals(HEALTH) && !s.isDebuff()) {
				return skillId;
			} else if (!s.isDebuff() && !s.getType().equals(HEALTH)) {
				return skillId;
			} else if (s.getType().equals(HEALTH) && s.isDebuff()) {
				return skillId;
			}
		}
		return null;
	}

	private static List<SpecialSkill> sortSkills(Player player, Enemy enemy) {
		final Stats playerStats = player.getCurrentStats();
		final Stats currentStats = enemy.getCurrentStats();
		final Stats stats = enemy.getStats();
		
		List<SpecialSkill> sortedByAspects = new ArrayList<>(enemy.getActiveSkills());
		Collections.sort(sortedByAspects, new Comparator<SpecialSkill>() {
			@Override
			public int compare(SpecialSkill arg0, SpecialSkill arg1) {
				
//				for(String interceptSkill: arg1.getBestInterceptSkills()) {
//					if(SpecialSkillsMap.isSpecialSkillEqualToName(arg0.getClass(), interceptSkill))
//						return 1;
//				}
				//hlavni logika
				if(arg0 instanceof LethalStrike)
					return 1;
				if(arg1 instanceof LethalStrike)
					return -1;
				if(arg0.getType().equals(HEALTH) && !arg0.isDebuff()){
					int missingHealth = stats.getHealth() - currentStats.getHealth();
					if(missingHealth < playerStats.getTotalPureDamage()) {
						return -1;
					}
				}
//				if(arg0 instanceof Rage)
				if(arg0.getBestInterceptSkills().contains(SpecialSkillsMap.getSkillId(arg1.getClass()))) {
						return -1;
				}
//				if(arg1 instanceof Rage)
				if(arg1.getBestInterceptSkills().contains(SpecialSkillsMap.getSkillId(arg0.getClass()))) {
					return 1;
				}
				if(!arg0.isDebuff() && arg1.isDebuff()) {
					return -1;
				}
				return 1;
			}
		});
		return sortedByAspects;
	}
	
	private static List<String> getBestCandidateSkills(List<SpecialSkill> sortedSkills, Player player) {
		SpecialSkill plSkill = player.getSpecialSkill();
		if (plSkill == null)
			return null;
		List<String> againstSkills = plSkill.getBestAgainstSkill();
		List<String> candidates = new ArrayList<>();
		for (String against : againstSkills) {
			for(SpecialSkill skill: sortedSkills) {
				if(hasBestInterceptSkillsUsed(skill.getBestInterceptSkills(), sortedSkills))
					candidates.add(against);
			}
		}
		return candidates;
	}
	
	private static boolean hasBestInterceptSkillsUsed(List<String> interceptSkills, List<SpecialSkill> activeSkills) {
		for(String is: interceptSkills) {
			for(SpecialSkill s: activeSkills) {
				if(SpecialSkillsMap.isSpecialSkillEqualToName(s.getClass(), is) && s.getCountOfUsed() == 0)
					return false;
			}
		}
		return true;
	}
	
}
