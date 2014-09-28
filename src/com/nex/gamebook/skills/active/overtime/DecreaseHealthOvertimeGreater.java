package com.nex.gamebook.skills.active.overtime;

import android.annotation.SuppressLint;
import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.SkillMap;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.CombatTextDispatcher;
import com.nex.gamebook.skills.ResultCombatText;
import com.nex.gamebook.skills.active.Skill;
import com.nex.gamebook.skills.active.SkillProperties;
import com.nex.gamebook.skills.active.conditional.DecreaseAttribute;

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
		Skill skill = createSkill(attacker);
		return ctx.getString(R.string.decrease_health_overtime_greater, skill.getDescription(ctx, attacker));
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm, boolean checkSummon) {
		Skill skill = createSkill(attacker);
		skill.setData(properties, skillName);
		skill.doAttack(attacker, attacked, callback, cm, checkSummon);
		return super.doAttackOnce(attacker, attacked, callback, cm, checkSummon);
	}
	private Skill createSkill(Character attacker) {
		if (proprietarySkill != null) {
			Skill propri = SkillMap.get(proprietarySkill);
//			propri.setCombatTextDispatcher(this);
			propri.setData(properties, skillName);
			return propri;
		}
		int skillvalue = getValue(attacker);
		DecreaseAttribute skill = new DecreaseAttribute(type, skillvalue);
		skill.setCombatTextDispatcher(new CombatTextDispatcher() {
			
			@Override
			public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
				return DecreaseHealthOvertimeGreater.super.getDefaultLogAttack(context, resultCombat);
			}
		});
		return skill;
	}
	@Override
	protected void redefineProperties(SkillProperties properties) {
		super.redefinePropertiesIfProprietarySkillExist(properties, proprietarySkill);
	}
	
	
	
	
}
