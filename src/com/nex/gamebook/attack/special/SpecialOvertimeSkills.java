package com.nex.gamebook.attack.special;

import com.nex.gamebook.game.ActiveOvertimeSkill;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public abstract class SpecialOvertimeSkills extends SpecialAttackSkill {

	@Override
	public boolean isPermanent() {
		return true;
	}

	@Override
	public int getAspectId() {
		return ASPECT_POWER;
	}

	@Override
	public boolean showPercentage() {
		return false;
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		SpecialSkill  skill = getTargetSkill();
		ActiveOvertimeSkill newSkill = new ActiveOvertimeSkill(skill, getOvertimeTurns());
		if(isDebuff()) {
			attacked.getOvertimeSkills().add(newSkill);
		} else {
			attacker.getOvertimeSkills().add(newSkill);	
		}
		return true;
	}

	public abstract SpecialSkill getTargetSkill();
	
	@Override
	public int getOvertimeTurns() {
		return 2;
	}

}
