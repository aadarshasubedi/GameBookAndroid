package com.nex.gamebook.entity.character;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Random;

import android.util.Log;

import com.nex.gamebook.entity.Story;
import com.nex.gamebook.story.Bonus;
import com.nex.gamebook.story.StorySectionOption;

public class Character implements Serializable {
	public static int MAX_LUCK_OF_CHARACTER = 14;
	
	public static int TOTAL_LUCK_FOR_CALC = 20;
	private int id;
	private int name;
	private int description;
	private int position;

	private Story story;
	private Stats stats = new Stats();
	private Stats currentStats = new Stats(stats);

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

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public Stats getCurrentStats() {
		return currentStats;
	}

	public void setCurrentStats(Stats currentStats) {
		this.currentStats = currentStats;
	}
	
	public void setCanShowOption(StorySectionOption option) {
		if(option.isAlwaysDisplayed()) {
			option.setDisplayed(true);
			return;
		}
		boolean both = option.isBothAspects();
		boolean isLuck = (option.isLuckAspect() && hasLuck()) || option.isAlreadyDisplayed();
		boolean hasSkill = option.getSkill() > 0 && currentStats.getSkill() >= option.getSkill();
		
		option.setDisplayed(both? isLuck && hasSkill : isLuck || hasSkill);
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

	public boolean isDefeated() {
		return getCurrentStats().getHealth() <= 0;
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
			Method currentAttr = Stats.class.getDeclaredMethod(createMethodName("get", bonus.getType().name().toLowerCase()), new Class[0]);
			Method defaultAttr = Stats.class.getDeclaredMethod(createMethodName("get", bonus.getType().name().toLowerCase()), new Class[0]);
			int currentValue = (int) currentAttr.invoke(this.currentStats, new Object[0]);
			int total = currentValue + (bonus.getCoeff() * bonus.getValue());
			int defaultValue = (int) defaultAttr.invoke(this.stats, new Object[0]);
			currentAttr = Stats.class.getDeclaredMethod(createMethodName("set", bonus.getType().name().toLowerCase()), int.class);
			defaultAttr = Stats.class.getDeclaredMethod(createMethodName("set", bonus.getType().name().toLowerCase()), int.class);
			if (total > defaultValue && !bonus.isOverflowed()) {
				realValue = defaultValue - currentValue;
				currentAttr.invoke(this.currentStats, defaultValue);
			} else {
				currentAttr.invoke(this.currentStats, total);
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
	
	public boolean hasLuck() {
		Random random = new Random();
		int res = random.nextInt(TOTAL_LUCK_FOR_CALC);
		return getCurrentStats().getLuck() >= res;
	}

}
