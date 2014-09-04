package com.nex.gamebook.attack.special.skill.overtime;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.skill.conditional.IncreaseHealthGreater;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public class IncreaseHealthOvertimeGreater extends IncreaseHealthOvertime {

	@Override
	public int getDescriptionId() {
		return R.string.increase_health_overtime_greater;
	}
	
	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		IncreaseHealthGreater greaterHeal = new IncreaseHealthGreater() {
			@Override
			public String getSkillNameKey() {
				return IncreaseHealthOvertimeGreater.this.getSkillNameKey();
			}
		};
		greaterHeal.doAttackOnce(attacker, attacked, callback, cm);
		return super.doAttackOnce(attacker, attacked, callback, cm);
	}
	
}
