package com.nex.gamebook.entity.special;

import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.playground.AttackCallback;

public interface SpecialAttack {

	
	void doAttack(Enemy enemy, Player player, AttackCallback callback);
	int getAffectedAttributeId();
}
