package com.nex.gamebook.attack.special.skill.attack;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.combat.CombatProcess;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public class QuickReaction extends SpecialAttackSkill {

	public QuickReaction() {
		super(NO_VALUE);
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat resultCombat) {
		CombatProcess combat = new CombatProcess(resolveEnemy(attacker, attacked));
		ResultCombat result = combat.doNormalAttack(attacker, attacked, getRealValue(attacker)/100f, false);
		result.setSpecialAttack(this);
		callback.logAttack(result);
		return false;
	}
	
	public int getRealValue(Character attacker) {
		return calcDynamicValue(40, super.getValue(attacker), attacker);
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
		return context.getString(R.string.quick_reaction_description, getRealValue(attacker),"%");
	}

	@Override
	public boolean isPermanent() {
		return true;
	}

	@Override
	public boolean isTriggerBeforeEnemyAttack() {
		return true;
	}

	@Override
	public boolean isTriggerAfterEnemyAttack() {
		return false;
	}



	@Override
	public int attemptsPerFight() {
		return 1;
	}

}
