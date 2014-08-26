package com.nex.gamebook.attack.special.skill;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.combat.CombatProcess;
import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public class TwiceAttack extends SpecialAttackSkill {
	private static final long serialVersionUID = -1804300495308316474L;


	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat rm) {
		CombatProcess combat = new CombatProcess(resolveEnemy(attacker, attacked));
		ResultCombat result = combat.doNormalAttack(attacker, attacked);
		result.setSpecialAttack(this);
		callback.logAttack(result);
		return true;
	}
	
	@Override
	public int getTextId() {
		return R.string.damage;
	}

	@Override
	public int getDescriptionId() {
		return R.string.twice_attack_description;
	}

	@Override
	public int getNameId() {
		return R.string.twice_attack_name;
	}

	@Override
	public int getTypeId() {
		return R.string.special_skill_type_attack;
	}

	@Override
	public boolean isPermanent() {
		return true;
	}
	
	@Override
	public int getValue(Character character) {
		return -1;
	}
	@Override
	public int getValueWhenLuck(Character character) {
		return getValue(character);
	}
	@Override
	public boolean showPercentage() {
		return false;
	}
	@Override
	public int getAspectId() {
		return R.string.special_skill_aspect_power;
	}
	
	@Override
	public int attemptsPerFight() {
		return 1;
	}
}
