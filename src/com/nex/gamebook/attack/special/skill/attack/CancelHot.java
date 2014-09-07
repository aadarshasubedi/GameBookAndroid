package com.nex.gamebook.attack.special.skill.attack;

import java.util.Set;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialCancelationSkill;
import com.nex.gamebook.game.ActiveOvertimeSkill;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;

public class CancelHot extends SpecialCancelationSkill<ActiveOvertimeSkill> {

	@Override
	public boolean isCancelPositive() {
		return true;
	}

	@Override
	public boolean isCondition() {
		return false;
	}

	@Override
	public Set<ActiveOvertimeSkill> getCancelablesSkills(Character attacker) {
		return attacker.getOvertimeSkills();
	}
	@Override
	public String getCanceledText(Context ctx, CharacterType type) {
		int text = R.string.player_cancel_hot;
		if(CharacterType.ENEMY.equals(type))
			text = R.string.enemy_cancel_hot;
		return ctx.getString(text);
	}
	@Override
	public String getDescription(Context ctx, com.nex.gamebook.game.Character attacker) {
		return ctx.getString(R.string.cancel_hot_description, getSumOfCanceledSkills(attacker));
	}

	
}
