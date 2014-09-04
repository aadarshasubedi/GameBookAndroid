package com.nex.gamebook.attack.special.skill.conditional;

import java.util.List;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.SpecialSkillsMap;

public class IncreaseDefense extends SpecialConditionalSkill {

	@Override
	public int getDescriptionId() {
		return R.string.increase_defense;
	}

	@Override
	public StatType getType() {
		return StatType.DEFENSE;
	}

	@Override
	public boolean isCondition() {
		return false;
	}

	@Override
	public int attemptsPerFight() {
		return 2;
	}

	@Override
	public boolean isPermanent() {
		return false;
	}

	@Override
	public boolean isTriggerBeforeEnemyAttack() {
		return false;
	}

	@Override
	public boolean isTriggerAfterEnemyAttack() {
		return false;
	}


	@Override
	public boolean inFight() {
		return false;
	}
	@Override
	public List<String> getBestAgainstSkill() {
		getBestAgainstSkill().add(SpecialSkillsMap.DECREASE_DEFENSE);
		return getBestAgainstSkill();
	}

}
