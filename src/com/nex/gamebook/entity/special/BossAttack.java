package com.nex.gamebook.entity.special;

import java.util.List;

import com.nex.gamebook.R;
import com.nex.gamebook.entity.Bonus;
import com.nex.gamebook.entity.Bonus.BonusType;
import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.playground.AttackCallback;

public class BossAttack implements SpecialAttack {
	private final Integer MINIMUM_PLAYER_ATTACK = 4;

	@Override
	public void doAttack(Enemy enemy, Player player, AttackCallback callback) {
		if (player.getCurrentStats().getAttack() < MINIMUM_PLAYER_ATTACK) {
			return;
		}
		Bonus bonus = createSpecialAttack();
		player.addBonus(bonus);
		List<Bonus> attacks = player.getSpecialTempAttacks();
		if(attacks.isEmpty()) {
			Bonus b = createSpecialAttack();
			b.setCoeff(1);
			attacks.add(b);
		} else {
			Bonus b = attacks.get(0);
			b.setValue(b.getValue() + bonus.getValue());
		}
		ResultCombat result = new ResultCombat();
		result.setSpecialAttack(this);
		result.setEnemyName(enemy.getName());
		result.setDamage(bonus.getValue());
		callback.attackCallBack(result);
	}

	
	private Bonus createSpecialAttack() {
		Bonus bonus = new Bonus();
		bonus.setValue(1);
		bonus.setType(BonusType.ATTACK);
		bonus.setCoeff(-1);
		bonus.setPermanent(true);
		bonus.setSpecialAttack(true);
		bonus.setOverflowed(true);
		return bonus;
	}
	
	@Override
	public int getAffectedAttributeId() {
		return R.string.attr_attack;
	}
}
