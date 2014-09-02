package com.nex.gamebook.game;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Enemy.EnemyLevel;
import com.nex.gamebook.util.GameBookUtils;

public class ExperienceMap {
	public static final int MAX_LEVEL = 20;
	public static long BASE_SECTION_XP = 50;
	private Map<Integer, Long> experienceMap = new HashMap<>();
	private Map<Integer, Double> gainedxpcoeffs = new HashMap<>();
	private static ExperienceMap instance;
	static {
		instance = new ExperienceMap();
		instance.init();
	}

	private void init() {
		double xpcoeff = 1.1;
		double gcoeff = 0.7;
		long xpRequiredToNextLevel = 20;
		long prev = 0;
		experienceMap.put(0, 0L);
		for (int i = 1; i <= MAX_LEVEL; i++) {
			if (i % 2 == 0) {
				gcoeff += 0.1;
			}
			gainedxpcoeffs.put(i, gcoeff);
			xpRequiredToNextLevel = prev + (long) (xpRequiredToNextLevel * xpcoeff);
			double mobs = (xpRequiredToNextLevel - prev) / getTotalXp(EnemyLevel.CREATURE.getBaseXP(), i, i, Enemy.DEFAULT_COEFF);
			double sections = (xpRequiredToNextLevel - prev) / getTotalXp(BASE_SECTION_XP, i, i, 1d);
			
			Log.i("Experience", xpRequiredToNextLevel + " at level " + i);
			Log.i("Experience", gcoeff + " at level " + i);
			Log.i("Experience", "mobs to level " + (mobs));
			Log.i("Experience", "sections to level " + (sections));
			Log.i("Experience", "----------");
			experienceMap.put(i, xpRequiredToNextLevel);
			prev = xpRequiredToNextLevel;
			if (i == 1) {
				gcoeff = 0.1;
			}
		}
	}

	public static ExperienceMap getInstance() {
		return instance;
	}

	public long getExperienceByLevel(int lvl) {
		return this.experienceMap.get(lvl);
	}

	public long getXpFromSection(StorySection section, int playerLevel) {
		return getTotalXp(BASE_SECTION_XP, section.getLevel(), playerLevel, section.getXpcoeff());
	}

	

	private double getLevelDifferenceCoeff(int level, int playerlevel) {
		double ref = ((double)level / (double)playerlevel);
		return ref;
	}

	public long getGainedExperienceFromEnemy(Enemy enemy, int playerLevel) {
		return getTotalXp(enemy.getEnemyLevel().getBaseXP(), enemy.getLevel(), playerLevel, enemy.getXpcoeff());
	}
	private long  getTotalXp(long basexp, int level, int playerlevel, double additionalCoeff) {
		double coeff = gainedxpcoeffs.get(level);
		long xp = (long) (((basexp + (basexp * level * coeff)) 
				* additionalCoeff) * getLevelDifferenceCoeff(level, playerlevel));
		return xp;
	}
	public void updateStatsByLevel(Character character) {
		for (StatType type : StatType.values()) {
			try {
				int val = type.getBaseValue();
				if (type.equals(character.getPrimaryStat())) {
					val = val * 2;
				}
				if (character.getLevel() % type.getIncreaseByLevel() != 0) {
					val = 0;
				}
				int baseVal = val + GameBookUtils.getStatByType(character.getStats(), type);
				GameBookUtils.setStatByType(character.getStats(), type, baseVal);
				int currVal = val + GameBookUtils.getStatByType(character.getCurrentStats(), type);
				GameBookUtils.setStatByType(character.getCurrentStats(), type, currVal);
			} catch (Exception e) {
				Log.e("StatsIncreasingBylevel", "", e);
			}
		}
	}

	public int getMaxLevel() {
		return MAX_LEVEL;
	}
}
