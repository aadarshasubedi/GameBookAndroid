package com.nex.gamebook.attack.special.skill.overtime;

import android.annotation.SuppressLint;
import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.ResultCombatText;
import com.nex.gamebook.attack.special.SkillProperties;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.skill.conditional.IncreaseAttribute;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.SpecialSkillsMap;
import com.nex.gamebook.playground.BattleLogCallback;

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
		SpecialSkill skill = createSkill(attacker);
		return ctx.getString(R.string.increase_health_overtime_greater, skill.getDescription(ctx, attacker));
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		SpecialSkill skill = createSkill(attacker);
		skill.setCombatTextDispatcher(this);
		
		skill.doAttack(attacker, attacked, callback, cm);
		return super.doAttackOnce(attacker, attacked, callback, cm);
	}

	private SpecialSkill createSkill(Character attacker) {
		if (proprietarySkill != null) {
			SpecialSkill propri = SpecialSkillsMap.get(proprietarySkill);
			propri.setData(properties, skillName);
			return propri;
		}
		int skillvalue = getValue(attacker);
		IncreaseAttribute skill = new IncreaseAttribute(type, skillvalue);
		return skill;
	}

	@Override
	protected void redefineProperties(SkillProperties properties) {
		super.redefinePropertiesIfProprietarySkillExist(properties, proprietarySkill);
	}
	@SuppressLint("ResourceAsColor")
	@Override
	public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
		ResultCombatText text = super.getLogAttack(context, resultCombat);
		text.setColor(R.color.reset);
		return text;
	}
}
