package com.nex.gamebook.entity.character;

import com.nex.gamebook.entity.Entity;

public abstract class Character implements Entity {

	private long id;
	
	private long storyId;
	private Stats stats = new Stats();
	private Stats currentStats = new Stats(stats);
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public long getStoryId() {
		return storyId;
	}

	public void setStoryId(long storyId) {
		this.storyId = storyId;
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

	public abstract int getDescription();
	public abstract int getName();
}
