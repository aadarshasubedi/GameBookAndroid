package com.nex.gamebook.attack.special.skill.conditional;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public class IncreaseHealth extends SpecialConditionalSkill {

	@Override
	public boolean isCondition() {
		return false;
	}

	@Override
	public int getDescriptionId() {
		return R.string.increase_health;
	}

	@Override
	public int getValue(Character character) {
		return character.getCurrentStats().getSpecialSkillPower() * 2;
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
	public int attemptsPerFight() {
		return 2;
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		if (attacker.getCurrentStats().getHealth() >= attacker.getStats().getHealth()) {
			cycles--;
			return true;
		}
		return doAttackOnce(attacker, attacked, callback, cm);
	}

	@Override
	public StatType getType() {
		return StatType.HEALTH;
	}

}
