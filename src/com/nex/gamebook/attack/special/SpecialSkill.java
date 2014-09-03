package com.nex.gamebook.attack.special;

import java.io.Serializable;
import java.util.List;

import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public interface SpecialSkill extends Serializable {

	/**
	 * return true if can proceed normal attack, only used when afterNormalAttack is false a triggers are disabled
	 * @param attacker
	 * @param attacked
	 * @param callback
	 * @param resultCombat
	 * @return
	 */
	boolean doAttack(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat resultCombat);
	int getTextId();
	int getDescriptionId();
	int getNameId();
	int getTypeId();
	
	int getValue(Character character);
	int getValueWhenLuck(Character character);
	boolean isPermanent();
	boolean isTriggerBeforeEnemyAttack();
	boolean isTriggerAfterEnemyAttack();
	boolean isTriggerBeforeEnemySpecialAttack();
	
	int getAspectId();
	boolean showPercentage();
	/**
	 * Called after fight ends
	 * 
	 * @return
	 */
	void cleanAfterFightEnd();
	void cleanAfterBattleEnd();
	/**
	 * Return -1 if special attack is applicable every turn.
	 * @return int
	 */
	int attemptsPerFight();
	boolean afterNormalAttack();
	/**
	 * when true then attemptsPerFight() is applicable per fight
	 * when false then skill is used only once in battle
	 * @return
	 */
	boolean inFight();
	boolean canUse();
	StatType getType();
	boolean isDebuff();
	/**
	 * This list is used by bosses and minions
	 * @return
	 */
	List<String> getBestAgainstSkill();
	/**
	 * This list is used only by bosses
	 * @return
	 */
	List<String> getBestInterceptSkills();
	int getCountOfUsed();
}

