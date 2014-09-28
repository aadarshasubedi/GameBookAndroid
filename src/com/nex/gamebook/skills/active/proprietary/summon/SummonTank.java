package com.nex.gamebook.skills.active.proprietary.summon;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Stats;
import com.nex.gamebook.game.Summon;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.CombatTextDispatcher;
import com.nex.gamebook.skills.ResultCombatText;
import com.nex.gamebook.skills.active.SummoningSkill;

public class SummonTank extends SummoningSkill {
	public static String ID = "summon_tank";

	@Override
	public String getDescription(Context context, Character attacker) {
		return context.getString(R.string.summon_tank, getSummonHealth(attacker), getSummonDefaultDefense(attacker), "%");
	}

	public int getSummonHealth(Character character) {
		float coeff = getProperties().getCoeff();
		if (coeff == 0) {
			coeff = 1f;
		}
		return (int) (8 * character.getCurrentStats().getSkillpower() * coeff);
	}

	public int getSummonDefaultDefense(Character character) {
		return  30;
	}
	
	public abstract class TankSummon extends Summon {

		public TankSummon(Character owner, int basehealth) {
			super(owner, basehealth);
		}
		
	}
	
	public Summon createSummon(Character character) {

		TankSummon summon = new TankSummon(character, getSummonHealth(character)) {
			@Override
			public String getName() {
				return SummonTank.this.getName();
			}

			@Override
			public void execute(Character attacker, Character attacked, BattleLogCallback callback) {
				ResultCombat rc = createBasicResult(0, CharacterType.SUMMON, resolveEnemy(attacker, attacked));
//				rc.setCritical(isCritical);
				CombatTextDispatcher dispatcher = SummonTank.this;
				rc.getSpecialAttack().setCombatTextDispatcher(new CombatTextDispatcher() {
					@Override
					public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
						int text = R.string.summon_defending;
						return new ResultCombatText(R.color.condition, context.getString(text, getName()));
					}
				});
				callback.logAttack(rc);
				rc.getSpecialAttack().setCombatTextDispatcher(dispatcher);
			}
		};
		Stats stats = new Stats(false);
		stats.setDefense(Stats.getValuePerc(Stats.TOTAL_ARMOR_FOR_CALC, getSummonDefaultDefense(character)));
		summon.setSummonStats(stats);
		return summon;
	}
}