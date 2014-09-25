package com.nex.gamebook.util;

import java.util.ArrayList;
import java.util.List;

public class SaveGameSectionState {
	private boolean xpAlreadyGained;
	private boolean completed;
	private boolean visited;
	private boolean alreadyHasLuck;
	private boolean bonusesAlreadyGained;
	private boolean bonusesBeforeFightAlreadyGained;
	private boolean bonusesAfterFightAlreadyGained;
	private boolean enemiesAlreadyKilled;
	private boolean hasLuck;
	private boolean tryluck = true;
	private List<Integer> disabledOptions = new ArrayList<>();

	public boolean isXpAlreadyGained() {
		return xpAlreadyGained;
	}

	public void setXpAlreadyGained(boolean xpAlreadyGained) {
		this.xpAlreadyGained = xpAlreadyGained;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public boolean isAlreadyHasLuck() {
		return alreadyHasLuck;
	}

	public void setAlreadyHasLuck(boolean alreadyHasLuck) {
		this.alreadyHasLuck = alreadyHasLuck;
	}

	public boolean isBonusesAlreadyGained() {
		return bonusesAlreadyGained;
	}

	public void setBonusesAlreadyGained(boolean bonusesAlreadyGained) {
		this.bonusesAlreadyGained = bonusesAlreadyGained;
	}

	public boolean isEnemiesAlreadyKilled() {
		return enemiesAlreadyKilled;
	}

	public void setEnemiesAlreadyKilled(boolean enemiesAlreadyKilled) {
		this.enemiesAlreadyKilled = enemiesAlreadyKilled;
	}

	public boolean isHasLuck() {
		return hasLuck;
	}

	public void setHasLuck(boolean hasLuck) {
		this.hasLuck = hasLuck;
	}

	public boolean isTryluck() {
		return tryluck;
	}

	public void setTryluck(boolean tryluck) {
		this.tryluck = tryluck;
	}

	public boolean isBonusesBeforeFightAlreadyGained() {
		return bonusesBeforeFightAlreadyGained;
	}

	public void setBonusesBeforeFightAlreadyGained(boolean bonusesBeforeFightAlreadyGained) {
		this.bonusesBeforeFightAlreadyGained = bonusesBeforeFightAlreadyGained;
	}

	public boolean isBonusesAfterFightAlreadyGained() {
		return bonusesAfterFightAlreadyGained;
	}

	public void setBonusesAfterFightAlreadyGained(boolean bonusesAfterFightAlreadyGained) {
		this.bonusesAfterFightAlreadyGained = bonusesAfterFightAlreadyGained;
	}

	public List<Integer> getDisabledOptions() {
		return disabledOptions;
	}

	public void setDisabledOptions(List<Integer> disabledOptions) {
		this.disabledOptions = disabledOptions;
	}

}
