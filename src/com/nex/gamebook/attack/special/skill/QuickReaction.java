package com.nex.gamebook.attack.special.skill;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.combat.CombatProcess;
import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.playground.AttackCallback;

public class QuickReaction extends SpecialAttackSkill {
	
	private static final long serialVersionUID = -7126895421549658686L;

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked,
			AttackCallback callback, ResultCombat resultCombat) {
		CombatProcess combat = new CombatProcess(resolveEnemy(attacker, attacked));
		ResultCombat result = combat.doNormalAttack(attacker, attacked, getValue(attacker)/100f);
		result.setSpecialAttack(this);
		callback.logAttack(result);
		return false;
	}

	@Override
	public int getTextId() {
		return R.string.attr_health;
	}

	@Override
	public int getDescriptionId() {
		return R.string.quick_reaction_description;
	}

	@Override
	public int getNameId() {
		return R.string.quick_reaction_name;
	}

	@Override
	public int getTypeId() {
		return R.string.special_skill_type_attack;
	}

	@Override
	public int getValue(Character character) {
		return calcDynamicValue(40, 1.3f, character.getCurrentStats().getSpecialSkillPower());
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
	public boolean isTriggerBeforeEnemyAttack() {
		return true;
	}

	@Override
	public boolean isTriggerAfterEnemyAttack() {
		return false;
	}

	@Override
	public int getAspectId() {
		return R.string.special_skill_aspect_power;
	}

	@Override
	public boolean showPercentage() {
		return true;
	}

	@Override
	public int attemptsPerFight() {
		return 1;
	}

}
