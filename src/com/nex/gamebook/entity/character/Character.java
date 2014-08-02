package com.nex.gamebook.entity.character;

import java.lang.reflect.Field;
import java.util.Random;

import android.util.Log;

import com.nex.gamebook.entity.Entity;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.story.Bonus;
import com.nex.gamebook.story.StorySectionOption;

public class Character implements Entity {

	private int id;
	private int name;
	private int description;
	private int position;

	private Story story;
	private Stats stats = new Stats();
	private Stats currentStats = new Stats(stats);

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Story getStory() {
		return story;
	}

	public void setStory(Story story) {
		this.story = story;
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

	public Boolean canShowOption(StorySectionOption option) {
		return currentStats.getSkill() >= option.getSkill();
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getDescription() {
		return description;
	}

	public void setDescription(int description) {
		this.description = description;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isDefeated() {
		return getCurrentStats().getHealth() <= 0;
	}

	/**
	 * Returns real added value
	 * 
	 * @param bonus
	 * @return
	 */
	public int addBonus(Bonus bonus) {
		int realValue = bonus.getValue();
		try {
			Field currentAttr = Stats.class.getDeclaredField(bonus.getType()
					.name().toLowerCase());
			Field defaultAttr = Stats.class.getDeclaredField(bonus.getType()
					.name().toLowerCase());
			currentAttr.setAccessible(true);
			defaultAttr.setAccessible(true);
			int currentValue = currentAttr.getInt(this.currentStats);
			int total = currentValue + (bonus.getCoeff() * bonus.getValue());
			int defaultValue = defaultAttr.getInt(this.stats);
			if (total > defaultValue && !bonus.isOverflowed()) {
				realValue = defaultValue - currentValue;
				currentAttr.set(this.currentStats, defaultValue);
			} else {
				currentAttr.set(this.currentStats, total);
			}
			currentAttr.setAccessible(false);
			currentAttr.setAccessible(false);
		} catch (Exception e) {
			Log.e("GameBook", "", e);
		}
		return realValue;
	}

	public boolean hasLuck() {
		Random random = new Random();
		int res = random.nextInt(15);
		return getCurrentStats().getLuck() >= res;
	}

}
