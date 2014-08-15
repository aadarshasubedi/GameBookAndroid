package com.nex.gamebook.entity.special;

import com.nex.gamebook.R;
import com.nex.gamebook.entity.Bonus;
import com.nex.gamebook.entity.Bonus.BonusType;
import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.playground.AttackCallback;

public class MinionAttack implements SpecialAttack {

	@Override
	public void doAttack(Enemy enemy, Player player, AttackCallback callback) {
		Bonus bonus = new Bonus();
		bonus.setValue(1);
		bonus.setType(BonusType.HEALTH);
		bonus.setCoeff(-1);
		bonus.setPermanent(true);
		bonus.setOverflowed(true);
		player.addBonus(bonus);
		ResultCombat result = new ResultCombat();
		result.setSpecialAttack(this);
		result.setDamage(bonus.getValue());
		result.setEnemyName(enemy.getName());
		callback.attackCallBack(result);
	}

	@Override
	public int getAffectedAttributeId() {
		return R.string.attr_health;
	}

	

}
