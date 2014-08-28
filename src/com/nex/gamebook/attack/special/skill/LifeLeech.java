package com.nex.gamebook.attack.special.skill;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.entity.Bonus;
import com.nex.gamebook.entity.Bonus.StatType;
import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public class LifeLeech extends SpecialAttackSkill {

	private static final long serialVersionUID = -7676824496024693983L;

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked,
			BattleLogCallback callback, ResultCombat resultCombat) {
		int dmg = resultCombat.getDamage();
		int leech = (int) ((float)dmg/100f * getRealValue(attacker));
		Bonus bonus = createSpecialAttack(1, leech, StatType.HEALTH);
		bonus.setCondition(false);
		bonus.setOverflowed(false);
		ResultCombat rs = createBasicResult(leech, attacker.getType());
		rs.setDamage(attacker.addBonus(bonus));
		callback.logAttack(rs);
		return false;
	}

	@Override
	public int getTextId() {
		return R.string.attr_health;
	}

	@Override
	public int getDescriptionId() {
		return R.string.life_leech_description;
	}

	@Override
	public int getNameId() {
		return R.string.life_leech_name;
	}

	@Override
	public int getTypeId() {
		return R.string.special_skill_type_attack;
	}

	@Override
	public int getValue(Character character) {
		return calcDynamicValue(30, 1.2f, character.getCurrentStats().getSpecialSkillPower());
	}

	@Override
	public int getValueWhenLuck(Character character) {
		return (int) (getValue(character) * 1.2);
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
	public boolean afterNormalAttack() {
		return true;
	}
}
