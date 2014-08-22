package com.nex.gamebook.attack.special.player;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.combat.CombatProcess;
import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.ResultCombat;
import com.nex.gamebook.playground.AttackCallback;

public class TwiceAttack implements SpecialSkill {
	private static final long serialVersionUID = -1804300495308316474L;

	final int MAX_ATTACKS = 1;

	private int attacks = 1;

	@Override
	public void doAttack(Enemy enemy, Player player, AttackCallback callback) {
		if (attacks > MAX_ATTACKS)
			return;

		CombatProcess combat = new CombatProcess(enemy);
		ResultCombat result = combat.attack(player, enemy);
		result.setSpecialAttack(this);
		callback.attackCallBack(result);
		attacks++;
	}

	@Override
	public int getTextId() {
		return R.string.damage;
	}

	@Override
	public void clean() {
		attacks = 1;
	}

	@Override
	public int getDescriptionId() {
		return R.string.twice_attack_description;
	}

	@Override
	public int getNameId() {
		return R.string.twice_attack_name;
	}

	@Override
	public int getTypeId() {
		return R.string.special_skill_attack;
	}

	@Override
	public boolean isPermanent() {
		return true;
	}
	@Override
	public boolean isTriggerEnemy() {
		return false;
	}
}
