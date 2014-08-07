package com.nex.gamebook.story.section;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.nex.gamebook.R;
import com.nex.gamebook.entity.Bonus;
import com.nex.gamebook.entity.Bonus.BonusState;
import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.StorySectionOption;

public class StorySection implements Serializable {

	private String text;
	private String alreadyVisitedText;
	private String enemiesDefeatedText;

	private int unreturnableSection = -1;

	private boolean loseSection;
	private boolean winSection;

	private boolean bonusesAlreadyGained;
	private boolean enemiesAlreadyKilled;
	private boolean completed;
	private boolean visited;
	private boolean hasLuck;
	private boolean luckDefeatEnemies;
	private int luckText = R.string.fight_luck_section;
	private int gameOverText = R.string.game_over_section;

	private List<StorySectionOption> options = new ArrayList<>();
	private List<Enemy> enemies = new ArrayList<>();
	private List<Bonus> bonuses = new ArrayList<>();

	public StorySection() {
		super();
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

	public void tryApplyLuckForBattle(Player character) {
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

	public boolean isLuckDefeatEnemies() {
		return luckDefeatEnemies;
	}

	public void setLuckDefeatEnemies(boolean luckDefeatEnemies) {
		this.luckDefeatEnemies = luckDefeatEnemies;
	}

	public int getUnreturnableSection() {
		return unreturnableSection;
	}

	public void setUnreturnableSection(int unreturnableSection) {
		this.unreturnableSection = unreturnableSection;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAlreadyVisitedText() {
		return alreadyVisitedText;
	}

	public void setAlreadyVisitedText(String alreadyVisitedText) {
		this.alreadyVisitedText = alreadyVisitedText;
	}

	public String getEnemiesDefeatedText() {
		return enemiesDefeatedText;
	}

	public void setEnemiesDefeatedText(String enemiesDefeatedText) {
		this.enemiesDefeatedText = enemiesDefeatedText;
	}

	public boolean isLoseSection() {
		return loseSection;
	}

	public void setLoseSection(boolean loseSection) {
		this.loseSection = loseSection;
	}

	public boolean isWinSection() {
		return winSection;
	}

	public void setWinSection(boolean winSection) {
		this.winSection = winSection;
	}

}
