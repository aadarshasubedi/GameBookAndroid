package com.nex.gamebook.attack.special.skill.overtime;

import android.annotation.SuppressLint;
import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.ResultCombatText;
import com.nex.gamebook.attack.special.SkillProperties;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.attack.special.skill.conditional.DecreaseAttribute;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.SpecialSkillsMap;
import com.nex.gamebook.playground.BattleLogCallback;

public class DecreaseHealthOvertimeGreater extends DecreaseHealthOvertime {

	private StatType type;
	private String proprietarySkill = null;

	public DecreaseHealthOvertimeGreater(StatType type) {
		this.type = type;
	}

	public DecreaseHealthOvertimeGreater(String proprietarySkill) {
		super();
		this.proprietarySkill = proprietarySkill;
	}

	@Override
	public String getDescription(Context ctx, Character attacker) {
		SpecialSkill skill = createSkill(attacker);
		return ctx.getString(R.string.decrease_health_overtime_greater, skill.getDescription(ctx, attacker));
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		SpecialSkill skill = createSkill(attacker);
		skill.setCombatTextDispatcher(this);
		skill.setData(properties, skillName);
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
		DecreaseAttribute skill = new DecreaseAttribute(type, skillvalue);
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
