package com.nex.gamebook.attack.special;

import com.nex.gamebook.game.ActiveOvertimeSkill;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.SpecialSkillsMap;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.playground.BattleLogCallback;

public abstract class SpecialOvertimeSkill extends SpecialAttackSkill {

	public SpecialOvertimeSkill(int constantValue) {
		super(constantValue);
	}


	@Override
	public StatType getType() {
		return StatType.HEALTH;
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		SpecialSkill skill = getTargetSkill(attacker);
		ActiveOvertimeSkill newSkill = new ActiveOvertimeSkill(skill, getOvertimeTurns());
		if (isCondition()) {
			attacked.getOvertimeSkills().add(newSkill);
		} else {
			attacker.getOvertimeSkills().add(newSkill);
		}
		return true;
	}

	public abstract SpecialSkill getTargetSkill(Character character);
	
	protected void redefinePropertiesIfProprietarySkillExist(SkillProperties properties, String proprietarySkill) {
		if(proprietarySkill!=null) {
			SpecialSkill s = SpecialSkillsMap.get(proprietarySkill);
			properties.setAfterEnemyAttack(s.isTriggerAfterEnemyAttack());
			properties.setAfterNormalAttack(s.afterNormalAttack());
			properties.setBeforeEnemyAttack(s.isTriggerBeforeEnemyAttack());
			properties.setBeforeEnemySkill(s.isTriggerBeforeEnemySpecialAttack());
		}
	}
}
