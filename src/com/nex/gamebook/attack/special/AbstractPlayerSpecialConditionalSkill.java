package com.nex.gamebook.attack.special;

import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.CharacterType;
import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Player;

public abstract class AbstractPlayerSpecialConditionalSkill extends AbstractSpecialConditionalSkill {

	private static final long serialVersionUID = -2250549340063825168L;
	@Override
	public Character resolveCharacterForApplication(Enemy enemy, Player player) {
		if(isCondition()) {
			return enemy;
		}
		return player;
	}
	
	@Override
	public Character resolveCharacterForValue(Enemy enemy, Player player) {
		return player;
	}
	
	@Override
	public int getMinAttributeForStopAttack() {
		return -1;
	}

	@Override
	public CharacterType getCharacterType() {
		return CharacterType.PLAYER;
	}
	
}
