package com.nex.gamebook.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

import android.util.Log;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.entity.io.GameBookUtils;

public abstract class Character implements Serializable, Mergable {
	private static final long serialVersionUID = 214922718575334896L;

	private Stats stats = new Stats();
	private Stats currentStats = new Stats(stats);
	private boolean fighting;
	private Story story;
	private transient Stats temporalStatsHolder;
	private SpecialSkill specialSkill;

	public Character() {
		// TODO Auto-generated constructor stub
	}

	public Character(Character character) {
		this.stats = new Stats(character.stats);
		this.currentStats = new Stats(this.stats);
		this.story = character.story;
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

	public boolean hasLuck() {
		Random random = new Random();
		int res = random.nextInt(Stats.TOTAL_LUCK_FOR_CALC);
		return getCurrentStats().getLuck() >= res;
	}

	public boolean isCriticalChance() {
		Random random = new Random();
		int res = random.nextInt(Stats.TOTAL_SKILL_FOR_CALC);
		return getCurrentStats().getSkill() >= res;
	}

	public boolean isDefeated() {
		return getCurrentStats().getHealth() <= 0;
	}

	public abstract CharacterType getType();

	public boolean isFighting() {
		return fighting;
	}

	public void setFighting(boolean fighting) {
		this.fighting = fighting;
	}

	public Story getStory() {
		return story;
	}

	public void setStory(Story story) {
		this.story = story;
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
			Method currentAttr = Stats.class.getDeclaredMethod(GameBookUtils.createMethodName("get", bonus.getType().name().toLowerCase()), new Class[0]);
			Method defaultAttr = Stats.class.getDeclaredMethod(GameBookUtils.createMethodName("get", bonus.getType().name().toLowerCase()), new Class[0]);

			int currentValue = (int) currentAttr.invoke(getCurrentStats(),
					new Object[0]);
			int total = currentValue + (bonus.getCoeff() * bonus.getValue());
			int defaultValue = (int) defaultAttr.invoke(getStats(),
					new Object[0]);
			currentAttr = Stats.class.getDeclaredMethod(
					GameBookUtils.createMethodName("set", bonus.getType()
							.name().toLowerCase()), int.class);
			int setedValue = 0;
			if (total > defaultValue && !bonus.isOverflowed()) {
				realValue = defaultValue - currentValue;
				setedValue = (int) currentAttr.invoke(getCurrentStats(),
						defaultValue);
			} else {
				if (total < 0) {
					realValue = currentValue;
					total = 0;
				}
				setedValue = (int) currentAttr.invoke(getCurrentStats(), total);
			}
			if ((currentValue + realValue) != setedValue) {
				realValue = setedValue - currentValue;
			}
			if (!bonus.isPermanent()) {
				if (this.temporalStatsHolder == null) {
					this.temporalStatsHolder = new Stats();

					this.temporalStatsHolder.setDamage(0);
				}
				Field tempAttr = Stats.class.getDeclaredField(bonus.getType().name().toLowerCase());
				tempAttr.setAccessible(true);
				tempAttr.set(this.temporalStatsHolder, realValue);
				tempAttr.setAccessible(false);
			}
		} catch (Exception e) {
			Log.e("GameBook", "", e);
		}
		return realValue;
	}

	public Stats getTemporalStatsHolder() {
		return temporalStatsHolder;
	}

	public void holdCurrentStatsToTemporal() {
		if (this.temporalStatsHolder != null) {
			this.temporalStatsHolder.setHolder(new Stats(getCurrentStats()));
		}
	}

	public Stats releaseTemporalStats() {
		if (this.temporalStatsHolder == null)
			return null;
		getCurrentStats().releaseTemporalStats(this.temporalStatsHolder);
		Stats releasedStats = new Stats(this.temporalStatsHolder);
		this.temporalStatsHolder = null;
		return releasedStats;
	}

	public SpecialSkill getSpecialSkill() {
		return specialSkill;
	}

	public void setSpecialSkill(SpecialSkill specialAttack) {
		this.specialSkill = specialAttack;
	}

}
