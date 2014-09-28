package com.nex.gamebook.skills.active.proprietary;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.ResultCombatText;
import com.nex.gamebook.skills.active.ActiveSkill;

public class Kick extends ActiveSkill {
	public static String ID = "kick";
	public Kick() {
		super(NO_VALUE);
	}

	@Override
	public String getDescription(Context ctx, Character attacker) {
		return ctx.getString(R.string.kick);
	}

	@Override
	public StatType getType() {
		return null;
	}

	@Override
	public boolean isCondition() {
		return false;
	}
	@Override
	public boolean isTriggerAfterEnemyAttack() {
		return true;
	}
	@Override
	public int getValue(Character character) {
		return NO_VALUE;
	}
	
	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm, boolean checksummon) {
		attacked.setCanCastSkill(false);
		ResultCombat c = createBasicResult(0, attacker.getType(), resolveEnemy(attacker, attacked));
		callback.logAttack(c);
		return true;
	}

	@Override
	public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
		int text = R.string.you_cast;
		if(resultCombat.getType().equals(CharacterType.ENEMY))
		text = R.string.enemy_cast;
		return new ResultCombatText(R.color.reset, context.getString(text, resultCombat.getSpecialAttack().getName()));
	}
	
}
