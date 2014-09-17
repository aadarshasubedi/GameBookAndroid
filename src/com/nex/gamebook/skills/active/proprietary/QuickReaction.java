package com.nex.gamebook.skills.active.proprietary;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.combat.CombatProcess;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.active.ActiveSkill;

public class QuickReaction extends ActiveSkill {

	public QuickReaction() {
		super(NO_VALUE);
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat resultCombat) {
		CombatProcess combat = new CombatProcess(resolveEnemy(attacker, attacked), callback.getEnemies());
		ResultCombat result = combat.doNormalAttack(attacker, attacked, this.getValue(attacker)/100f, false);
		result.setSpecialAttack(this);
		callback.logAttack(result);
		return false;
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
		return context.getString(R.string.quick_reaction_description, this.getValue(attacker),"%");
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
