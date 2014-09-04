package com.nex.gamebook.attack.special.skill.attack;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.combat.CombatProcess;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public class TwiceAttack extends SpecialAttackSkill {

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
		CombatProcess combat = new CombatProcess(resolveEnemy(attacker, attacked));
		ResultCombat result = combat.doNormalAttack(attacker, attacked, false);
		result.setSpecialAttack(this);
		callback.logAttack(result);
		return true;
	}

	@Override
	public String getDescription(Context context) {
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
	public boolean showPercentage() {
		return false;
	}

	@Override
	public int getAspectId() {
		return R.string.special_skill_aspect_power;
	}

	@Override
	public int attemptsPerFight() {
		return 1;
	}
}
