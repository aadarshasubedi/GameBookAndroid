package com.nex.gamebook.util;

import java.util.HashMap;
import java.util.Map;

import com.nex.gamebook.game.Stats;

public class SaveGameState {

	private Stats stats;
	private Stats currentStats;
	private int position;
	private int sections;
	private int visitedSections;
	private int level = 1;
	private long experience = 0;
	private Map<Integer, SaveGameSectionState> sectionsState = new HashMap<>();

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

}
