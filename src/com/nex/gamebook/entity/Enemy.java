package com.nex.gamebook.entity;

import com.nex.gamebook.playground.AttackCallback;

public class Enemy extends com.nex.gamebook.entity.Character {

	private static final long serialVersionUID = -1969817378614053107L;
	private int name;
	private int index;
	private boolean affectPlayer;

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	private ResultCombat attack(Character attackChar,
			Character attackedCharacter) {
		ResultCombat resultCombat = new ResultCombat();
		resultCombat.setType(attackChar.getType());
		resultCombat.setLuck(attackedCharacter.hasLuck());
		if (!resultCombat.isLuck()) {
			resultCombat.setCritical(attackChar.isCriticalChance());
			int attack = attackChar.getCurrentStats().getAttack();
			if (resultCombat.isCritical()) {
				double criticalMultiplier = attackChar.hasLuck() ? 1 : 0.5;
				resultCombat.setMultiply(criticalMultiplier);
				attack += attack * criticalMultiplier;
			}
			resultCombat.setDamage(attack
					- attackedCharacter.getCurrentStats().getDefense());
			int attackedHealth = attackedCharacter.getCurrentStats()
					.getHealth();
			attackedCharacter.getCurrentStats().setHealth(
					attackedHealth - resultCombat.getDamage());
		}
		resultCombat.setEnemyName(getName());
		return resultCombat;
	}

	public void fight(AttackCallback callback) {
		Character character = callback.getCharacter();
		boolean enemyBegin = !character.hasLuck();

		while (getCurrentStats().getHealth() > 0) {

			if (enemyBegin) {
				callback.attackCallBack(attack(this, character));
			} else {
				callback.attackCallBack(attack(character, this));
			}
			enemyBegin = !enemyBegin;
			if (character.isDefeated()) {
				break;
			}
		}
		callback.fightEnd();
	}

	public boolean isAffectPlayer() {
		return affectPlayer;
	}

	public void setAffectPlayer(boolean affectPlayer) {
		this.affectPlayer = affectPlayer;
	}

	@Override
	public CharacterType getType() {
		return CharacterType.ENEMY;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
