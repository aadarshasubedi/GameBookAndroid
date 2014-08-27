package com.nex.gamebook.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.util.Log;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.entity.Bonus.StatType;
import com.nex.gamebook.entity.io.GameBookUtils;

public abstract class Character implements Serializable, Mergable {
	private static final long serialVersionUID = 214922718575334896L;
	private Stats stats = new Stats();
	private Stats currentStats = new Stats(stats);
	private boolean fighting;
	private Story story;
	private transient Stats temporalStatsHolder;
	private String skillName;
	private Map<String, Integer> specialSkills = new HashMap<>();
	private transient Set<SpecialSkill> activeSkills;
	private transient List<Bonus> conditions = new ArrayList<>();
	private int level = 1;
	private long experience = 0;

	private StatType primaryStat;

	public Character() {
		// TODO Auto-generated constructor stub
	}

	public Character(Character character) {
		this.stats = new Stats(character.stats);
		this.currentStats = new Stats(this.stats);
		this.skillName = character.skillName;
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
	public abstract boolean hasLuck();
	public abstract boolean isCriticalChance();
	public boolean hasLuck(int modificator) {
		Random random = new Random();
		int res = random.nextInt(modificator);
		return getCurrentStats().getLuck() >= res;
	}

	public boolean isCriticalChance(int modificator) {
		Random random = new Random();
		int res = random.nextInt(modificator);
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
		int realValue = 0;
		try {
			int currentValue = GameBookUtils.getStatByType(getCurrentStats(), bonus.getType());
			int total = currentValue + (bonus.getCoeff() * bonus.getValue());
			int defaultValue = GameBookUtils.getStatByType(getStats(), bonus.getType());
			int setedValue = 0;
			if (bonus.isBase()) {
				GameBookUtils.setStatByType(getStats(), bonus.getType(), defaultValue + bonus.getValue());
				defaultValue = GameBookUtils.getStatByType(getStats(), bonus.getType());
			} 
			if (bonus.getCoeff() > 0 && total > defaultValue && !bonus.isOverflowed()) {
				if (total > defaultValue && currentValue < defaultValue) {
					realValue = defaultValue - currentValue;
					setedValue = GameBookUtils.setStatByType(getCurrentStats(), bonus.getType(), defaultValue);
				} else {
					realValue = 0;
				}
				
			} else {
				if (total < 0) {
					realValue = currentValue;
					total = 0;
				}
				setedValue = GameBookUtils.setStatByType(getCurrentStats(), bonus.getType(), total);
			}
			if(setedValue==0) {
				realValue = 0;
			} else if ((currentValue + realValue) != setedValue) {
				realValue = setedValue - currentValue;
			}
			if (!bonus.isPermanent()) {
				if (this.temporalStatsHolder == null) {
					this.temporalStatsHolder = new Stats();
					this.temporalStatsHolder.nullAllAttributes();
				}
				Field tempAttr = Stats.class.getDeclaredField(bonus.getType().name().toLowerCase());
				tempAttr.setAccessible(true);
				tempAttr.set(this.temporalStatsHolder, realValue);
				tempAttr.setAccessible(false);
			}
		} catch (Exception e) {
			Log.e("GameBook", "", e);
		}
		if(bonus.isBase()) {
			realValue = bonus.getValue();
		}
		return realValue;
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

	public List<Bonus> getConditions() {
		if (conditions == null)
			conditions = new ArrayList<Bonus>();
		return conditions;
	}

	public Bonus findConditionById(String id) {
		for (Bonus b : conditions) {
			if (b.getConditionId().equals(id)) {
				return b;
			}
		}
		return null;
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

	public Map<String, Integer> getSpecialSkills() {
		return specialSkills;
	}

	public void setSpecialSkills(Map<String, Integer> specialSkills) {
		this.specialSkills = specialSkills;
	}

	public SpecialSkill getSpecialSkill() {
		return getSpecialSkill(skillName);
	}
	public SpecialSkill getSpecialSkill(String skillName) {
		if(skillName!=null && skillName.length()>0)
		for (SpecialSkill skill : activeSkills) {
			if (SpecialSkillsMap.getSkills().get(skillName).equals(skill.getClass())) {
				return skill;
			}
		}
		return null;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public List<String> getAvailableSkills() {
		List<String> skills = new ArrayList<String>();
		for (Map.Entry<String, Integer> entry : this.specialSkills.entrySet()) {
			if (entry.getValue() <= getLevel()) {
				skills.add(entry.getKey());
			}
		}
		return skills;
	}

	public void cleanActiveSkillsAfterFightEnd() {
		for (SpecialSkill skill : activeSkills) {
			skill.cleanAfterFightEnd();
		}
	}
	public void cleanActiveSkillsAfterBattleEnd() {
		for (SpecialSkill skill : activeSkills) {
			skill.cleanAfterBattleEnd();
		}
	}
	public void createActiveSkills() {
		this.activeSkills = new HashSet<>();
		for (String s : getAvailableSkills()) {
			this.activeSkills.add(SpecialSkillsMap.get(s));
		}
	}
	public void updateActiveSkills() {
//		this.activeSkills.clear();
		for (String s : getAvailableSkills()) {
			SpecialSkill skill = getSpecialSkill(s);
			if(skill==null)
			this.activeSkills.add(SpecialSkillsMap.get(s));
		}
	}
	
	
	public int getXpToLevelPercentage() {
		long xpToLevel = ExperienceMap.getInstance().getExperienceByLevel(getLevel());
		return (int) (((double) experience / (double) xpToLevel) * 100d);
	}

	public StatType getPrimaryStat() {
		return primaryStat;
	}

	public void setPrimaryStat(StatType primaryStat) {
		this.primaryStat = primaryStat;
	}
	
	public Stats resetStats(int mod) {
		Stats s = new Stats();
		s.nullAllAttributes();
		for(StatType type: StatType.values()) {
			try {
				int defaultVal = GameBookUtils.getStatByType(this.stats, type);
				int currentVal = GameBookUtils.getStatByType(this.currentStats, type);
				if( (mod==0 && currentVal!=defaultVal) ||
					(mod==1 && currentVal>defaultVal)||
					(mod==2 && currentVal<defaultVal)) {
					GameBookUtils.setStatByType(this.currentStats, type, defaultVal);
					int res = currentVal -  defaultVal;
					GameBookUtils.setStatByType(s, type, res);
				}
			} catch (Exception e) {
				Log.e("ResetOverwlowedAttributes", "", e);
			}
		}
		return s;
	}
	public String getSkillName() {
		return skillName;
	}
}
