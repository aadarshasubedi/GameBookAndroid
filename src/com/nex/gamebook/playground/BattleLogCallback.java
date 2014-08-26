package com.nex.gamebook.playground;

import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.ResultCombat;

public interface BattleLogCallback {
	
	Player getCharacter();
	void logAttack(ResultCombat resultCombat);
	void divide(int turn);
	void fightEnd(long xp);
	void logLevelIncreased();
	void logExperience(long xp);
}
