package com.nex.gamebook.attack.special.skill;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.entity.Bonus.BonusType;
import com.nex.gamebook.playground.AttackCallback;

public class ReflectDamage extends SpecialAttackSkill {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, AttackCallback callback, ResultCombat resultCombat) {
		int value = resultCombat.getDamage();
		int percentage = getRealValue(attacker);
		value = getResultValuePercentage(value, percentage);
		attacked.addBonus(createSpecialAttack(-1, value, BonusType.HEALTH));
		ResultCombat result = createBasicResult(value, attacker.getType());
		result.setEnemyName(resolveEnemy(attacker, attacked).getName());
		callback.logAttack(result);
		return true;
	}

	@Override
	public int getTextId() {
		return R.string.attr_health;
	}

	@Override
	public int getDescriptionId() {
		return R.string.reflect_damage_description;
	}

	@Override
	public int getNameId() {
		return R.string.reflect_damage_name;
	}

	@Override
	public int getTypeId() {
		return R.string.special_skill_type_attack;
	}
	@Override
	public int getValue(Character character) {
		return calcDynamicValue(30, 1.4f, character.getCurrentStats().getSpecialSkillPower());
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
		return false;
	}

	@Override
	public boolean isTriggerAfterEnemyAttack() {
		return true;
	}
	@Override
	public boolean showPercentage() {
		return true;
	}
	@Override
	public int getAspectId() {
		return R.string.special_skill_aspect_reflect;
	}
}
