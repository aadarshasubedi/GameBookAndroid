package com.nex.gamebook.attack.special.skill.conditional;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.game.Bonus.StatType;

public class DecreaseSkillpower extends SpecialConditionalSkill {

	@Override
	public int getMinAttributeForStopAttack() {
		return 1;
	}

	@Override
	public int getDescriptionId() {
		return R.string.decrease_skillpower;
	}

	@Override
	public StatType getType() {
		return StatType.SKILLPOWER;
	}

	@Override
	public boolean isCondition() {
		return true;
	}

	@Override
	public int attemptsPerFight() {
		return 1;
	}

	@Override
	public boolean isPermanent() {
		return false;
	}

	@Override
	public boolean isTriggerBeforeEnemyAttack() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isTriggerBeforeEnemySpecialAttack() {
		return true;
	}
}