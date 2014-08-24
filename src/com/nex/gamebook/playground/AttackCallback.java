package com.nex.gamebook.playground;

import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.ResultCombat;

public interface AttackCallback {
	
	Player getCharacter();
	
	void logAttack(ResultCombat resultCombat);
	void divide(int turn);
	void fightEnd();
}
