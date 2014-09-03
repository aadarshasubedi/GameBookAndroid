package com.nex.gamebook.game;

import static com.nex.gamebook.game.Bonus.StatType.ATTACK;
import static com.nex.gamebook.game.Bonus.StatType.HEALTH;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.skill.QuickReaction;
import com.nex.gamebook.game.Enemy.EnemyLevel;

public class AISpecialSkillsUsage {

	public static String getBestSpecialSkill(Player player, Enemy enemy, boolean enemyBegin) {
		if (enemy.getAvailableSkills().isEmpty())
			return null;
		List<SpecialSkill> enemyActiveSkills = sortSkills(player, enemy);

		List<String> candidates = null;
		if (!enemy.getEnemyLevel().equals(EnemyLevel.CREATURE)) {
			candidates = getBestCandidateSkills(enemyActiveSkills, player);
		}
		if (candidates == null || candidates.isEmpty()) {
			return findSkillIfNoCandidates(enemyActiveSkills, player, enemy, enemyBegin);
		}
		for (String s : candidates) {
			for (SpecialSkill activeSkill : enemyActiveSkills) {
				if (enemyBegin && activeSkill instanceof QuickReaction)
					continue;
				if (SpecialSkillsMap.isSpecialSkillEqualToName(activeSkill.getClass(), s) && activeSkill.canUse()) {
					return findInterceptAndReturnCorrectSkill(enemy, activeSkill, enemyActiveSkills);
				}
			}
		}
		return null;
	}
	
	private static String findInterceptAndReturnCorrectSkill(Enemy enemy, SpecialSkill specialSkill, List<SpecialSkill> activeSkills) {
		if(!enemy.getEnemyLevel().equals(EnemyLevel.BOSS)) return SpecialSkillsMap.getSkillId(specialSkill.getClass());
		for(String interceptor: specialSkill.getBestInterceptSkills()) {
			String skill = findInterceptSkill(interceptor, activeSkills);
			if(skill!=null) return skill;
		}
		return SpecialSkillsMap.getSkillId(specialSkill.getClass());
	}
	
	private static String findInterceptSkill(String interceptSkill, List<SpecialSkill> activeSkills) {
		for(SpecialSkill a:activeSkills) {
			String aId = SpecialSkillsMap.getSkillId(a.getClass());
			if(aId.equals(interceptSkill) && a.canUse())
				return aId;
		}
		return null;
	}
	
	private static void sortByUsage(List<SpecialSkill> skills) {
		Collections.sort(skills, new Comparator<SpecialSkill>() {
			@Override
			public int compare(SpecialSkill lhs, SpecialSkill rhs) {
				if (lhs.attemptsPerFight() == -1 && rhs.attemptsPerFight() == -1)
					return lhs.getCountOfUsed() > rhs.getCountOfUsed() ? 1 : -1;
				return 0;
			}
		});
	}

	public static String findSkillIfNoCandidates(List<SpecialSkill> sortedSkills, Player player, final Enemy enemy, boolean enemyBegin) {
		Stats c = enemy.getCurrentStats();
		Stats b = enemy.getStats();
		SpecialSkill plSkill = player.getSpecialSkill();
		List<SpecialSkill> limitedSkills = separateLimitedSkills(sortedSkills);
		boolean onlyInfiniteSkills = true;
		for (SpecialSkill s : limitedSkills) {
			if (s.canUse()) {
				onlyInfiniteSkills = false;
				break;
			}
		}
		if (onlyInfiniteSkills) {
			sortedSkills.removeAll(limitedSkills);
		}
		sortByUsage(sortedSkills);
		for (SpecialSkill s : sortedSkills) {
			if (plSkill != null)
				for (String a : s.getBestAgainstSkill()) {
					if (SpecialSkillsMap.isSpecialSkillEqualToName(plSkill.getClass(), a) && plSkill.canUse())
						continue;
				}
			int missingHP = b.getHealth() - c.getHealth();
			if (!s.canUse() || (s.getType().equals(HEALTH) && !s.isDebuff() && s.getValue(enemy) <= missingHP))
				continue;
			return findInterceptAndReturnCorrectSkill(enemy, s, sortedSkills);
		}
		return null;
	}

	public static List<SpecialSkill> separateLimitedSkills(List<SpecialSkill> sortedSkills) {
		List<SpecialSkill> limitedSkills = new ArrayList<SpecialSkill>();
		for (SpecialSkill skill : sortedSkills) {
			if (skill.attemptsPerFight() > 0) {
				limitedSkills.add(skill);
			}
		}
		return limitedSkills;
	}

	private static List<SpecialSkill> sortSkills(Player player, final Enemy enemy) {
		final Stats playerStats = player.getCurrentStats();
		final Stats currentStats = enemy.getCurrentStats();
		final Stats stats = enemy.getStats();

		List<SpecialSkill> sortedByAspects = new ArrayList<>(enemy.getActiveSkills());
		Collections.sort(sortedByAspects, new Comparator<SpecialSkill>() {
			@Override
			public int compare(SpecialSkill arg0, SpecialSkill arg1) {
				if (enemy.getEnemyLevel().equals(EnemyLevel.BOSS)) {
					if (arg0.getBestInterceptSkills().contains(SpecialSkillsMap.getSkillId(arg1.getClass()))) {
						return 1;
					}
					if (arg1.getBestInterceptSkills().contains(SpecialSkillsMap.getSkillId(arg0.getClass()))) {
						return -1;
					}
				}
				if (arg0.attemptsPerFight() == -1 && arg1.attemptsPerFight() > 0)
					return 1;
				if (arg1.attemptsPerFight() == -1 && arg0.attemptsPerFight() > 0)
					return -1;
				if(arg0.getType().equals(HEALTH) && !arg0.isDebuff())  {
					int totalDmg = playerStats.getTotalPureDamage();
					if((currentStats.getHealth()-totalDmg)<=0) return -1;
					return 1;
				}
				if(!enemy.getEnemyLevel().equals(EnemyLevel.CREATURE))
				if(arg0.getType().equals(ATTACK) && arg0.isDebuff()) return -1;
//				if (!arg0.isDebuff() && arg1.isDebuff() && !arg0.getType().equals(HEALTH)) {
//					return -1;
//				}
				return 1;
			}
		});
		return sortedByAspects;
	}

	private static List<String> getBestCandidateSkills(List<SpecialSkill> sortedSkills, Player player) {
		SpecialSkill plSkill = player.getSpecialSkill();
		if (plSkill == null || !plSkill.canUse())
			return null;
		List<String> againstSkills = plSkill.getBestAgainstSkill();
		List<String> candidates = new ArrayList<>();
		for (String against : againstSkills) {
			for (SpecialSkill skill : sortedSkills) {
				if (skill.canUse() && hasBestInterceptSkillsUsed(skill.getBestInterceptSkills(), sortedSkills) && SpecialSkillsMap.getSkillId(skill.getClass()).equals(against))
					candidates.add(against);
			}
		}
		return candidates;
	}

	private static boolean hasBestInterceptSkillsUsed(List<String> interceptSkills, List<SpecialSkill> activeSkills) {
		for (String is : interceptSkills) {
			for (SpecialSkill s : activeSkills) {
				if (SpecialSkillsMap.isSpecialSkillEqualToName(s.getClass(), is) && s.getCountOfUsed() == 0)
					return false;
			}
		}
		return true;
	}

}
