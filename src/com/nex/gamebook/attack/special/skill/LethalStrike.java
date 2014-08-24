package com.nex.gamebook.attack.special.skill;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.entity.Bonus;
import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.entity.Bonus.BonusType;
import com.nex.gamebook.entity.Stats;
import com.nex.gamebook.playground.AttackCallback;

public class LethalStrike extends SpecialAttackSkill {

	private static final long serialVersionUID = 3670504540933316785L;

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked,
			AttackCallback callback, ResultCombat resultCombat) {
		ResultCombat result = createBasicResult(0, attacker.getType());
		if (attacker.hasLuck(getModificator(attacker))) {
			int damage = attacked.getCurrentStats().getHealth() - 1;
			result.setDamage(damage);
			Bonus bonus = createSpecialAttack(-1, damage, BonusType.HEALTH);
			attacked.addBonus(bonus);
			addTemporalBonus(attacked, bonus, createConditionId(attacker));
		}
		callback.logAttack(result);
		return true;
	}

	private int getModificator(Character c) {
		return (int) (Stats.TOTAL_LUCK_FOR_CALC - c.getCurrentStats().getSpecialSkillPower());
	}
	
	@Override
	public int getTextId() {
		return R.string.attr_health;
	}

	@Override
	public int getDescriptionId() {
		return R.string.lethal_strike_description;
	}

	@Override
	public int getNameId() {
		return R.string.lethal_strike_name;
	}

	@Override
	public int getTypeId() {
		return R.string.special_skill_type_attack;
	}

	@Override
	public int getValue(Character character) {
		return Stats.getPercentage(character.getCurrentStats().getLuck(), getModificator(character));
	}

	@Override
	public int getValueWhenLuck(Character character) {
		return (int) (getValue(character) * 1.5);
	}

	@Override
	public boolean isPermanent() {
		return false;
	}

	@Override
	public boolean showPercentage() {
		return true;
	}
	
	@Override
	public int getAspectId() {
		return R.string.special_skill_aspect_chance;
	}
}
