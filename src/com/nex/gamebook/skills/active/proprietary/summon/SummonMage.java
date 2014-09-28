package com.nex.gamebook.skills.active.proprietary.summon;

import android.content.Context;

import com.nex.gamebook.R;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.CharacterType;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.Stats;
import com.nex.gamebook.game.Summon;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.CombatTextDispatcher;
import com.nex.gamebook.skills.ResultCombatText;
import com.nex.gamebook.skills.active.SkillProperties;
import com.nex.gamebook.skills.active.SummoningSkill;
import com.nex.gamebook.skills.active.conditional.DecreaseAttribute;
import com.nex.gamebook.skills.passive.StrongSummon;

public class SummonMage extends SummoningSkill {
	public static String ID = "summon_mage";

	@Override
	public String getDescription(Context context, Character attacker) {
		return context.getString(R.string.summon_mage, getSummonHealth(attacker), getDamage(attacker));
	}

	public int getSummonHealth(Character character) {
		float coeff = getProperties().getCoeff();
		if (coeff == 0) {
			coeff = 1f;
		}
		return (int) (1 * character.getCurrentStats().getSpecialSkillPower() * coeff);
	}

	public int getDamage(Character attacker) {
		float coeff = getProperties().getCoeff();
		if (coeff == 0) {
			coeff = 1f;
		}
		return (int) (2 * coeff) * attacker.getCurrentStats().getSpecialSkillPower();
	}

	public Summon createSummon(Character character) {

		Summon summon = new Summon(character, getSummonHealth(character)) {
			@Override
			public String getName() {
				return SummonMage.this.getName();
			}

			@Override
			public void execute(Character attacker, Character attacked, BattleLogCallback callback) {
				int damage = getDamage(owner);

				StrongSummon passive = (StrongSummon) owner.findPassiveSkill(StrongSummon.class);
				if (passive != null) {
					damage += ((double) damage / 100) * passive.power(owner);
				}
				final boolean isCritical = isCriticalChance();
				if (isCritical) {
					damage *= getCriticalPower();
				}
				DecreaseAttribute dmgSkill = new DecreaseAttribute(StatType.HEALTH, damage);
				SkillProperties p = new SkillProperties();
				p.setPermanent(true);
				dmgSkill.setData(p, "");
				dmgSkill.setCombatTextDispatcher(new CombatTextDispatcher() {
					@Override
					public ResultCombatText getLogAttack(Context context, ResultCombat resultCombat) {
						int text = R.string.summon_cause;
						if (isCritical) {
							text = R.string.summon_cause_critical;
						}
						return new ResultCombatText(R.color.condition, context.getString(text, getName(), resultCombat.getDamage()));
					}
				});
				dmgSkill.doAttack(attacker, attacked, callback, null);
			}
		};
		Stats stats = new Stats(false);
		summon.setSummonStats(stats);
		return summon;
	}
}