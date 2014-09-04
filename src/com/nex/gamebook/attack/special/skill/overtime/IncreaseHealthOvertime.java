package com.nex.gamebook.attack.special.skill.overtime;

import java.util.ArrayList;
import java.util.List;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialOvertimeSkills;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.skill.conditional.IncreaseHealth;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.SpecialSkillsMap;

public class IncreaseHealthOvertime extends SpecialOvertimeSkills {

	@Override
	public int getDescriptionId() {
		return R.string.increase_health_overtime;
	}

	@Override
	public int getValue(Character character) {
		return getTargetSkill().getValue(character);
	}

	@Override
	public int attemptsPerFight() {
		return 1;
	}

	@Override
	public boolean isDebuff() {
		return false;
	}

	@Override
	public int getOvertimeTurns() {
		return 5;
	}

	@Override
	public int getValueWhenLuck(Character character) {
		return getTargetSkill().getValueWhenLuck(character);
	}

	@Override
	public StatType getType() {
		return StatType.HEALTH;
	}

	@Override
	public SpecialSkill getTargetSkill() {
		return new IncreaseHealth() {
			@Override
			public String getSkillNameKey() {
				return IncreaseHealthOvertime.this.getSkillNameKey();
			}
		};
	}

	@Override
	public List<String> getBestAgainstSkill() {
		List<String> s = new ArrayList<String>();
		s.add(SpecialSkillsMap.CANCEL_POSITIVE);
		return s;
	}

}
