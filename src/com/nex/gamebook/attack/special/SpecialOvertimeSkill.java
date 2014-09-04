package com.nex.gamebook.attack.special;

import com.nex.gamebook.game.ActiveOvertimeSkill;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.playground.BattleLogCallback;

public abstract class SpecialOvertimeSkill extends SpecialAttackSkill {

	public SpecialOvertimeSkill(int constantValue) {
		super(constantValue);
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
	public StatType getType() {
		return StatType.HEALTH;
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		SpecialSkill skill = getTargetSkill(attacker);
		ActiveOvertimeSkill newSkill = new ActiveOvertimeSkill(skill, getOvertimeTurns());
		if (isCondition()) {
			attacked.getOvertimeSkills().add(newSkill);
		} else {
			attacker.getOvertimeSkills().add(newSkill);
		}
		return true;
	}

	public abstract SpecialSkill getTargetSkill(Character character);

}
