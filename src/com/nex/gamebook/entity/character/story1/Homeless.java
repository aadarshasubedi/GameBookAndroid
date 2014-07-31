package com.nex.gamebook.entity.character.story1;

import com.nex.gamebook.R;
import com.nex.gamebook.entity.character.Character;

public class Homeless extends Character {

	@Override
	public int getDescription() {
		return R.string.dsc_homeless;
	}

	@Override
	public int getName() {
		return R.string.ch_homeless;
	}

}
