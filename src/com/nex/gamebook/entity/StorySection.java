package com.nex.gamebook.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.entity.Bonus.BonusState;
import com.nex.gamebook.entity.io.GameBookUtils;

public class StorySection implements Serializable, Mergable {

	private static final long serialVersionUID = 3228667395424629590L;
	private String text;
	private String alreadyVisitedText;
	private String enemiesDefeatedText;
	private String luckText = "main_luck_text";
	private String gameOverText = "main_gameover_text";

	private int unreturnableSection = -1;

	private boolean loseSection;
	private boolean winSection;

	private boolean bonusesAlreadyGained;
	private boolean enemiesAlreadyKilled;
	private boolean completed;
	private boolean visited;
	private boolean hasLuck;
	private boolean luckDefeatEnemies;
	private float scoreMultiplier;

	private Story story;

	private List<StorySectionOption> options = new ArrayList<>();
	private List<Enemy> enemies = new ArrayList<>();
	private List<Bonus> bonuses = new ArrayList<>();
	private transient List<EnemyAssign> enemiesIds = new ArrayList<EnemyAssign>();
	
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
	public List<Bonus> getTemporalBonuses() {
		List<Bonus> bon = new ArrayList<Bonus>();
		for (Bonus b : bonuses) {
			if (!b.isPermanent()) {
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

	public String getLuckText() {
		return GameBookUtils.getInstance().getText(luckText, story);
	}

	public void setLuckText(String luckText) {
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

	public String getGameOverText() {
		return GameBookUtils.getInstance().getText(gameOverText, story);
	}

	public void setGameOverText(String gameOverText) {
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
		return GameBookUtils.getInstance().getText(text, story);
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAlreadyVisitedText() {
		return GameBookUtils.getInstance().getText(alreadyVisitedText, story);
	}

	public void setAlreadyVisitedText(String alreadyVisitedText) {
		this.alreadyVisitedText = alreadyVisitedText;
	}

	public String getEnemiesDefeatedText() {
		return GameBookUtils.getInstance().getText(enemiesDefeatedText, story);
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

	public float getScoreMultiplier() {
		return scoreMultiplier;
	}

	public void setScoreMultiplier(float scoreMultiplier) {
		this.scoreMultiplier = scoreMultiplier;
	}

	public void setStory(Story story) {
		this.story = story;
	}

	public Story getStory() {
		return story;
	}

	public List<EnemyAssign> getEnemiesIds() {
		return enemiesIds;
	}
	
	public void assignEnemies() {
		for(EnemyAssign enemyKey: enemiesIds) {
			Enemy enemy = story.findEnemy(enemyKey.getEnemyKey());
			if(enemy == null) {
				Log.e("GamebookEnemeNotFound", enemyKey.getEnemyKey());
				continue;
			}
			SpecialSkill skill = SpecialSkillsMap.getEnemiesAttack(enemyKey.getEnemySkill());
			if(skill==null && enemy.getSpecialSkill()!=null) {
				Class<? extends SpecialSkill> cls = enemy.getSpecialSkill().getClass();
				try {
					skill = cls.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					Log.e("EnemyAssing", "", e);
				}
			}
			
			this.enemies.add(new Enemy(enemy, skill));
		}
	}
	
}
