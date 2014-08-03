package com.nex.gamebook.story.section;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nex.gamebook.R;
import com.nex.gamebook.entity.character.Character;
import com.nex.gamebook.story.Bonus;
import com.nex.gamebook.story.Bonus.BonusState;
import com.nex.gamebook.story.Enemy;
import com.nex.gamebook.story.StorySectionOption;

public class StorySection {

	private int text;
	private int enemiesDefeatedText;
	
	private boolean bonusesAlreadyGained;
	private boolean enemiesAlreadyKilled;
	private boolean endGame;
	private boolean completed;
	private boolean visited;
	private boolean hasLuck = false;
	private int luckText = R.string.fight_luck_section;
	private int gameOverText = R.string.game_over_section;

	private List<StorySectionOption> options = new ArrayList<>();
	private List<Enemy> enemies = new ArrayList<>();
	private List<Bonus> bonuses = new ArrayList<>();

	public StorySection() {
		super();
	}

	public int getEnemiesDefeatedText() {
		return enemiesDefeatedText;
	}

	public void setEnemiesDefeatedText(int enemiesDefeatedText) {
		this.enemiesDefeatedText = enemiesDefeatedText;
	}

	public int getText() {
		return text;
	}

	public void setText(int text) {
		this.text = text;
	}

	public List<StorySectionOption> getOptions() {
		return options;
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}

	public List<Bonus> getBonuses() {
		return bonuses;
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

	public List<Bonus> getBonuses(BonusState state) {
		List<Bonus> bon = new ArrayList<Bonus>();
		for (Bonus b : bonuses) {
			if (b.getState().equals(state)) {
				bon.add(b);
			}
		}
		return bon;
	}

	public boolean isEndGame() {
		return endGame;
	}

	public void setEndGame(boolean endGame) {
		this.endGame = endGame;
	}

	public boolean isAllDefeated() {
		for (Enemy enemy : this.enemies) {
			if (!enemy.isDefeated()) {
				return false;
			}
		}
		return true;
	}

	public int getLuckText() {
		return luckText;
	}

	public void setLuckText(int luckText) {
		this.luckText = luckText;
	}

	public boolean isHasLuck() {
		return hasLuck;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public void tryApplyLuckForBattle(Character character) {
		hasLuck = character.hasLuck();
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public void reset() {
		this.completed = false;
	}

	public void setHasLuck(boolean hasLuck) {
		this.hasLuck = hasLuck;
	}

	public int getGameOverText() {
		return gameOverText;
	}

	public void setGameOverText(int gameOverText) {
		this.gameOverText = gameOverText;
	}

}
