package com.nex.gamebook.character.definition;

import com.nex.gamebook.R;

public class Warrior extends Character {
	
	public static int MARKER = R.string.ch_warrior;
	
	public Warrior() {
		super(MARKER);
	}
	
	@Override
	public int getDescription() {
		return R.string.dsc_warrior;
	}
}
