package com.nex.gamebook.skills.active;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.SkillMap;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.ResultCombatText;

public abstract class ActiveOvertimeSkill extends ActiveSkill {

	public ActiveOvertimeSkill(int constantValue) {
		super(constantValue);
	}


	@Override
	public StatType getType() {
		return StatType.HEALTH;
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		Skill skill = getTargetSkill(attacker);
		OvertimeSkill newSkill = new OvertimeSkill(skill, getOvertimeTurns());
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

	public abstract Skill getTargetSkill(Character character);
	
	protected SkillProperties createOvertimeSkillProperties() {
		SkillProperties p = new SkillProperties(this.properties);
		p.setPermanent(true);
		return p;
	}
	
	public ResultCombatText getLogAttackOvertime(Context context, ResultCombat resultCombat) {
		int text = R.string.you_applied;
		if(resultCombat.getType().equals(CharacterType.ENEMY)) {
			text = R.string.enemy_applied;
		}
		return new ResultCombatText(R.color.reset, context.getString(text, getName().toLowerCase()));
	}
	
	
}
