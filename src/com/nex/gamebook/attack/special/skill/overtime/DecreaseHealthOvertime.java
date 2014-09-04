package com.nex.gamebook.attack.special.skill.overtime;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.skill.conditional.DecreaseHealth;

public class DecreaseHealthOvertime extends IncreaseHealthOvertime {

	@Override
	public int getDescriptionId() {
		return R.string.decrease_health_overtime;
	}
	
	public SpecialSkill getTargetSkill() {
		return new DecreaseHealth();		
	}
	
	@Override
	public boolean isDebuff() {
		return true;
	}
	
}
