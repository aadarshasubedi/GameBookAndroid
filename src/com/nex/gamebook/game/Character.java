package com.nex.gamebook.game;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.util.Log;

import com.nex.gamebook.attack.special.SkillProperties;
import com.nex.gamebook.attack.special.SpecialAttackSkill;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.util.GameBookUtils;

public abstract class Character implements Serializable, Mergable {
	private static final long serialVersionUID = 214922718575334896L;
	private Stats stats = new Stats(true);
	private Stats currentStats = new Stats(stats, false);
	private boolean fighting;
	private Story story;
	private boolean overideHolderStats = false;
	private Stats temporalStatsHolder;
	// private String skillName;
	private transient List<SkillAssign> assignedSkills = new ArrayList<>();

	private transient Map<SkillRequiredLevel, SpecialSkill> specialSkills = new HashMap<>();
	private transient Set<SpecialSkill> activeSkills;
	private transient SpecialSkill selectedSkill;

	private transient Set<ActiveOvertimeSkill> overtimeSkills = new HashSet<ActiveOvertimeSkill>();
	private transient Set<Bonus> conditions = new HashSet<>();
	private int level = 1;
	private long experience = 0;
	private StatType primaryStat;
	private boolean canAttack = true;
	private boolean canCastSkill = true;

	public Character() {
		// TODO Auto-generated constructor stub
	}

	public Character(Character character) {
		this.stats = new Stats(character.stats, true);
		this.currentStats = new Stats(this.stats, false);
		this.story = character.story;
		this.specialSkills = new HashMap<>(character.getSpecialSkills());
		this.assignedSkills = new ArrayList<SkillAssign>(character.getAssignedSkills());
	}

	public Stats getStats() {
		return stats;
	}

	public Stats getCurrentStats() {
		return currentStats;
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
		int bonusValue = bonus.getValue();

		try {

			int currentValue = GameBookUtils.getStatByType(getCurrentStats(), bonus.getType());
			int total = currentValue + (bonus.getCoeff() * bonusValue);
			int defaultValue = GameBookUtils.getStatByType(getStats(), bonus.getType());
			int setedValue = 0;
			if (bonus.isBase()) {
				GameBookUtils.setStatByType(getStats(), bonus.getType(), defaultValue + bonusValue);
				int difference = currentValue - defaultValue;
				defaultValue = GameBookUtils.getStatByType(getStats(), bonus.getType());
				defaultValue += difference;
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
				} else {
					realValue = bonusValue;
				}
				setedValue = GameBookUtils.setStatByType(getCurrentStats(), bonus.getType(), total);
			}
			if (bonus.getCoeff() > 0)
				if (setedValue == 0) {
					realValue = 0;
				}
			// if ((currentValue + realValue) != setedValue && ) {
			// realValue = setedValue - currentValue;
			// }
			if (!bonus.isPermanent()) {
				if (this.temporalStatsHolder == null) {
					overideHolderStats = true;
					this.temporalStatsHolder = new Stats(false);
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
		if (bonus.isBase()) {
			realValue = bonusValue;
		}
		return realValue;
	}

	public void holdCurrentStatsToTemporal() {
		if (this.temporalStatsHolder != null && overideHolderStats) {
			this.temporalStatsHolder.setHolder(new Stats(getCurrentStats(), false));
			overideHolderStats = false;
		}
	}

	public Stats releaseTemporalStats() {
		if (this.temporalStatsHolder == null)
			return null;
		getCurrentStats().releaseTemporalStats(this.temporalStatsHolder);
		Stats releasedStats = new Stats(this.temporalStatsHolder, -1, false);
		this.temporalStatsHolder = null;
		return releasedStats;
	}

	public Set<Bonus> getConditions() {
		if (conditions == null)
			conditions = new HashSet<>();
		return conditions;
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

	public Map<SkillRequiredLevel, SpecialSkill> getSpecialSkills() {
		return specialSkills;
	}

	public void setSpecialSkills(Map<SkillRequiredLevel, SpecialSkill> specialSkills) {
		this.specialSkills = specialSkills;
	}

	public SpecialSkill getSelectedSkill() {
		return selectedSkill;
	}

	public void setSelectedSkill(SpecialSkill selectedSkill) {
		this.selectedSkill = selectedSkill;
	}

	public void clearOvertimeSkills() {
		overtimeSkills.clear();
	}

	public void clearConditionSkills(boolean all) {
		if (all) {
			conditions.clear();
		} else {
			List<Bonus> releaseThese = new ArrayList<Bonus>();
			for (Bonus b : this.conditions) {
				if (b.getTurns() > SpecialAttackSkill.NO_VALUE) {
					releaseThese.add(b);
				}
			}
			this.conditions.removeAll(releaseThese);
		}
	}

	public void cleanActiveSkillsAfterFightEnd() {
		for (SpecialSkill skill : activeSkills) {
			skill.cleanAfterFightEnd();
		}
		clearOvertimeSkills();
		clearConditionSkills(false);
	}

	public void cleanActiveSkillsAfterBattleEnd() {
		for (SpecialSkill skill : activeSkills) {
			skill.cleanAfterBattleEnd();
		}
		clearConditionSkills(true);
	}

	public void createActiveSkills() {
		this.activeSkills = new HashSet<>();
		updateActiveSkills();
	}

	public void updateActiveSkills() {
		// this.activeSkills.clear();
		for (Map.Entry<SkillRequiredLevel, SpecialSkill> skills : specialSkills.entrySet()) {
			SkillRequiredLevel level = skills.getKey();
			if (level.getLevel() <= getLevel())
				this.activeSkills.add(skills.getValue());
		}
	}

	public int getXpToLevelPercentage() {
		long xpToLevel = ExperienceMap.getInstance().getExperienceByLevel(getLevel());
		long previousXp = ExperienceMap.getInstance().getExperienceByLevel(getLevel() - 1);
		long res1 = xpToLevel - previousXp;
		long res2 = experience - previousXp;
		return (int) (((float) res2 / (float) res1) * 100d);
	}

	public StatType getPrimaryStat() {
		return primaryStat;
	}

	public void setPrimaryStat(StatType primaryStat) {
		this.primaryStat = primaryStat;
	}

	public Stats resetStats(int mod) {
		Stats s = new Stats(false);
		s.nullAllAttributes();
		for (StatType type : StatType.values()) {
			try {
				int defaultVal = GameBookUtils.getStatByType(this.stats, type);
				int currentVal = GameBookUtils.getStatByType(this.currentStats, type);
				if ((mod == 0 && currentVal != defaultVal) || (mod == 1 && currentVal > defaultVal) || (mod == 2 && currentVal < defaultVal)) {
					GameBookUtils.setStatByType(this.currentStats, type, defaultVal);
					int res = currentVal - defaultVal;
					GameBookUtils.setStatByType(s, type, -res);
				}
			} catch (Exception e) {
				Log.e("ResetOverwlowedAttributes", "", e);
			}
		}
		return s;
	}

	public Set<SpecialSkill> getActiveSkills() {
		return activeSkills;
	}

	public Set<ActiveOvertimeSkill> getOvertimeSkills() {
		return overtimeSkills;
	}

	public abstract void chooseBestSkill(Character c, boolean enemyBegin);

	public boolean hasHots() {
		return getHots() > 0;
	}

	public boolean hasBuffs() {
		return getDebuffs() > 0;
	}

	public int getHots() {
		int i = 0;
		for (ActiveOvertimeSkill a : getOvertimeSkills()) {
			if (!a.getTargetSkill().isCondition())
				i++;
		}
		return i;
	}

	public boolean hasBuff(StatType type) {
		for (Bonus a : getConditions()) {
			if (a.getCoeff() > 0 && a.getType().equals(type))
				return true;

		}
		return false;
	}

	public int getBuffs() {
		int i = 0;
		for (Bonus a : getConditions()) {
			if (a.getCoeff() > 0)
				i++;
		}
		return i;
	}

	public boolean hasDots() {
		return getDots() > 0;
	}

	public boolean hasDebuffs() {
		return getDebuffs() > 0;
	}

	public boolean hasDebuff(StatType type) {
		for (Bonus a : getConditions()) {
			if (a.getCoeff() < 0 && a.getType().equals(type))
				return true;

		}
		return false;
	}

	public int getDots() {
		int i = 0;
		for (ActiveOvertimeSkill a : getOvertimeSkills()) {
			if (a.getTargetSkill().isCondition())
				i++;
		}
		return i;
	}

	public int getDebuffs() {
		int i = 0;
		for (Bonus a : getConditions()) {
			if (a.getCoeff() < 0)
				i++;
		}
		return i;
	}

	public int getLongestHot() {
		List<ActiveOvertimeSkill> buffs = new ArrayList<ActiveOvertimeSkill>();
		for (ActiveOvertimeSkill a : getOvertimeSkills()) {
			if (!a.getTargetSkill().isCondition())
				buffs.add(a);
		}
		Collections.sort(buffs, createOvertimeSkillsComparator());
		if (buffs.size() > 0)
			return buffs.get(0).getRemainsTurns();
		return 0;
	}

	public int getLongestDot() {
		List<ActiveOvertimeSkill> buffs = new ArrayList<ActiveOvertimeSkill>();
		for (ActiveOvertimeSkill a : getOvertimeSkills()) {
			if (a.getTargetSkill().isCondition())
				buffs.add(a);
		}
		Collections.sort(buffs, createOvertimeSkillsComparator());
		if (buffs.size() > 0)
			return buffs.get(0).getRemainsTurns();
		return 0;
	}

	public int getLongestBuff() {
		List<Bonus> buffs = new ArrayList<Bonus>();
		for (Bonus a : getConditions()) {
			if (a.getCoeff() > 0)
				buffs.add(a);
		}
		Collections.sort(buffs, createConditionsComparator());
		if (buffs.size() > 0)
			return buffs.get(0).getRemainsTurns();
		return 0;
	}

	public int getLongestDebuff() {
		List<Bonus> buffs = new ArrayList<Bonus>();
		for (Bonus a : getConditions()) {
			if (a.getCoeff() < 0)
				buffs.add(a);
		}
		Collections.sort(buffs, createConditionsComparator());
		if (buffs.size() > 0)
			return buffs.get(0).getRemainsTurns();
		return 0;
	}

	private Comparator<ActiveOvertimeSkill> createOvertimeSkillsComparator() {
		return new Comparator<ActiveOvertimeSkill>() {
			@Override
			public int compare(ActiveOvertimeSkill lhs, ActiveOvertimeSkill rhs) {
				return -Integer.valueOf(lhs.getRemainsTurns()).compareTo(rhs.getRemainsTurns());
			}
		};
	}

	private Comparator<Bonus> createConditionsComparator() {
		return new Comparator<Bonus>() {
			@Override
			public int compare(Bonus lhs, Bonus rhs) {
				return -Integer.valueOf(lhs.getRemainsTurns()).compareTo(rhs.getRemainsTurns());
			}
		};
	}

	public List<SkillAssign> getAssignedSkills() {
		return assignedSkills;
	}

	public void createSkills(Map<String, SkillProperties> skills) {
		for (SkillAssign assignedSkill : this.assignedSkills) {
			SkillProperties skillProp = skills.get(assignedSkill.getSkillKey());
			if (skillProp == null) {
				Log.w("SkillAssign", "skill with name " + assignedSkill.getSkillKey() + " not exist");
				continue;
			}
			this.specialSkills.put(new SkillRequiredLevel(skillProp.getLevelRequired(), skillProp.getId()), skillProp.createSpecialSkill(getStory()));
		}
		assignedSkills.clear();
	}

	public void setCurrentStats(Stats currentStats) {
		Stats s = new Stats(currentStats, false);
		s.setCharacter(this);
		this.currentStats = s;
	}

	public void setStats(Stats stats) {
		Stats s = new Stats(stats, true);
		s.setCharacter(this);
		this.stats = s;
	}

	public boolean isCanAttack() {
		return canAttack;
	}

	public void setCanAttack(boolean canAttack) {
		this.canAttack = canAttack;
	}

	public boolean isCanCastSkill() {
		return canCastSkill;
	}

	public void setCanCastSkill(boolean canCastSkill) {
		this.canCastSkill = canCastSkill;
	}
	
	public void allowActions() {
		this.canAttack = true;
		this.canCastSkill = true;
	}
	
}
