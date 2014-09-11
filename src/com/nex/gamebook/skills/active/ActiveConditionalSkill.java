package com.nex.gamebook.skills.active;

import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Stats;
import com.nex.gamebook.playground.BattleLogCallback;

public abstract class ActiveConditionalSkill extends ActiveSkill {

	public ActiveConditionalSkill(int constantValue) {
		super(constantValue);
	}

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		Character applicationChar = resolveCharacterForApplication(attacked, attacker);
		Bonus bonus = createReductedBonus(attacker, applicationChar);
		if (!isPermanent()) {
			applicationChar.getConditions().add(bonus);
		} else {
			if(bonus.getType().equals(StatType.HEALTH)) {
				if(isCondition()) {
					attacker.getStatistics().addSkillGivenDamage(bonus.getValue());
					attacked.getStatistics().addSkillTakenDamage(bonus.getValue());
				} else {
					attacker.getStatistics().addHealedHealth(bonus.getValue());
				}
			}
			int realValue = applicationChar.addBonus(bonus);
			bonus.setValue(realValue);
		}
		ResultCombat result = createBasicResult(bonus.getValue(), attacker.getType(), resolveEnemy(attacker, attacked));
		callback.logAttack(result);
		return true;
	}

	public abstract boolean isCondition();

	public int getMinAttributeForStopAttack() {
		if(properties.getType().equals(StatType.ATTACK)) {
			return 1;
		}
		return -1;
	}

	public Character resolveCharacterForApplication(Character attacked, Character attacker) {
		if (isCondition()) {
			return attacked;
		}
		return attacker;
	}


	@Override
	public boolean afterNormalAttack() {
		return false;
	}

	@Override
	public boolean doSomething(Character attacked, Character attacker) {
		if(isCondition()) {
			Player test = new Player();
			test.setCurrentStats(new Stats(attacked.getCurrentStats(), false));
			test.setStats(new Stats(attacked.getCurrentStats(), true));
			Bonus bonus = createSpecialAttack(isCondition() ? -1 : 1, getValue(attacker), getType());
			int realValue = test.addBonus(bonus);
			if(realValue==0) return false;
		}
		return true;
	}
}
