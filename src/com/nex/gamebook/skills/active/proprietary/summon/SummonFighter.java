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
import com.nex.gamebook.skills.passive.StrongSummon;

public class SummonFighter extends SummoningSkill {
	public static String ID = "summon_fighter";

	@Override
	public String getDescription(Context context, Character attacker) {
		return context.getString(R.string.summon_attacker, getSummonHealth(attacker), this.getDamage(attacker), "%");
	}

	public int getSummonHealth(Character character) {
		float coeff = getProperties().getCoeff();
		if (coeff == 0) {
			coeff = 1f;
		}
		return (int) (3 * character.getCurrentStats().getSkillpower() * coeff);
	}

	public int getDamage(Character attacker) {
		return calcDynamicValue(20, attacker.getCurrentStats().getSpecialSkillPower(), attacker);
	}
	
	public Summon createSummon(Character character) {

		Summon summon = new Summon(character, getSummonHealth(character)) {
			@Override
			public String getName() {
				return SummonFighter.this.getName();
			}

			@Override
			public void execute(Character attacker, Character attacked, BattleLogCallback callback) {
				int damage = (int) (((double) owner.getStats().getTotalPureDamage() / 100) * SummonFighter.this.getDamage(attacker));
				StrongSummon passive = (StrongSummon) owner.findPassiveSkill(StrongSummon.class);
				if (passive != null) {
					damage += ((double) damage / 100) * passive.power(owner);
				}
				final boolean isCritical = isCriticalChance();
				if (isCritical) {
					damage *= getCriticalPower();
				}

				attacked.damage(damage);
				ResultCombat rc = createBasicResult(damage, CharacterType.SUMMON, resolveEnemy(attacker, attacked));
//				rc.setCritical(isCritical);
				CombatTextDispatcher dispatcher = SummonFighter.this;
				rc.getSpecialAttack().setCombatTextDispatcher(new CombatTextDispatcher() {
					@Override
					public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
						int text = R.string.summon_cause;
						if (isCritical) {
							text = R.string.summon_cause_critical;
						}
						return new ResultCombatText(R.color.condition, context.getString(text, getName(), resultCombat.getDamage()));
					}
				});
				callback.logAttack(rc);
				rc.getSpecialAttack().setCombatTextDispatcher(dispatcher);
			}
		};
		Stats stats = new Stats(false);
		summon.setSummonStats(stats);
		return summon;
	}
}
