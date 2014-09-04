package com.nex.gamebook.attack.special.skill.attack;

import java.util.ArrayList;
import java.util.List;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.SpecialSkillsMap;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.playground.BattleLogCallback;

public class ReflectDamage extends SpecialAttackSkill {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat resultCombat) {
		int value = resultCombat.getDamage();
		int percentage = getRealValue(attacker);
		value = getResultValuePercentage(value, percentage);
		attacked.addBonus(createSpecialAttack(-1, value, getType()));
		ResultCombat result = createBasicResult(value, attacker.getType(), resolveEnemy(attacker, attacked));
		result.setEnemyName(resolveEnemy(attacker, attacked).getName());
		callback.logAttack(result);
		return true;
	}

	@Override
	public StatType getType() {
		return StatType.HEALTH;
	}

	@Override
	public boolean isDebuff() {
		return true;
	}

	@Override
	public int getDescriptionId() {
		return R.string.reflect_damage_description;
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
		return ASPECT_POWER;
	}
}
