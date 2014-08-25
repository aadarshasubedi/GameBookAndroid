package com.nex.gamebook.entity;

import java.util.HashMap;
import java.util.Map;

public class ExperienceMap {
	private static int MAX_LEVEL = 50;
	
	private Map<Integer, Long> experienceMap = new HashMap<>();
	private Map<Integer, Double> xpcoeffs = new HashMap<>();
	
	private static ExperienceMap instance;
	static {
		instance = new ExperienceMap();
		instance.init();
	}
	
	private void init() {
		xpcoeffs.put(10, 1.6);
		xpcoeffs.put(20, 1.7);
		xpcoeffs.put(30, 1.8);
		xpcoeffs.put(40, 1.9);
		int basexp = 50;
		double xpcoeff = 1.5;
		long xpRequiredToNextLevel = 0;
		for(int i = 1; i <= MAX_LEVEL; i++) {
			xpRequiredToNextLevel =  (long) (xpRequiredToNextLevel + basexp * xpcoeff);
			experienceMap.put(i, xpRequiredToNextLevel);
			Double newCoeff = xpcoeffs.get(i);
			if(newCoeff != null) {
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
	
	public void updateStatsByLevel(Character character) {
		
	}
}
