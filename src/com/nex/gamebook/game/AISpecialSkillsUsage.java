package com.nex.gamebook.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.skills.active.ActiveCancelationSkill;
import com.nex.gamebook.skills.active.Skill;
import com.nex.gamebook.skills.active.proprietary.CancelBuff;
import com.nex.gamebook.skills.active.proprietary.CancelDebuff;
import com.nex.gamebook.skills.active.proprietary.CancelDot;
import com.nex.gamebook.skills.active.proprietary.CancelHot;

public class AISpecialSkillsUsage {

	public static Skill getBestSpecialSkill(Player player, final Enemy enemy, boolean enemyBegin) {
		List<Skill> friendlySkills = new ArrayList<>();
		List<Skill> attackSkills = new ArrayList<>();
		List<Skill> proprietarySkills = new ArrayList<>();
		for (Skill s : enemy.getActiveSkills()) {
			if (s.getProperties().proprietarySkillExists()) {
				proprietarySkills.add(s);
			} else if (s.isCondition()) {
				attackSkills.add(s);
			} else {
				friendlySkills.add(s);
			}
		}
		// seradime podle buffu/debuffu a hot dot
		Skill optimalProprietarySkill = findOptimalProprietarySkill(proprietarySkills, player, enemy);
		Skill optimalFriendlySkill = findOptimalFriendlySkill(friendlySkills, player, enemy);
		Skill optimalAttackSkill = findOptimalAttackSkill(attackSkills, player, enemy);
		List<Skill> optimalSkills = new ArrayList<>();
		if (optimalAttackSkill != null)
			optimalSkills.add(optimalAttackSkill);
		if (optimalProprietarySkill != null)
			optimalSkills.add(optimalProprietarySkill);
		if (optimalFriendlySkill != null)
			optimalSkills.add(optimalFriendlySkill);
		Collections.sort(optimalSkills, new Comparator<Skill>() {
			@Override
			public int compare(Skill lhs, Skill rhs) {
				// skilly na zruseni buff/hot nebo debuff/dot maji prednost pred
				// vsemi ostatnimi skily
				if (lhs instanceof ActiveCancelationSkill)
					return -1;
				if (rhs instanceof ActiveCancelationSkill)
					return 1;
				return Integer.valueOf(lhs.getValue(enemy)).compareTo(rhs.getValue(enemy));
			}
		});
		if (!optimalSkills.isEmpty())
			return optimalSkills.get(0);
		return null;
	}
	
	public static Skill findOptimalAttackSkill(List<Skill> skill, Player player, Enemy enemy) {
		Collections.sort(skill, new Comparator<Skill>() {
			@Override
			public int compare(Skill lhs, Skill rhs) {
				return Integer.valueOf(lhs.getOvertimeTurns()).compareTo(rhs.getOvertimeTurns());
			}
		});
		//TODO vytvorit AI na selekci spravneho skilu
		for (Skill s: skill) {
			if(!s.canUse()) continue;
			return s;
		}
		return null;
	}
	
	public static Skill findOptimalFriendlySkill(List<Skill> skill, Player player, Enemy enemy) {
		Collections.sort(skill, new Comparator<Skill>() {
			@Override
			public int compare(Skill lhs, Skill rhs) {
				return Integer.valueOf(lhs.getOvertimeTurns()).compareTo(rhs.getOvertimeTurns());
			}
		});
		//TODO vytvorit AI na selekci spravneho skilu
		for (Skill s : skill) {
			if(!s.canUse()) continue;
			return s;
		}
		return null;
	}
	public static Skill findOptimalProprietarySkill(List<Skill> skill, Player player, Enemy enemy) {		
		for (Skill s : skill) {
			if(!s.canUse()) continue;
			if(s instanceof ActiveCancelationSkill) {
				if (s instanceof CancelDot && enemy.hasDots()) {
					return s;
				} else if (s instanceof CancelDebuff && enemy.hasDebuffs()) {
					return s;
				} else if (s instanceof CancelHot && player.hasHots()) {
					return s;
				} else if (s instanceof CancelBuff && player.hasBuffs()) {
					return s;
				}
				continue;
			}
			//TODO dodelat pak AI pro ostatni proprietarni skily
			return s;
		}
		return null;
	}

}