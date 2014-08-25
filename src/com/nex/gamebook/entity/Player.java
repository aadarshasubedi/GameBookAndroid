package com.nex.gamebook.entity;

import java.util.ArrayList;
import java.util.List;

import com.nex.gamebook.entity.io.GameBookUtils;
import com.nex.gamebook.playground.AttackCallback;

public class Player extends Character {

	private static final long serialVersionUID = 7279750413253963361L;
	private int id;
	private String name;
	private String description;
	private int position;
	private int sections;
	private int visitedSections;

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
		if (health > 0)
			score += health;
		int attack = getCurrentStats().getAttack() - getStats().getAttack();
		if (attack > 0)
			score += attack;
		int defense = getCurrentStats().getDefense() - getStats().getDefense();
		if (defense > 0)
			score += defense;
		int skill = getCurrentStats().getSkill() - getStats().getSkill();
		if (skill > 0)
			score += skill;
		int luck = getCurrentStats().getLuck() - getStats().getLuck();
		if (luck > 0)
			score += luck;
		int sectionsMultiplier = getSections() - getVisitedSections();
		if (sectionsMultiplier == 0)
			sectionsMultiplier = 1;
		score += getSections() * sectionsMultiplier;
		float multiplier = getStory().getSection(position).getScoreMultiplier();
		if (multiplier > 1f) {
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

	public void addExperience(AttackCallback callback, long exp) {
		setExperience(getExperience()+exp);
		long requiredExperience = ExperienceMap.getInstance().getExperienceByLevel(getLevel());
		while(getExperience()> requiredExperience) {
			setLevel(getLevel() + 1);
			requiredExperience = ExperienceMap.getInstance().getExperienceByLevel(getLevel());
			callback.logLevelIncreased();
			ExperienceMap.getInstance().updateStatsByLevel(this);
		}		
	}
	
}
