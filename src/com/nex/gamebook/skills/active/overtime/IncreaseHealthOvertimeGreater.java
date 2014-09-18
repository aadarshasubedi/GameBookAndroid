package com.nex.gamebook.skills.active.overtime;

import android.annotation.SuppressLint;
import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.SkillMap;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.CombatTextDispatcher;
import com.nex.gamebook.skills.ResultCombatText;
import com.nex.gamebook.skills.active.Skill;
import com.nex.gamebook.skills.active.SkillProperties;
import com.nex.gamebook.skills.active.conditional.IncreaseAttribute;

public class IncreaseHealthOvertimeGreater extends IncreaseHealthOvertime {

	private StatType type;
	private String proprietarySkill;

	public IncreaseHealthOvertimeGreater(StatType type) {
		this.type = type;
	}

	public IncreaseHealthOvertimeGreater(String proprietarySkill) {
		super();
		this.proprietarySkill = proprietarySkill;
	}

	@Override
	public String getDescription(Context ctx, Character attacker) {
		Skill skill = createSkill(attacker);
		return ctx.getString(R.string.increase_health_overtime_greater, skill.getDescription(ctx, attacker));
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		Skill skill = createSkill(attacker);
		skill.setData(properties, skillName);
		skill.doAttack(attacker, attacked, callback, cm);
		return super.doAttackOnce(attacker, attacked, callback, cm);
	}

	private Skill createSkill(Character attacker) {
		if (proprietarySkill != null) {
			Skill propri = SkillMap.get(proprietarySkill);
//			propri.setCombatTextDispatcher(this);
			propri.setData(properties, skillName);
			return propri;
		}
		int skillvalue = getValue(attacker);
		IncreaseAttribute skill = new IncreaseAttribute(type, skillvalue);
		skill.setCombatTextDispatcher(new CombatTextDispatcher() {
			
			@Override
			public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
				return IncreaseHealthOvertimeGreater.super.getLogAttack(context, resultCombat);
			}
		});
		return skill;
	}

	@Override
	protected void redefineProperties(SkillProperties properties) {
		super.redefinePropertiesIfProprietarySkillExist(properties, proprietarySkill);
	}
	@SuppressLint("ResourceAsColor")
	@Override
	public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
		ResultCombatText text = super.getLogAttackOvertime(context, resultCombat);
		text.setColor(R.color.reset);
		return text;
	}
}
