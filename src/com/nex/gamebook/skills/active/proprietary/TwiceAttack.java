package com.nex.gamebook.skills.active.proprietary;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.combat.CombatProcess;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.active.ActiveSkill;

public class TwiceAttack extends ActiveSkill {

	public TwiceAttack() {
		super(NO_VALUE);
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
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat rm) {
		CombatProcess combat = new CombatProcess(resolveEnemy(attacker, attacked), callback.getEnemies());
		ResultCombat result = combat.doNormalAttack(callback, attacker, attacked, false);
		result.setSpecialAttack(this);
		callback.logAttack(result);
		return true;
	}

	@Override
	public String getDescription(Context context, Character attacker) {
		return context.getString(R.string.twice_attack_description);
	}

	@Override
	public boolean isPermanent() {
		return true;
	}

	@Override
	public int getValue(Character character) {
		return NO_VALUE;
	}


	@Override
	public int attemptsPerFight() {
		return 1;
	}
}
