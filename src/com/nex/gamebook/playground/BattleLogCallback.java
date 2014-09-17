package com.nex.gamebook.playground;

import java.util.List;

import com.nex.gamebook.game.Enemy;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.ResultCombat;

public interface BattleLogCallback {
	
	Player getCharacter();
	List<Enemy> getEnemies();
	void logAttack(ResultCombat resultCombat);
	void divide(int turn);
	void fightEnd(long xp);
	void logLevelIncreased();
	void logExperience(long xp);
}
