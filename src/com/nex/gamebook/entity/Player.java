package com.nex.gamebook.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import android.util.Log;

import com.nex.gamebook.entity.io.GameBookUtils;

public class Player extends Character {

	private static final long serialVersionUID = 7279750413253963361L;
	private int id;
	private String name;
	private String description;
	private int position;
	private int sections;
	private int visitedSections;
	private transient Stats temporalStatsHolder;
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
			
			int currentValue = (int) currentAttr.invoke(getCurrentStats(), new Object[0]);
			int total = currentValue + (bonus.getCoeff() * bonus.getValue());
			int defaultValue = (int) defaultAttr.invoke(getStats(), new Object[0]);
			currentAttr = Stats.class.getDeclaredMethod(
					GameBookUtils.createMethodName("set", bonus.getType().name().toLowerCase()), int.class);
			int setedValue = 0;
			if (total > defaultValue && !bonus.isOverflowed()) {
				realValue = defaultValue - currentValue;
				setedValue = (int)currentAttr.invoke(getCurrentStats(), defaultValue);
			} else {
				setedValue = (int)currentAttr.invoke(getCurrentStats(), total);
			}
			if((currentValue + realValue) != setedValue) {
				realValue = setedValue - currentValue;
			}
			if(!bonus.isPermanent()) {
				if(this.temporalStatsHolder == null) {
					this.temporalStatsHolder = new Stats();
					
					this.temporalStatsHolder.setDamage(0);
				}
				Field tempAttr = Stats.class.getDeclaredField(bonus.getType().name().toLowerCase());
				tempAttr.setAccessible(true);
				tempAttr.set(this.temporalStatsHolder, realValue);
				tempAttr.setAccessible(false);
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
	
	public int getSections() {
		return sections;
	}

	public void setSections(int sections) {
		this.sections = sections;
	}

	public int getVisitedSections() {
		return visitedSections;
	}

	public void setVisitedSections(int visitedSections) {
		this.visitedSections = visitedSections;
	}

	public void addSection() {
		this.sections++;
	}

	public void addVisitedSection() {
		this.visitedSections++;
	}
	
	public Stats getTemporalStatsHolder() {
		return temporalStatsHolder;
	}
	public void holdCurrentStatsToTemporal() {
		if(this.temporalStatsHolder!=null) {
			this.temporalStatsHolder.setHolder(new Stats(getCurrentStats()));
		}
	}
	public Stats releaseTemporalStats() {
		if(this.temporalStatsHolder == null) return null;
		getCurrentStats().releaseTemporalStats(this.temporalStatsHolder);
		Stats releasedStats = new Stats(this.temporalStatsHolder);
		this.temporalStatsHolder = null;
		return releasedStats;
	}
}
