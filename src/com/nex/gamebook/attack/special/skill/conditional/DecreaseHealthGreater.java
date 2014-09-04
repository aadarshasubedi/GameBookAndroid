package com.nex.gamebook.attack.special.skill.conditional;

import com.nex.gamebook.game.Character;

public class DecreaseHealthGreater extends DecreaseHealth {

	@Override
	public int getValue(Character character) {
		return super.getValue(character) * 2;
	}

}
