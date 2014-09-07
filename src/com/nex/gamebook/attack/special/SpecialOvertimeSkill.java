package com.nex.gamebook.attack.special;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.ActiveOvertimeSkill;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
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
		skill.setData(createOvertimeSkillProperties(), getName());
		if (isCondition()) {
			attacked.getOvertimeSkills().add(newSkill);
		} else {
			attacker.getOvertimeSkills().add(newSkill);
		}
		ResultCombat r = createBasicResult(0, attacker.getType(), resolveEnemy(attacker, attacked));
		callback.logAttack(r);
		return true;
	}

	public abstract SpecialSkill getTargetSkill(Character character);
	
	protected SkillProperties createOvertimeSkillProperties() {
		SkillProperties p = new SkillProperties(this.properties);
		p.setPermanent(true);
		return p;
	}
	@Override
	public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
		int text = R.string.you_cast;
		if(resultCombat.getType().equals(CharacterType.ENEMY)) {
			text = R.string.enemy_cast;
		}
		return new ResultCombatText(R.color.reset, context.getString(text, getName()));
	}
	
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
