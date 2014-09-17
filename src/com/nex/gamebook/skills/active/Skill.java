package com.nex.gamebook.skills.active;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Story;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.CombatTextDispatcher;

public interface Skill {
	/**
	 * return true if can proceed normal attack, only used when
	 * afterNormalAttack is false a triggers are disabled
	 * 
	 * @param attacker
	 * @param attacked
	 * @param callback
	 * @param resultCombat
	 * @return
	 */
	boolean doAttack(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat resultCombat);
	String getDescription(Context ctx, Character attacker);
	int getValue(Character character);
	boolean isPermanent();
	boolean isTriggerBeforeEnemyAttack();
	boolean isTriggerAfterEnemyAttack();
	boolean isTriggerBeforeEnemySpecialAttack();
	boolean isTriggerOnEndOfRound();
	
	/**
	 * Called after fight ends
	 * 
	 * @return
	 */
	void cleanAfterFightEnd();
	void cleanAfterBattleEnd();
	/**
	 * Return -1 if special attack is applicable every turn.
	 * 
	 * @return int
	 */
	int attemptsPerFight();
	boolean afterNormalAttack();
	/**
	 * when true then attemptsPerFight() is applied per fight else is applied per battle
	 * skill is used only once in battle
	 * 
	 * @return
	 */
	boolean resetAtBattleEnd();
	boolean canUse();
	StatType getType();
	boolean isCondition();
	boolean isOverTime();	
	int getOvertimeTurns();
	boolean causeDamage();
	int getCountOfUsed();
	boolean doSomething(Character attacked, Character attacker);
	String getName();
	void setData(SkillProperties properties, String translatedSkillName);
	SkillProperties getProperties();
	void setCombatTextDispatcher(CombatTextDispatcher dispatcher);
	CombatTextDispatcher getCombatTextDispatcher();
}