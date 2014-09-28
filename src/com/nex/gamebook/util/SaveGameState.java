package com.nex.gamebook.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.nex.gamebook.game.Stats;

public class SaveGameState {

	private Stats stats;
	private Stats currentStats;
	private Statistics statistics;
	private String storyPath;
	private int position;
	private long score;
	private int resetSkillsAvailable;
	private int level = 1;
	private long experience = 0;
	private Map<Integer, SaveGameSectionState> sectionsState = new HashMap<>();
	private int skillPoints;
	private Set<String> learnedPassiveSkills = new HashSet<>();
	private transient Properties properties;

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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getExperience() {
		return experience;
	}

	public void setExperience(long experience) {
		this.experience = experience;
	}

	public Map<Integer, SaveGameSectionState> getSectionsState() {
		return sectionsState;
	}

	public void setSectionsState(Map<Integer, SaveGameSectionState> sectionsState) {
		this.sectionsState = sectionsState;
	}

	public int getSkillPoints() {
		return skillPoints;
	}

	public void setSkillPoints(int skillPoints) {
		this.skillPoints = skillPoints;
	}

	public Set<String> getLearnedPassiveSkills() {
		return learnedPassiveSkills;
	}

	public void setLearnedPassiveSkills(Set<String> learnedPassiveSkills) {
		this.learnedPassiveSkills = learnedPassiveSkills;
	}

	public Statistics getStatistics() {
		return statistics;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public String getStoryPath() {
		return storyPath;
	}

	public void setStoryPath(String storyPath) {
		this.storyPath = storyPath;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public int getResetSkillsAvailable() {
		return resetSkillsAvailable;
	}

	public void setResetSkillsAvailable(int resetSkillsAvailable) {
		this.resetSkillsAvailable = resetSkillsAvailable;
	}

}
