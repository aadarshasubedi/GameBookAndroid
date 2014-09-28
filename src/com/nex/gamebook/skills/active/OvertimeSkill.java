package com.nex.gamebook.skills.active;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Cancelable;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.CombatTextDispatcher;
import com.nex.gamebook.skills.ResultCombatText;

public class OvertimeSkill implements CombatTextDispatcher, Cancelable {

	private Skill targetSkill;
	private CharacterType realTargetType;

	protected int turns;
	protected int currentTurns = 0;

	public OvertimeSkill(Skill targetSkill, int turns) {
		super();
		this.targetSkill = targetSkill;
		this.turns = turns;
	}

	public Skill getTargetSkill() {
		return targetSkill;
	}

	public int getTurns() {
		return turns;
	}

	public boolean execute(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat resultCombat) {

		targetSkill.setCombatTextDispatcher(this);
		if (targetSkill.isCondition())
			targetSkill.doAttack(attacked, attacker, callback, resultCombat, false);
		else
			targetSkill.doAttack(attacker, attacked, callback, resultCombat);

		targetSkill.cleanAfterBattleEnd();
		targetSkill.cleanAfterFightEnd();
		currentTurns++;
		if (currentTurns == turns)
			return false;
		return true;
	}

	public int getDamage() {
		return this.targetSkill.getConstantValue();
	}
	public int getReducedDamage(Character target) {
		return this.targetSkill.getReductedDamage(target, this.targetSkill.getConstantValue());
	}
	
	@Override
	public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
		Skill skill = resultCombat.getSpecialAttack();
		int color = R.color.positive;
		if (realTargetType!=null && CharacterType.SUMMON.equals(realTargetType)) {
			color = R.color.condition;
		} else if (CharacterType.ENEMY.equals(resultCombat.getType())) {
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

	public CharacterType getRealTargetType() {
		return realTargetType;
	}

	public void setRealTargetType(CharacterType realTargetType) {
		this.realTargetType = realTargetType;
	}

}
