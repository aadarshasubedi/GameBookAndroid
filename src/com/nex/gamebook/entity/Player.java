package com.nex.gamebook.entity;

import java.lang.reflect.Method;

import com.nex.gamebook.entity.io.GameBookUtils;

import android.util.Log;

public class Player extends Character {

	private static final long serialVersionUID = 7279750413253963361L;
	private int id;
	private String name;
	private String description;
	private int position;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCanShowOption(StorySectionOption option) {
		if (option.isAlwaysDisplayed()) {
			option.setDisplayed(true);
			return;
		}
		boolean both = option.isBothAspects();
		boolean isLuck = (option.isLuckAspect() && hasLuck())
				|| option.isAlreadyDisplayed();
		boolean hasSkill = option.getSkill() > 0
				&& getCurrentStats().getSkill() >= option.getSkill();

		option.setDisplayed(both ? isLuck && hasSkill : isLuck || hasSkill);
	}

	public String getName() {
		return GameBookUtils.getInstance().getText(name, getStory());
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return GameBookUtils.getInstance().getText(description, getStory());
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Returns real added value
	 * 
	 * @param bonus
	 * @return
	 */
	public int addBonus(Bonus bonus) {
		int realValue = bonus.getValue();
		try {
			Method currentAttr = Stats.class.getDeclaredMethod(
					GameBookUtils.createMethodName("get", bonus.getType().name().toLowerCase()), new Class[0]);
			Method defaultAttr = Stats.class.getDeclaredMethod(
					GameBookUtils.createMethodName("get", bonus.getType().name().toLowerCase()), new Class[0]);
			int currentValue = (int) currentAttr.invoke(getCurrentStats(),
					new Object[0]);
			int total = currentValue + (bonus.getCoeff() * bonus.getValue());
			int defaultValue = (int) defaultAttr.invoke(getStats(),
					new Object[0]);
			currentAttr = Stats.class.getDeclaredMethod(
					GameBookUtils.createMethodName("set", bonus.getType().name()
							.toLowerCase()), int.class);
			defaultAttr = Stats.class.getDeclaredMethod(
					GameBookUtils.createMethodName("set", bonus.getType().name()
							.toLowerCase()), int.class);
			if (total > defaultValue && !bonus.isOverflowed()) {
				realValue = defaultValue - currentValue;
				currentAttr.invoke(getCurrentStats(), defaultValue);
			} else {
				currentAttr.invoke(getCurrentStats(), total);
			}
		} catch (Exception e) {
			Log.e("GameBook", "", e);
		}
		return realValue;
	}

	

	@Override
	public CharacterType getType() {
		return CharacterType.PLAYER;
	}

	public StorySection getCurrentSection() {
		return getStory().getSection(this.position);
	}

	public int getScore() {
		int score = 0;
		int health = getCurrentStats().getHealth() - getStats().getHealth();
		if(health>0)score+=health;
		int attack = getCurrentStats().getAttack() - getStats().getAttack();
		if(attack>0)score+=attack;
		int defense = getCurrentStats().getDefense() - getStats().getDefense();
		if(defense>0)score+=defense;
		int skill = getCurrentStats().getSkill() - getStats().getSkill();
		if(skill>0)score+=skill;
		int luck = getCurrentStats().getLuck() - getStats().getLuck();
		if(luck>0)score+=luck;
		int sectionsMultiplier = getSections() - getVisitedSections();
		if(sectionsMultiplier == 0) sectionsMultiplier = 1;
		score += getSections() * sectionsMultiplier;
		float multiplier = getStory().getSection(position).getScoreMultiplier();
		if(multiplier>1f) {
			score *= multiplier;
		}
		return score;
	}
}
