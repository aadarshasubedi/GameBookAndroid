package com.nex.gamebook.entity;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.nex.gamebook.entity.Bonus.StatType;
import com.nex.gamebook.entity.io.GameBookUtils;

public class ExperienceMap {
	public static final int MAX_LEVEL = 20;

	private Map<Integer, Long> experienceMap = new HashMap<>();
	private Map<Integer, Double> xpcoeffs = new HashMap<>();
	private Map<Integer, Double> gainedxpcoeffs = new HashMap<>();
	private static ExperienceMap instance;
	static {
		instance = new ExperienceMap();
		instance.init();
	}

	private void init() {
		xpcoeffs.put(10, 1.1);
		double xpcoeff = 1.0;
		double gcoeff = 0.5;
		double gcoeffInc = 0.1;
		long xpRequiredToNextLevel = 40;
		for (int i = 1; i <= MAX_LEVEL; i++) {
			if(i==11) {
				gcoeffInc = 0.5;
			}
			gcoeff += gcoeffInc;
			gainedxpcoeffs.put(i, gcoeff);
			xpRequiredToNextLevel = xpRequiredToNextLevel / 2 + (long) (xpRequiredToNextLevel * xpcoeff);
			Log.i("Experience", xpRequiredToNextLevel + " at level " + i);
			Log.i("Experience gaining coefficient", gcoeff + " at level " + i);
			experienceMap.put(i, xpRequiredToNextLevel);
			Double newCoeff = xpcoeffs.get(i);
			if (newCoeff != null) {
				xpcoeff = newCoeff;
			}
		}
	}

	public static ExperienceMap getInstance() {
		return instance;
	}

	public long getExperienceByLevel(int lvl) {
		return this.experienceMap.get(lvl);
	}

	public long getGainedExperienceFromEnemy(Enemy enemy) {
		int enemyLevel = enemy.getLevel();
		long experience = enemy.getEnemyLevel().getBaseXP() * enemyLevel;
		double coeff = gainedxpcoeffs.get(enemyLevel);
		return (long) (experience * coeff);
	}
	
	public void updateStatsByLevel(Character character) {
		for(StatType type: StatType.values()) {
			try {
				int val = type.getBaseValue();
				if(type.equals(character.getPrimaryStat())){
					val = val * 2;
				}
				if(character.getLevel() %type.getIncreaseByLevel() != 0) {
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
}
