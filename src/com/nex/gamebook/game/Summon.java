package com.nex.gamebook.game;

import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.passive.ArmoredSummon;
import com.nex.gamebook.skills.passive.EnragedSummon;
import com.nex.gamebook.skills.passive.PassiveSkill;
import com.nex.gamebook.skills.passive.ToughSummon;

public abstract class Summon extends Character {

	protected Character owner;
	protected int baseHealth;
	public Summon(Character owner, int basehealth) {
		super();
		this.baseHealth = basehealth;
		this.owner = owner;
	}

	public void setSummonStats(Stats stats) {
		stats.setCharacter(this);
		stats.setDefense(getDefenseFromPassive(owner) + stats.getDefense());
		stats.setSkill(getSkillFromPassive() + stats.getSkill());
		stats.setHealth(getHealthFromPassive(owner));
		setCurrentStats(new Stats(stats, false));
		setStats(new Stats(stats, true));
	}

	public abstract void execute(Character attacker, Character attacked, BattleLogCallback callback);

	@Override
	public boolean hasLuck() {
		return false;
	}

	@Override
	public boolean isCriticalChance() {
		return isCriticalChance(Stats.TOTAL_SKILL_FOR_CALC);
	}

	public float getCriticalPower() {
		EnragedSummon ps = (EnragedSummon) owner.findPassiveSkill(EnragedSummon.class);
		if(ps!=null) {
			return ps.getCriticalPower(owner);
		}
		return 0f;
	}

	@Override
	public CharacterType getType() {
		return CharacterType.SUMMON;
	}

	@Override
	public void chooseBestSkill(Character c, boolean enemyBegin) {
		// summon no have skills
	}

	public int getSkillFromPassive() {
		PassiveSkill ps = owner.findPassiveSkill(EnragedSummon.class);
		if(ps!=null) {
			return Stats.getValuePerc(Stats.TOTAL_LUCK_FOR_CALC, ps.power(owner));
		}
		return 0;
	}
	
	public int getDefenseFromPassive(Character owner) {
		ArmoredSummon passive = (ArmoredSummon) owner.findPassiveSkill(ArmoredSummon.class);
		if (passive != null) {
			int percentage = passive.power(owner);
			return Stats.getValuePerc(Stats.TOTAL_ARMOR_FOR_CALC, percentage);
		}
		return 0;
	}
	public int getHealthFromPassive(Character owner) {
		ToughSummon passive = (ToughSummon) owner.findPassiveSkill(ToughSummon.class);
		if (passive != null) {
			int percentage = passive.power(owner);
			return (int) (this.baseHealth + ((double)this.baseHealth/100)*percentage);
		}
		return this.baseHealth;
	}
}
