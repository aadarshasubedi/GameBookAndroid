package com.nex.gamebook.attack.special;

import java.io.Serializable;

import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.playground.AttackCallback;

public interface SpecialSkill extends Serializable {

	/**
	 * return true if can proceed normal attack, only used when afterNormalAttack is false a triggers are disabled
	 * @param attacker
	 * @param attacked
	 * @param callback
	 * @param resultCombat
	 * @return
	 */
	boolean doAttack(Character attacker, Character attacked, AttackCallback callback, ResultCombat resultCombat);
	int getTextId();
	int getDescriptionId();
	int getNameId();
	int getTypeId();
	
	int getValue(Character character);
	int getValueWhenLuck(Character character);
	boolean isPermanent();
	boolean isTriggerBeforeEnemyAttack();
	boolean isTriggerAfterEnemyAttack();
	int getAspectId();
	boolean showPercentage();
	/**
	 * Called after fight ends
	 * 
	 * @return
	 */
	void clean();
	/**
	 * Return -1 if special attack is aplicable every turn.
	 * @return int
	 */
	int attemptsPerFight();
	boolean afterNormalAttack();
}
