package com.nex.gamebook.attack.special.skill;

import static com.nex.gamebook.game.SpecialSkillsMap.QUICK_REACTION;

import java.util.ArrayList;
import java.util.List;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.game.SpecialSkillsMap;
import com.nex.gamebook.game.Bonus.StatType;
import static com.nex.gamebook.game.SpecialSkillsMap.*;
public class Disarm extends SpecialConditionalSkill {
	private static final long serialVersionUID = -1804300495308316474L;
	
	@Override
	public int getMinAttributeForStopAttack() {
		return 1;
	}
	
	@Override
	public int getTextId() {
		return R.string.attr_attack;
	}

	@Override
	public int getDescriptionId() {
		return R.string.attack_condition_description;
	}
	@Override
	public int getNameId() {
		return R.string.attack_condition_name;
	}

	@Override
	public StatType getType() {
		return StatType.ATTACK;
	}

	@Override
	public boolean isCondition() {
		return true;
	}

	@Override
	public int attemptsPerFight() {
		return 1;
	}
	@Override
	public boolean isPermanent() {
		return false;
	}
	@Override
	public int getTypeId() {
		return R.string.special_skill_type_debuff;
	}

	@Override
	public boolean isTriggerBeforeEnemyAttack() {
		// TODO Auto-generated method stub
		return true;
	}

}
