package com.nex.gamebook.game;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.playground.BattleLogCallback;

public class ActiveOvertimeSkill {

	private SpecialSkill targetSkill;

	private int turns;
	private int currentTurns = 0;
	private String overrideSkillname;

	public ActiveOvertimeSkill(SpecialSkill targetSkill, int turns) {
		super();
		this.targetSkill = targetSkill;
		this.turns = turns;
	}

	public SpecialSkill getTargetSkill() {
		return targetSkill;
	}

	public int getTurns() {
		return turns;
	}

	public String getOverrideSkillname() {
		return overrideSkillname;
	}

	public boolean execute(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat resultCombat) {
		if (currentTurns == turns)
			return false;
		targetSkill.doAttack(attacker, attacked, callback, resultCombat);
		targetSkill.cleanAfterBattleEnd();
		targetSkill.cleanAfterFightEnd();
		currentTurns++;
		return true;
	}

}
