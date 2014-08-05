package com.nex.gamebook.entity;

import java.lang.reflect.Method;

import com.nex.gamebook.story.section.StorySection;

import android.util.Log;

public class Player extends Character {

	private static final long serialVersionUID = 7279750413253963361L;
	private int id;
	private int name;
	private int description;
	private int position;

	private Story story;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Story getStory() {
		return story;
	}

	public void setStory(Story story) {
		this.story = story;
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

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getDescription() {
		return description;
	}

	public void setDescription(int description) {
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
					createMethodName("get", bonus.getType().name()
							.toLowerCase()), new Class[0]);
			Method defaultAttr = Stats.class.getDeclaredMethod(
					createMethodName("get", bonus.getType().name()
							.toLowerCase()), new Class[0]);
			int currentValue = (int) currentAttr.invoke(getCurrentStats(),
					new Object[0]);
			int total = currentValue + (bonus.getCoeff() * bonus.getValue());
			int defaultValue = (int) defaultAttr.invoke(getStats(),
					new Object[0]);
			currentAttr = Stats.class.getDeclaredMethod(
					createMethodName("set", bonus.getType().name()
							.toLowerCase()), int.class);
			defaultAttr = Stats.class.getDeclaredMethod(
					createMethodName("set", bonus.getType().name()
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

	private String createMethodName(String type, String fieldName) {
		String firstPart = fieldName.substring(0, 1).toUpperCase();
		String secondPart = fieldName.substring(1);
		return type + firstPart + secondPart;
	}

	@Override
	public CharacterType getType() {
		return CharacterType.PLAYER;
	}

	public StorySection getCurrentSection() {
		return getStory().getSection(this.position);
	}
	
}
