package com.nex.gamebook.skills.active.proprietary;

import java.util.Set;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.skills.active.ActiveCancelationSkill;
import com.nex.gamebook.skills.active.OvertimeSkill;

public class CancelDot extends ActiveCancelationSkill<OvertimeSkill> {

	@Override
	public boolean isCancelPositive() {
		return false;
	}

	@Override
	public boolean isCondition() {
		return false;
	}

	@Override
	public String getDescription(Context ctx, com.nex.gamebook.game.Character attacker) {
		return ctx.getString(R.string.cancel_dot_description, getSumOfCanceledSkills(attacker));
	}
	@Override
	public Set<OvertimeSkill> getCancelablesSkills(Character attacker) {
		return attacker.getOvertimeSkills();
	}
	@Override
	public String getCanceledText(Context ctx, CharacterType type) {
		int text = R.string.player_cancel_dot;
		if(CharacterType.ENEMY.equals(type))
			text = R.string.enemy_cancel_dot;
		return ctx.getString(text);
	}
}
