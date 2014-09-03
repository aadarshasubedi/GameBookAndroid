package com.nex.gamebook.attack.special.skill;

import java.util.ArrayList;
import java.util.List;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.game.SpecialSkillsMap;
import com.nex.gamebook.game.Stats;
import com.nex.gamebook.playground.BattleLogCallback;
import static com.nex.gamebook.game.SpecialSkillsMap.*;
public class LethalStrike extends SpecialAttackSkill {

	private static final long serialVersionUID = 3670504540933316785L;

	@Override
	public boolean doAttackOnce(Character attacker, Character attacked,
			BattleLogCallback callback, ResultCombat resultCombat) {
		ResultCombat result = createBasicResult(0, attacker.getType(), resolveEnemy(attacker, attacked));
		int modificator = getModificator(attacker);
		if(attacker.hasLuck()) {
			modificator = getModificatorWhenLuck(attacker);
		}
		if (attacker.hasLuck(modificator)) {
			int damage = attacked.getCurrentStats().getHealth() - 1;
			result.setDamage(damage);
			Bonus bonus = createSpecialAttack(-1, damage, getType());
			attacked.addBonus(bonus);
			addTemporalBonus(attacked, bonus, createConditionId(attacker));
		}
		callback.logAttack(result);
		if(result.getDamage()==0)
			return true;
		return false;
	}

	private int getModificator(Character c) {
		return (int) (Stats.TOTAL_LUCK_FOR_CALC + c.getLevel() - c.getCurrentStats().getSpecialSkillPower());
	}
	
	private int getModificatorWhenLuck(Character c) {
		return (int) (getModificator(c) * 0.9);
	}
	
	@Override
	public int getTextId() {
		return R.string.attr_health;
	}

	@Override
	public int getDescriptionId() {
		return R.string.lethal_strike_description;
	}

	@Override
	public int getNameId() {
		return R.string.lethal_strike_name;
	}

	@Override
	public int getTypeId() {
		return R.string.special_skill_type_attack;
	}

	@Override
	public int getValue(Character character) {
		return Stats.getPercentage(character.getCurrentStats().getLuck(), getModificator(character));
	}

	@Override
	public int getValueWhenLuck(Character character) {
		return Stats.getPercentage(character.getCurrentStats().getLuck(), getModificatorWhenLuck(character));
	}

	@Override
	public boolean isPermanent() {
		return false;
	}

	@Override
	public boolean showPercentage() {
		return true;
	}
	
	@Override
	public int getAspectId() {
		return R.string.special_skill_aspect_chance;
	}
	@Override
	public boolean isDebuff() {
		return true;
	}
	
	@Override
	public StatType getType() {
		return StatType.HEALTH;
	}

}
