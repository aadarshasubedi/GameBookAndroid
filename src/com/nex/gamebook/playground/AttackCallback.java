package com.nex.gamebook.playground;

import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.ResultCombat;

public interface AttackCallback {
	
	Player getCharacter();
	
	void attackCallBack(ResultCombat resultCombat);
	
	void fightEnd();
}
