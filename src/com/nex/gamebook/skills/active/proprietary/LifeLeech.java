package com.nex.gamebook.skills.active.proprietary;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.active.ActiveSkill;

public class LifeLeech extends ActiveSkill {
	public static String ID = "life_leech";
	public LifeLeech() {
		super(NO_VALUE);
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat resultCombat, boolean checkSummon) {
		int dmg = resultCombat.getDamage();
		int leech = (int) ((float) dmg / 100f * this.getValue(attacker));
		Bonus bonus = createSpecialAttack(1, leech, StatType.HEALTH);
		bonus.setCondition(false);
		bonus.setOverflowed(false);
		attacker.getStatistics().addHealedHealth(leech);
		ResultCombat rs = createBasicResult(leech, attacker.getType(), resolveEnemy(attacker, attacked));
		rs.setDamage(attacker.addBonus(bonus));
		callback.logAttack(rs);
		return false;
	}
	
	@Override
	public int getValue(Character character) {
		return calcDynamicValue(30, super.getValue(character), character);
	}
	
	@Override
	public String getDescription(Context context, Character attacker) {
		return context.getString(R.string.life_leech_description, this.getValue(attacker), "%");
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

}
