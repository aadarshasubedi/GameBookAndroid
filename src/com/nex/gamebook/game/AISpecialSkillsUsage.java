package com.nex.gamebook.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.nex.gamebook.attack.special.SpecialCancelationSkill;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.skill.attack.CancelBuff;
import com.nex.gamebook.attack.special.skill.attack.CancelDebuff;
import com.nex.gamebook.attack.special.skill.attack.CancelDot;
import com.nex.gamebook.attack.special.skill.attack.CancelHot;
import com.nex.gamebook.game.Bonus.StatType;

public class AISpecialSkillsUsage {
	public static SpecialSkill getBestSpecialSkill(Player player, final Enemy enemy, boolean enemyBegin) {
		List<SpecialSkill> friendlySkills = new ArrayList<>();
		List<SpecialSkill> attackSkills = new ArrayList<>();
		List<SpecialSkill> proprietarySkills = new ArrayList<>();
		for (SpecialSkill s : enemy.getActiveSkills()) {
			if (s.getProperties().proprietarySkillExists()) {
				proprietarySkills.add(s);
			} else if (s.isCondition()) {
				attackSkills.add(s);
			} else {
				friendlySkills.add(s);
			}
		}
		// seradime podle buffu/debuffu a hot dot
		SpecialSkill optimalProprietarySkill = findOptimalProprietarySkill(proprietarySkills, player, enemy);
		SpecialSkill optimalFriendlySkill = findOptimalFriendlySkill(friendlySkills, player, enemy);
		SpecialSkill optimalAttackSkill = findOptimalAttackSkill(attackSkills, player, enemy);
		List<SpecialSkill> optimalSkills = new ArrayList<>();
		if (optimalAttackSkill != null)
			optimalSkills.add(optimalAttackSkill);
		if (optimalProprietarySkill != null)
			optimalSkills.add(optimalProprietarySkill);
		if (optimalFriendlySkill != null)
			optimalSkills.add(optimalFriendlySkill);
		Collections.sort(optimalSkills, new Comparator<SpecialSkill>() {
			@Override
			public int compare(SpecialSkill lhs, SpecialSkill rhs) {
				// skilly na zruseni buff/hot nebo debuff/dot maji prednost pred
				// vsemi ostatnimi skily
				if (lhs instanceof SpecialCancelationSkill)
					return -1;
				if (rhs instanceof SpecialCancelationSkill)
					return 1;
				return Integer.valueOf(lhs.getValue(enemy)).compareTo(rhs.getValue(enemy));
			}
		});
		if (!optimalSkills.isEmpty())
			return optimalSkills.get(0);
		return null;
	}
	
	public static SpecialSkill findOptimalAttackSkill(List<SpecialSkill> skill, Player player, Enemy enemy) {
		Collections.sort(skill, new Comparator<SpecialSkill>() {
			@Override
			public int compare(SpecialSkill lhs, SpecialSkill rhs) {
				return Integer.valueOf(lhs.getOvertimeTurns()).compareTo(rhs.getOvertimeTurns());
			}
		});
		//TODO vytvorit AI na selekci spravneho skilu
		for (SpecialSkill s: skill) {
			if(!s.canUse()) continue;
			return s;
		}
		return null;
	}
	
	public static SpecialSkill findOptimalFriendlySkill(List<SpecialSkill> skill, Player player, Enemy enemy) {
		Collections.sort(skill, new Comparator<SpecialSkill>() {
			@Override
			public int compare(SpecialSkill lhs, SpecialSkill rhs) {
				return Integer.valueOf(lhs.getOvertimeTurns()).compareTo(rhs.getOvertimeTurns());
			}
		});
		//TODO vytvorit AI na selekci spravneho skilu
		for (SpecialSkill s : skill) {
			if(!s.canUse()) continue;
			return s;
		}
		return null;
	}
	public static SpecialSkill findOptimalProprietarySkill(List<SpecialSkill> skill, Player player, Enemy enemy) {		
		for (SpecialSkill s : skill) {
			if(!s.canUse()) continue;
			if(s instanceof SpecialCancelationSkill) {
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