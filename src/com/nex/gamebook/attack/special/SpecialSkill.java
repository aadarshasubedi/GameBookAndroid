package com.nex.gamebook.attack.special;

import java.io.Serializable;

import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.playground.AttackCallback;

public interface SpecialSkill extends Serializable {

	
	void doAttack(Enemy enemy, Player player, AttackCallback callback);
	int getTextId();
	int getDescriptionId();
	int getNameId();
	int getTypeId();
	/**
	 * Called after fight ends
	 * 
	 * @return
	 */
	void clean();
	boolean isPermanent();
	boolean isTriggerEnemy();
//	boolean isEnemy();
	
}
