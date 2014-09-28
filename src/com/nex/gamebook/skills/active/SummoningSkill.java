package com.nex.gamebook.skills.active;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Summon;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.ResultCombatText;

public abstract class SummoningSkill extends ActiveSkill {

	public SummoningSkill() {
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
	public boolean isPermanent() {
		return true;
	}

	
	
	public abstract Summon createSummon(Character character);

	@Override
	public int getValue(Character character) {
		return NO_VALUE;
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm, boolean checkSummon) {
		attacker.setSummon(createSummon(attacker));
		ResultCombat r = createBasicResult(0, attacker.getType(), resolveEnemy(attacker, attacked));
		callback.logAttack(r);
		return false;
	}

	@Override
	public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
		int text = R.string.you_summon_creature;
		if (resultCombat.getType().equals(CharacterType.ENEMY))
			text = R.string.enemy_summon_creature;
		return new ResultCombatText(R.color.reset, context.getString(text, getName()));
	}
	
	@Override
	public boolean resetAtBattleEnd() {
		return true;
	}
}
