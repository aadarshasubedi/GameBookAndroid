package com.nex.gamebook.attack.special.skill.conditional;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;

public class DecreaseHealth extends SpecialConditionalSkill {

	@Override
	public int getDescriptionId() {
		return R.string.decrease_health;
	}

	@Override
	public int getValue(Character character) {
		return character.getCurrentStats().getSkillpower() * 2;
	}

	@Override
	public int getValueWhenLuck(Character character) {
		return (int) (getValue(character) * 1.5);
	}

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
	public int attemptsPerFight() {
		return 4;
	}
	
	@Override
	public StatType getType() {
		return StatType.HEALTH;
	}

	@Override
	public boolean afterNormalAttack() {
		return true;
	}

	@Override
	public boolean isCondition() {
		return true;
	}
}
