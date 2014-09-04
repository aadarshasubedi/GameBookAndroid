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
		int leech = (int) ((float) dmg / 100f * getValue(attacker));
		Bonus bonus = createSpecialAttack(1, leech, StatType.HEALTH);
		bonus.setCondition(false);
		bonus.setOverflowed(false);
		ResultCombat rs = createBasicResult(leech, attacker.getType(), resolveEnemy(attacker, attacked));
		rs.setDamage(attacker.addBonus(bonus));
		callback.logAttack(rs);
		return false;
	}
//	@Override
//	public int getValue(Character character) {
//		return calcDynamicValue(30, 1.2f, character.getCurrentStats().getSpecialSkillPower());
//	}
	@Override
	public String getDescription(Context context) {
		return context.getString(R.string.life_leech_description);
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
	public boolean showPercentage() {
		return true;
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
	public int getAspectId() {
		return ASPECT_POWER;
	}
	@Override
	public boolean isCondition() {
		return true;
	}
}
