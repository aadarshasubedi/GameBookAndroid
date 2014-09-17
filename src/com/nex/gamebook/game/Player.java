package com.nex.gamebook.game;

import java.util.Map;

import android.util.Log;

import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.util.GameBookUtils;
import com.nex.gamebook.util.SaveGameSectionState;
import com.nex.gamebook.util.SaveGameState;

public class Player extends Character {
	public static int SKILLPOINTS_AT_LEVEL = 3;
	private int id;
	private String name;
	private String description;
	private int position;
	
	private Enemy currentEnemy;

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
		boolean isLuck = (option.isLuckAspect() && hasLuck()) || option.isAlreadyDisplayed();
		boolean hasSkill = option.getSkill() > 0 && getCurrentStats().getSkill() >= option.getSkill();

		option.setDisplayed(both ? isLuck && hasSkill : isLuck || hasSkill);
	}

	public String getName() {
		return GameBookUtils.getInstance().getText(name, getStory());
	}

	public String getRawName() {
		return name;
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
	public void addExperience(BattleLogCallback callback, long exp) {
		if (getLevel() == ExperienceMap.getInstance().getMaxLevel()) {
			return;
		}
		
		setExperience(getExperience() + exp);
		long requiredExperience = ExperienceMap.getInstance().getExperienceByLevel(getLevel());
		callback.logExperience(exp);
		while (getExperience() >= requiredExperience) {
			setLevel(getLevel() + 1);
			if(getLevel() % SKILLPOINTS_AT_LEVEL == 0) {
				setSkillPoints(getSkillPoints() + 1);
			}
			requiredExperience = ExperienceMap.getInstance().getExperienceByLevel(getLevel());
			callback.logLevelIncreased();
			ExperienceMap.getInstance().updateStatsByLevel(this);
		}
	}

	@Override
	public boolean hasLuck() {
		return hasLuck(Stats.TOTAL_LUCK_FOR_CALC + getLevel());
	}

	@Override
	public boolean isCriticalChance() {
		return isCriticalChance(Stats.TOTAL_SKILL_FOR_CALC + getLevel());
	}

	public void save() {
		try {
			GameBookUtils.getInstance().saveGame(this);
		} catch (Exception e) {
			Log.e("SaveGame", "", e);
		}
	}

	public SaveGameState createSaveGameState() {
		SaveGameState state = new SaveGameState();
		state.setCurrentStats(getCurrentStats());
		state.setStats(getStats());
		state.setLevel(getLevel());
		state.setExperience(getExperience());
		state.setPosition(getPosition());
		state.setStatistics(getStatistics());
		state.setLearnedPassiveSkills(getLearnedPassiveSkills());
		state.setSkillPoints(getSkillPoints());
		for (Map.Entry<Integer, StorySection> entry : getStory().getSections().entrySet()) {
			SaveGameSectionState sectionState = new SaveGameSectionState();
			StorySection section = entry.getValue();
			sectionState.setAlreadyHasLuck(section.isAlreadyHasLuck());
			sectionState.setBonusesAlreadyGained(section.isBonusesAlreadyGained());
			sectionState.setBonusesAfterFightAlreadyGained(section.isBonusesAfterFightAlreadyGained());
			sectionState.setBonusesBeforeFightAlreadyGained(section.isBonusesBeforeFightAlreadyGained());
			sectionState.setCompleted(section.isCompleted());
			sectionState.setEnemiesAlreadyKilled(section.isEnemiesAlreadyKilled());
			sectionState.setHasLuck(section.isHasLuck());
			sectionState.setTryluck(section.isTryluck());
			sectionState.setVisited(section.isVisited());
			sectionState.setXpAlreadyGained(section.isXpAlreadyGained());
			state.getSectionsState().put(entry.getKey(), sectionState);
		}
		return state;
	}

	public void updateSavedGameStates(SaveGameState state) {
		setLevel(state.getLevel());
		setExperience(state.getExperience());
		setPosition(state.getPosition());
		setStatistics(state.getStatistics());
		setCurrentStats(state.getCurrentStats());
		setStats(state.getStats());
		setSkillPoints(state.getSkillPoints());
		setLearnedPassiveSkills(state.getLearnedPassiveSkills());
		instantiatePassiveSkills();
		for (Map.Entry<Integer, SaveGameSectionState> entry : state.getSectionsState().entrySet()) {
			SaveGameSectionState section = entry.getValue();
			StorySection sectionState = getStory().getSection(entry.getKey());
			if (sectionState == null) {
				Log.w("Loading game", "section " + entry.getKey() + " nont exist");
				continue;
			}
			sectionState.setAlreadyHasLuck(section.isAlreadyHasLuck());
			sectionState.setBonusesAlreadyGained(section.isBonusesAlreadyGained());
			sectionState.setBonusesAfterFightAlreadyGained(section.isBonusesAfterFightAlreadyGained());
			sectionState.setBonusesBeforeFightAlreadyGained(section.isBonusesBeforeFightAlreadyGained());
			sectionState.setCompleted(section.isCompleted());
			sectionState.setEnemiesAlreadyKilled(section.isEnemiesAlreadyKilled());
			sectionState.setHasLuck(section.isHasLuck());
			sectionState.setTryluck(section.isTryluck());
			sectionState.setVisited(section.isVisited());
			sectionState.setXpAlreadyGained(section.isXpAlreadyGained());
		}
	}

	@Override
	public void chooseBestSkill(Character c, boolean enemyBegin) {
	}

	public Enemy getCurrentEnemy() {
		return currentEnemy;
	}

	public void setCurrentEnemy(Enemy currentEnemy) {
		this.currentEnemy = currentEnemy;
	}

}