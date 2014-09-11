package com.nex.gamebook.skills;

import android.content.Context;

import com.nex.gamebook.game.ResultCombat;

public interface CombatTextDispatcher {
	public ResultCombatText getLogAttack(Context ctx, ResultCombat combat);
}
