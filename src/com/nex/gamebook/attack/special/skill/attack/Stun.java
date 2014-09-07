package com.nex.gamebook.attack.special.skill.attack;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.ResultCombatText;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;

public class Stun extends SpecialAttackSkill {

	public Stun() {
		super(NO_VALUE);
	}

	@Override
	public String getDescription(Context ctx, Character attacker) {
		return ctx.getString(R.string.stun);
	}

	@Override
	public StatType getType() {
		return null;
	}

	@Override
	public boolean isCondition() {
		return false;
	}
	
	@Override
	public boolean isTriggerAfterEnemyAttack() {
		return true;
	}

	@Override
	public int getValue(Character character) {
		return NO_VALUE;
	}
	
	@Override
	public boolean doAttackOnce(Character attacker, Character attacked, BattleLogCallback callback, ResultCombat cm) {
		attacked.setCanAttack(false);
		attacked.setCanCastSkill(false);
		ResultCombat c = createBasicResult(0, attacker.getType(), resolveEnemy(attacker, attacked));
		callback.logAttack(c);
		return true;
	}

	@Override
	public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
		int text = R.string.you_cast;
		if(resultCombat.getType().equals(CharacterType.ENEMY))
		text = R.string.enemy_cast;
		return new ResultCombatText(R.color.reset, context.getString(text, resultCombat.getSpecialAttack().getName()));
	}
	
}
