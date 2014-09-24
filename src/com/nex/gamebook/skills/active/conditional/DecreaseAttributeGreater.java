package com.nex.gamebook.skills.active.conditional;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.active.Skill;

public class DecreaseAttributeGreater extends DecreaseAttribute {
	private String proprietarySkill;

	public DecreaseAttributeGreater(StatType type, String proprietary) {
		super(type, NO_VALUE);
		this.proprietarySkill = proprietary;
	}

	
	public String getDescription(Context ctx, Character attacker) {
		Skill skill = createSkill(attacker, proprietarySkill);
		return super.getDescription(ctx, attacker) + " "+ctx.getString(R.string.and)+" " + skill.getDescription(ctx, attacker).toLowerCase();
	}
	
	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		Skill skill = createSkill(attacker, proprietarySkill);
		skill.setData(properties, skillName);
		skill.doAttack(attacker, attacked, callback, cm);
		return super.doAttackOnce(attacker, attacked, callback, cm);
	}

}
