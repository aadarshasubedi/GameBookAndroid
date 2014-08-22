package com.nex.gamebook.attack.special;

import java.util.List;

import com.nex.gamebook.entity.Bonus;
import com.nex.gamebook.entity.Bonus.BonusType;
import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.CharacterType;
import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.playground.AttackCallback;

public abstract class AbstractSpecialConditionalSkill implements SpecialSkill {

	private static final long serialVersionUID = 1L;
	private int cycles = 0;
	

	
	
	@Override
	public void doAttack(Enemy enemy, Player player, AttackCallback callback) {
		int max = attemptsPerFight();
		if (max > 0 && cycles >= max)
			return;
		Character applicationChar = resolveCharacterForApplication(enemy, player);
		Character character = resolveCharacterForValue(enemy, player);
		Bonus bonus = createSpecialAttack(character);
		int value = applicationChar.getCurrentStats().getValueByBonusType(getType());
		if(value - bonus.getValue() <= getMinAttributeForStopAttack()) {
			return;
		}
		applicationChar.addBonus(bonus);
		if(!isPermanent()) {
			String conditionid = character.getSpecialSkill().getClass().getName() 
					+ "_" + character.getClass().getName();
			List<Bonus> tattacks = player.getConditions();
			Bonus b = player.findConditionById(conditionid);
			if (b==null) {
				b = createSpecialAttack(character);
				b.setConditionId(conditionid);
				b.setValue(bonus.getValue());
				b.setCoeff(bonus.getCoeff()*-1);
				tattacks.add(b);
			} else {
				b.setValue(b.getValue() + bonus.getValue());
			}
		}
		ResultCombat result = new ResultCombat();
		result.setSpecialAttack(this);
		result.setEnemyName(enemy.getName());
		result.setDamage(bonus.getValue());
		result.setType(getCharacterType());
		callback.attackCallBack(result);
		cycles++;
	}
	
	
	
	public abstract CharacterType getCharacterType();
	public abstract Character resolveCharacterForApplication(Enemy enemy, Player player);
	public abstract Character resolveCharacterForValue(Enemy enemy, Player player);
	public abstract int getValue(Character character);
	public abstract BonusType getType();
	public abstract boolean isCondition();
	public abstract int getMinAttributeForStopAttack();
	
	/**
	 * Return -1 if special attack is aplicable every turn.
	 * @return int
	 */
	public abstract int attemptsPerFight();
	
	private Bonus createSpecialAttack(Character character) {
		Bonus bonus = new Bonus();
		int value = getValue(character);
		bonus.setValue(value);
		bonus.setType(getType());
		bonus.setCoeff(isCondition()?-1:1);
		bonus.setPermanent(true);
		bonus.setSpecialAttack(true);
		bonus.setOverflowed(true);
		return bonus;
	}

	
	
	@Override
	public void clean() {
		cycles = 0;
	}
}
