package com.nex.gamebook.attack.special.skill.attack;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public class LifeLeech extends SpecialAttackSkill {

	public LifeLeech() {
		super(NO_VALUE);
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat resultCombat) {
		int dmg = resultCombat.getDamage();
		int leech = (int) ((float) dmg / 100f * getRealValue(attacker));
		Bonus bonus = createSpecialAttack(1, leech, StatType.HEALTH);
		bonus.setCondition(false);
		bonus.setOverflowed(false);
		ResultCombat rs = createBasicResult(leech, attacker.getType(), resolveEnemy(attacker, attacked));
		rs.setDamage(attacker.addBonus(bonus));
		callback.logAttack(rs);
		return false;
	}
	public int getRealValue(Character character) {
		return calcDynamicValue(30, super.getValue(character), character);
	}
	@Override
	public String getDescription(Context context, Character attacker) {
		return context.getString(R.string.life_leech_description, getRealValue(attacker), "%");
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
	public boolean afterNormalAttack() {
		return true;
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
	public int attemptsPerFight() {
		return 1;
	}
}
