package com.nex.gamebook.attack.special.skill.conditional;

import java.util.ArrayList;
import java.util.List;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.SpecialSkillsMap;

public class IncreaseAttack extends SpecialConditionalSkill {

	@Override
	public int getDescriptionId() {
		return R.string.increase_attack;
	}

	@Override
	public StatType getType() {
		return StatType.ATTACK;
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
	public List<String> getBestInterceptSkills() {
		getBestInterceptSkills().add(SpecialSkillsMap.DECREASE_DEFENSE);
		return getBestInterceptSkills();
	}

	@Override
	public List<String> getBestAgainstSkill() {
		getBestAgainstSkill().add(SpecialSkillsMap.DECREASE_ATTACK);
		getBestAgainstSkill().add(SpecialSkillsMap.QUICK_REACTION);
		return getBestAgainstSkill();
	}

}
