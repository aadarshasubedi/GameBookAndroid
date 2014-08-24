package com.nex.gamebook.attack.special.skill;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.entity.Bonus;
import com.nex.gamebook.entity.Bonus.BonusType;
import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.playground.AttackCallback;

public class Heal extends SpecialAttackSkill {

	private static final long serialVersionUID = -5096382275794064276L;

	@Override
	public int getTextId() {
		return R.string.attr_health;
	}

	@Override
	public int getDescriptionId() {
		return R.string.heal_description;
	}

	@Override
	public int getNameId() {
		return R.string.heal_name;
	}

	@Override
	public int getTypeId() {
		return R.string.special_skill_type_buff;
	}

	@Override
	public int getValue(Character character) {
		return character.getCurrentStats().getSpecialSkillPower();
	}

	@Override
	public int getValueWhenLuck(Character character) {
		return getValue(character) * 2;
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
	public boolean doAttackOnce(Character attacker, Character attacked,
			AttackCallback callback, ResultCombat cm) {
		Bonus bonus = createSpecialAttack(1, getValue(attacker), BonusType.HEALTH);
		ResultCombat rc = createBasicResult(attacker.addBonus(bonus), attacker.getType());
		callback.logAttack(rc);
		return true;
	}

}
