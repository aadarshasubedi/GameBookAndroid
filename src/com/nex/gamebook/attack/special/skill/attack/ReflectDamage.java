package com.nex.gamebook.attack.special.skill.attack;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public class ReflectDamage extends SpecialAttackSkill {

	public ReflectDamage() {
		super(NO_VALUE);
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat resultCombat) {
		int value = resultCombat.getDamage();
		int percentage = getValue(attacker);
		value = getResultValuePercentage(value, percentage);
		attacked.addBonus(createSpecialAttack(-1, value, getType()));
		ResultCombat result = createBasicResult(value, attacker.getType(), resolveEnemy(attacker, attacked));
		result.setEnemyName(resolveEnemy(attacker, attacked).getName());
		callback.logAttack(result);
		return true;
	}
	@Override
	public int getValue(Character character) {
		return calcDynamicValue(30, super.getValue(character), character);
	}
	@Override
	public StatType getType() {
		return StatType.HEALTH;
	}

	@Override
	public boolean isCondition() {
		return true;
	}

	@Override
	public String getDescription(Context context, Character attacker) {
		return context.getString(R.string.reflect_damage_description, getValue(attacker), "%");
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
	public int attemptsPerFight() {
		return 1;
	}
}
