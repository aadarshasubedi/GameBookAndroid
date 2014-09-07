package com.nex.gamebook.attack.special.skill.attack;

import java.util.Set;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialCancelationSkill;
import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;

public class CancelBuff extends SpecialCancelationSkill<Bonus> {

	@Override
	public boolean isCancelPositive() {
		return true;
	}

	@Override
	public boolean isCondition() {
		return false;
	}

	@Override
	public String getDescription(Context ctx, com.nex.gamebook.game.Character attacker) {
		return ctx.getString(R.string.cancel_buff_description, getSumOfCanceledSkills(attacker));
	}
	@Override
	public Set<Bonus> getCancelablesSkills(Character attacker) {
		return attacker.getConditions();
	}
	@Override
	public String getCanceledText(Context ctx, CharacterType type) {
		int text = R.string.player_cancel_buff;
		if(CharacterType.ENEMY.equals(type))
			text = R.string.enemy_cancel_buff;
		return ctx.getString(text);
	}
}
