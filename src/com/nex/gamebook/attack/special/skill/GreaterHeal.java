package com.nex.gamebook.attack.special.skill;

import com.nex.gamebook.R;
import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public class GreaterHeal extends Heal {

	private static final long serialVersionUID = 1L;

	@Override
	public int getTextId() {
		return R.string.attr_health;
	}

	@Override
	public int getDescriptionId() {
		return R.string.greater_heal_description;
	}

	@Override
	public int getNameId() {
		return R.string.greater_heal_name;
	}

	@Override
	public int getTypeId() {
		return R.string.special_skill_type_buff;
	}

	@Override
	public int getValue(Character character) {
		return character.getCurrentStats().getSpecialSkillPower() * 4;
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
		return R.string.special_skill_aspect_power;
	}

	@Override
	public boolean showPercentage() {
		return false;
	}
	
	@Override
	public int attemptsPerFight() {
		return 1;
	}
	
	@Override
	public boolean doAttack(Character attacker, Character attacked,
			BattleLogCallback callback, ResultCombat resultCombat) {
		return doAttackOnce(attacker, attacked, callback, resultCombat);
	}
	@Override
	public boolean isTriggerAfterEnemyAttack() {
		return true;
	}
	@Override
	public boolean doAttackOnce(Character attacker, Character attacked,
			BattleLogCallback callback, ResultCombat cm) {
		return super.doAttackOnce(attacker, attacked, callback, cm);
	}


}