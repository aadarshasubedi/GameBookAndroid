package com.nex.gamebook.attack.special.skill.conditional;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;

public class IncreaseHealthGreater extends IncreaseHealth {

	@Override
	public int getValue(Character character) {
		return getValue(character) * 2;
	}

}
