package com.nex.gamebook.attack.special;

import com.nex.gamebook.game.ActiveOvertimeSkill;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.playground.BattleLogCallback;

public abstract class SpecialCancelationSkill extends SpecialAttackSkill {
	@Override
	public int getValue(Character character) {
		return -1;
	}

	@Override
	public int getValueWhenLuck(Character character) {
		return getValue(character);
	}

	@Override
	public boolean isPermanent() {
		return false;
	}

	@Override
	public int getAspectId() {
		return NO_ASPECT;
	}

	@Override
	public boolean showPercentage() {
		return false;
	}

	@Override
	public StatType getType() {
		return null;
	}

	public abstract boolean isCancelPositive();

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		ActiveOvertimeSkill releasedSkill = null;
		for (ActiveOvertimeSkill s : attacked.getOvertimeSkills()) {
			if ((isCancelPositive() && !s.getTargetSkill().isDebuff()) || (!isCancelPositive() && s.getTargetSkill().isDebuff())) {
				releasedSkill = s;
				break;
			}
		}
		if (releasedSkill != null) {
			boolean removed = attacked.getOvertimeSkills().remove(releasedSkill);
			if (removed) {
				ResultCombat rc = createBasicResult(0, attacker.getType(), resolveEnemy(attacker, attacked));
				callback.logAttack(rc);
			}
		}
		return false;
	}

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
}
