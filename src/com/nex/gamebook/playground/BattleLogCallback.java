package com.nex.gamebook.playground;

import java.util.List;

import android.content.Context;

import com.nex.gamebook.game.Enemy;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.ResultCombat;

public interface BattleLogCallback {
	
	Player getCharacter();
	List<Enemy> getEnemies();
	Context getContext();
	void logAttack(ResultCombat resultCombat);
	void divide(int turn);
	void fightEnd(long xp);
	void logLevelIncreased();
	void logExperience(long xp);
	void logPassiveSkillsTriggered(String text);
	void logSummonDie(String summonName);
	void logText(String text, int color);
}
