package com.nex.gamebook.attack.special.skill.overtime;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public class DecreaseHealthOvertimeGreater extends IncreaseHealthOvertime {

	@Override
	public int getDescriptionId() {
		return R.string.decrease_health_overtime_greater;
	}
	
	
	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		DecreaseHealthOvertimeGreater decrease = new DecreaseHealthOvertimeGreater() {
			@Override
			public String getSkillNameKey() {
				return DecreaseHealthOvertimeGreater.this.getSkillNameKey();
			}
		};
		decrease.doAttackOnce(attacker, attacked, callback, cm);
		return super.doAttackOnce(attacker, attacked, callback, cm);
	}
	
}
