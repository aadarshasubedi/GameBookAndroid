package com.nex.gamebook.attack.special;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.ActiveOvertimeSkill;
import com.nex.gamebook.game.Cancelable;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.playground.BattleLogCallback;

public abstract class SpecialCancelationSkill<E extends Cancelable> extends SpecialAttackSkill {

	public SpecialCancelationSkill() {
		super(NO_VALUE);
	}

	@Override
	public int getValue(Character character) {
		return NO_VALUE;
	}

	@Override
	public boolean isPermanent() {
		return false;
	}
	@Override
	public StatType getType() {
		return null;
	}

	public abstract boolean isCancelPositive();

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		int totalcanceledSkills = getSumOfCanceledSkills(attacker);
		Character character = resolveCharacterForApplyingOT(attacker, attacked);
		int currentCanceled = 0;
		List<E> canceledList = new ArrayList<>();
		Set<E> cancelabelsSkills = getCancelablesSkills(character);
		for (E s : cancelabelsSkills) {
			if (currentCanceled<totalcanceledSkills && (isCancelPositive() && !s.isNegative()) || (!isCancelPositive() && s.isNegative())) {
				canceledList.add(s);
				currentCanceled++;
				break;
			}
		}
		if (!canceledList.isEmpty()) {
			for(Cancelable c: canceledList) {
				boolean removed = cancelabelsSkills.remove(c);
				if (removed) {
					ResultCombat rc = createBasicResult(0, attacker.getType(), resolveEnemy(attacker, attacked));
					callback.logAttack(rc);
				}
			}
			
		}
		return false;
	}
	
	public abstract Set<E> getCancelablesSkills(Character character);
	
	public Character resolveCharacterForApplyingOT(Character attacker, Character attacked) {
		if(isCancelPositive()) {
			return attacked;
		} else {
			return attacker;
		}
	}
	
	@Override
	public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
		return new ResultCombatText(R.color.reset, getCanceledText(context, resultCombat.getType()));
	}
	
	public abstract String getCanceledText(Context ctx, CharacterType type);
	
	@Override
	public boolean isTriggerAfterEnemyAttack() {
		return true;
	}

	@Override
	public int attemptsPerFight() {
		return 1;
	}

	@Override
	public boolean causeDamage() {
		return false;
	}
	
	public int getSumOfCanceledSkills(Character attacker) {
		int totalcanceledSkills = getValue(attacker) / 2;
		if(totalcanceledSkills == 0) {
			totalcanceledSkills = 1;
		}
		return totalcanceledSkills;
	}
	
}
