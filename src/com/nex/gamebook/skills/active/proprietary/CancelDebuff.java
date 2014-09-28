package com.nex.gamebook.skills.active.proprietary;

import java.util.Set;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.skills.active.ActiveCancelationSkill;

public class CancelDebuff extends ActiveCancelationSkill<Bonus> {
	public static String ID = "cancel_debuff";
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
		return ctx.getString(R.string.cancel_debuff_description, getSumOfCanceledSkills(attacker));
	}
	@Override
	public Set<Bonus> getCancelablesSkills(Character attacker) {
		return attacker.getConditions();
	}
	@Override
	public String getCanceledText(Context ctx, CharacterType type) {
		int text = R.string.player_cancel_debuff;
		if(CharacterType.ENEMY.equals(type))
			text = R.string.enemy_cancel_debuff;
		return ctx.getString(text);
	}
}
