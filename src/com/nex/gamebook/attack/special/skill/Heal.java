package com.nex.gamebook.attack.special.skill;

import java.util.ArrayList;
import java.util.List;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.SpecialSkillsMap;
import com.nex.gamebook.playground.BattleLogCallback;
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
			BattleLogCallback callback, ResultCombat cm) {
		if(attacker.getCurrentStats().getHealth() >= attacker.getStats().getHealth()) {
			cycles--;
			used=false;
			return true;
		}
		ResultCombat rc = createBasicResult(0, attacker.getType(), resolveEnemy(attacker, attacked));
		Bonus bonus = createSpecialAttack(1, getRealValue(attacker), getType());
		bonus.setOverflowed(false);
		rc.setDamage(attacker.addBonus(bonus));
		callback.logAttack(rc);
		return true;
	}
	@Override
	public StatType getType() {
		return StatType.HEALTH;
	}

}
