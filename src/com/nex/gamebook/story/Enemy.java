package com.nex.gamebook.story;

import com.nex.gamebook.R;
import com.nex.gamebook.entity.character.Character;

public class Enemy {

	private int attack;
	private int skill;
	private int name;
	private int withoutDamageText = R.string.fight_withoutdamage;
	private int withDamageText = R.string.fight_withdamage;
	private int skillText = R.string.fight_skill;
	private int luckText = R.string.fight_luck;
	private int loseText = R.string.fight_lose;

	private int resultText = R.string.fight_error;
	private int resultAttrText = R.string.fight_error_attr;

	private boolean defeated;
	private boolean affectPlayer;

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public boolean isDefeated() {
		return defeated;
	}

	public void setDefeated(boolean defeated) {
		this.defeated = defeated;
	}

	public int getWithoutDamageText() {
		return withoutDamageText;
	}

	public void setWithoutDamageText(int withoutDamageText) {
		this.withoutDamageText = withoutDamageText;
	}

	public int getWithDamageText() {
		return withDamageText;
	}

	public void setWithDamageText(int withDamageText) {
		this.withDamageText = withDamageText;
	}

	public int getSkillText() {
		return skillText;
	}

	public void setSkillText(int skillText) {
		this.skillText = skillText;
	}

	public int getLoseText() {
		return loseText;
	}

	public void setLoseText(int loseText) {
		this.loseText = loseText;
	}

	public int getLuckText() {
		return luckText;
	}

	public void setLuckText(int luckText) {
		this.luckText = luckText;
	}

	public int getResultText() {
		return resultText;
	}

	public int getResultAttrText() {
		return resultAttrText;
	}

	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public void fight(Character character) {
		if (attack <= character.getCurrentStats().getDefense()) {
			resultText = withoutDamageText;
			resultAttrText = R.string.fight_aspect_defense;
		} else if (skill < character.getCurrentStats().getSkill()) {
			resultText = skillText;
			resultAttrText = R.string.fight_aspect_skill;
		} else if (character.hasLuck()) {
			resultText = luckText;
			resultAttrText = R.string.fight_aspect_luck;
		} else {
			int def = character.getCurrentStats().getDefense();
			int health = character.getCurrentStats().getHealth();
			character.getCurrentStats().setHealth(health - (attack - def));
			resultText = withDamageText;
			resultAttrText = R.string.fight_aspect_health;
			affectPlayer = true;
		}
		if (character.isDefeated()) {
			resultText = loseText;
			resultAttrText = R.string.fight_aspect_health;
		}
	}

	public boolean isAffectPlayer() {
		return affectPlayer;
	}

	public void setAffectPlayer(boolean affectPlayer) {
		this.affectPlayer = affectPlayer;
	}

}
