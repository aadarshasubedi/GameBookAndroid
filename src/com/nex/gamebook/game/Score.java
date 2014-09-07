package com.nex.gamebook.game;

import java.util.Properties;

import android.util.Log;

import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.util.GameBookUtils;

public class Score {
	private Stats stats;
	private Stats currentStats;
	private int sections;
	private int visitedSections;
	private double multiplier;
	private String charName;
	private int level;
	private String storyPath;
	private transient Properties properties;

	public void saveScoreData(Player player) {
		this.stats = new Stats(player.getStats(), true);
		this.currentStats = new Stats(player.getCurrentStats(), false);
		this.sections = player.getSections();
		this.visitedSections = player.getVisitedSections();
		this.multiplier = player.getStory().getSection(player.getPosition()).getScoreMultiplier();
		this.level = player.getLevel();
		this.charName = player.getRawName();
		this.storyPath = player.getStory().getPath();
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

	public int getScore() {
		int score = 0;
		for (StatType type : StatType.values()) {
			try {
				score += GameBookUtils.getStatByType(getCurrentStats(), type);
			} catch (Exception e) {
				Log.e("Score", "", e);
			}
		}
		int sectionsMultiplier = getSections() - getVisitedSections();
		if (sectionsMultiplier == 0)
			sectionsMultiplier = 1;
		score += getSections() * sectionsMultiplier;
		score += level;
		if (multiplier > 1d) {
			score *= multiplier;
		}
		return score;
	}

	public String getCharName() {
		return properties.getProperty(charName, charName);
	}

	public String getStoryname() {
		return properties.getProperty("name", "storyName");
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getStoryPath() {
		return storyPath;
	}

	public int getLevel() {
		return level;
	}
}
