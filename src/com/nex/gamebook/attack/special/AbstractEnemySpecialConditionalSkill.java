package com.nex.gamebook.attack.special;

import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.CharacterType;
import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Player;

public abstract class AbstractEnemySpecialConditionalSkill extends AbstractSpecialConditionalSkill {

	private static final long serialVersionUID = -8487672109416376734L;

	@Override
	public Character resolveCharacterForApplication(Enemy enemy, Player player) {
		if(isCondition()) {
			return player;
		}
		return enemy;
	}
	
	@Override
	public Character resolveCharacterForValue(Enemy enemy, Player player) {
		return enemy;
	}
	
	@Override
	public CharacterType getCharacterType() {
		return CharacterType.ENEMY;
	}
}
