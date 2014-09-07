package com.nex.gamebook.game;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.CombatTextDispatcher;
import com.nex.gamebook.attack.special.ResultCombatText;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.playground.BattleLogCallback;

public class ActiveOvertimeSkill implements CombatTextDispatcher, Cancelable {

	private SpecialSkill targetSkill;

	private int turns;
	private int currentTurns = 0;

	public ActiveOvertimeSkill(SpecialSkill targetSkill, int turns) {
		super();
		this.targetSkill = targetSkill;
		this.turns = turns;
	}

	public SpecialSkill getTargetSkill() {
		return targetSkill;
	}

	public int getTurns() {
		return turns;
	}

	public boolean execute(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat resultCombat) {

		targetSkill.setCombatTextDispatcher(this);
		if(targetSkill.isCondition())
			targetSkill.doAttack(attacked, attacker, callback, resultCombat);
		else 
			targetSkill.doAttack(attacker, attacked, callback, resultCombat);
			
		targetSkill.cleanAfterBattleEnd();
		targetSkill.cleanAfterFightEnd();
		currentTurns++;
		if (currentTurns == turns)
			return false;
		return true;
	}

	@Override
	public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
		SpecialSkill skill = resultCombat.getSpecialAttack();
		int color = R.color.positive;
		if (CharacterType.ENEMY.equals(resultCombat.getType())) {
			color = R.color.negative;
		}
		String text = resultCombat.getSpecialAttack().getName();
		text += " " + context.getString(R.string.overtime_works);
		if (skill.causeDamage()) {
			text += " " + context.getString(R.string.for_word);
			text += " " + resultCombat.getDamage();
		}
		text += " " + context.getString(skill.getType().getText()).toLowerCase();
		String suff = "(+)";
		if (skill.isCondition()) {
			suff = "(-)";
		}
		text += " " + suff;
		return new ResultCombatText(color, text);
	}

	public int getCurrentTurns() {
		return currentTurns;
	}
	
	public int getRemainsTurns() {
		return turns - currentTurns;
	}

	@Override
	public boolean isNegative() {
		return targetSkill.isCondition();
	}
	
}

