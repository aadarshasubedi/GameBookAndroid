package com.nex.gamebook.attack.special.skill;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialConditionalSkill;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.playground.BattleLogCallback;

public class Bleeding extends SpecialConditionalSkill {
	
	private static final long serialVersionUID = 2040871476810819647L;
	
	@Override
	public int getTextId() {
		return R.string.attr_health;
	}

	@Override
	public int getDescriptionId() {
		return R.string.health_condition_description;
	}

	@Override
	public int getNameId() {
		return R.string.health_condition_name;
	}

	@Override
	public StatType getType() {
		return StatType.HEALTH;
	}

	@Override
	public boolean isCondition() {
		return true;
	}

	@Override
	public int attemptsPerFight() {
		return -1;
	}
	
	@Override
	public boolean isPermanent() {
		return true;
	}
	
	@Override
	public int getTypeId() {
		return R.string.special_skill_type_debuff;
	}

	@Override
	public boolean isTriggerBeforeEnemyAttack() {
		return false;
	}

	@Override
	public boolean afterNormalAttack() {
		return true;
	}
	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		if(cm.isLuck()) {
			return true;
		}
		return super.doAttackOnce(attacker, attacked, callback, cm);
	}
	@Override
	public boolean isTriggerAfterEnemyAttack() {
		return false;
	}
}
