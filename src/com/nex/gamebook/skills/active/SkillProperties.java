package com.nex.gamebook.skills.active;

import android.util.Log;

import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.SkillMap;
import com.nex.gamebook.game.Story;
import com.nex.gamebook.skills.active.conditional.DecreaseAttribute;
import com.nex.gamebook.skills.active.conditional.IncreaseAttribute;
import com.nex.gamebook.skills.active.overtime.DecreaseHealthOvertime;
import com.nex.gamebook.skills.active.overtime.DecreaseHealthOvertimeGreater;
import com.nex.gamebook.skills.active.overtime.IncreaseHealthOvertime;
import com.nex.gamebook.skills.active.overtime.IncreaseHealthOvertimeGreater;

public class SkillProperties {

	private int attempts = ActiveSkill.NO_VALUE;
	private int turns = ActiveSkill.NO_VALUE;
	private int levelRequired;
	private String proprietarySkill;

	private String skillName = null;
	private StatType type;
	// private StatType greaterType;
	private boolean increase;
	private boolean resetAtBattleEnd;
	private boolean beforeEnemySkill;
	private boolean beforeEnemyAttack;
	private boolean afterEnemyAttack;
	private boolean afterNormalAttack;
	private boolean onEndOfRound;
	private boolean permanent;
	private boolean condition;

	public SkillProperties() {

	}

	public SkillProperties(SkillProperties source) {
		this.proprietarySkill = source.proprietarySkill;
		this.skillName = source.skillName;
		this.increase = source.increase;
		this.resetAtBattleEnd = source.resetAtBattleEnd;
		this.permanent = source.permanent;
		this.coeff = source.coeff;
		this.condition = source.condition;
		this.skillName = source.skillName;
		this.attempts = source.attempts;
		this.turns = source.attempts;
		this.type = source.type;
		this.beforeEnemySkill = source.beforeEnemySkill;
		this.beforeEnemyAttack = source.beforeEnemyAttack;
		this.afterEnemyAttack = source.afterEnemyAttack;
		this.afterNormalAttack = source.afterNormalAttack;

	}

	private String id;
	private float coeff;

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public int getTurns() {
		return turns;
	}

	public void setTurns(int turns) {
		this.turns = turns;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public String getProprietarySkill() {
		return proprietarySkill;
	}

	public void setProprietarySkill(String proprietarySkill) {
		this.proprietarySkill = proprietarySkill;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public void setLevelRequired(int levelRequired) {
		this.levelRequired = levelRequired;
	}

	public StatType getType() {
		return type;
	}

	public void setType(StatType type) {
		this.type = type;
	}

	public Skill createSpecialSkill(Story story) {
		Skill skill = resolveSkill();
		skill.setData(this, story.getProperties().getProperty(skillName));
		return skill;
	}

	public boolean isIncrease() {
		return increase;
	}

	public void setIncrease(boolean increase) {
		this.increase = increase;
	}

	public boolean proprietarySkillExists() {
		boolean exist = proprietarySkill != null && proprietarySkill.trim().length() > 0;
		if (exist) {
			Class<?> cls = SkillMap.getSkills().get(proprietarySkill);
			if (cls == null) {
				Log.e("SkillCreation", "proprieatary skill with name " + proprietarySkill + " not exist");
			} else {
				return true;
			}
		}
		return false;
	}

	private Skill resolveSkill() {
		if (proprietarySkillExists() && turns == 0) {
			Skill skill = SkillMap.get(proprietarySkill);
			return skill;
		}
		if (increase) {
			if (turns > 0 && !condition) {
				if (proprietarySkillExists()) {
					return new IncreaseHealthOvertimeGreater(proprietarySkill);
				} else if (type != null) {
					return new IncreaseHealthOvertimeGreater(type);
				} else {
					return new IncreaseHealthOvertime();
				}
			} else {
				return new IncreaseAttribute(type);
			}
		} else {
			if (turns > 0 && !condition) {
				if (proprietarySkillExists()) {
					return new DecreaseHealthOvertimeGreater(proprietarySkill);
				} else if (type != null) {
					return new DecreaseHealthOvertimeGreater(type);
				} else {
					return new DecreaseHealthOvertime();
				}
			} else {
				return new DecreaseAttribute(type);
			}
		}
	}

	public float getCoeff() {
		return coeff;
	}

	public void setCoeff(float coeff) {
		this.coeff = coeff;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isResetAtBattleEnd() {
		return resetAtBattleEnd;
	}

	public void setResetAtBattleEnd(boolean resetAtBattleEnd) {
		this.resetAtBattleEnd = resetAtBattleEnd;
	}

	public boolean isBeforeEnemyAttack() {
		return beforeEnemyAttack;
	}

	public void setBeforeEnemyAttack(boolean beforeEnemyAttack) {
		this.beforeEnemyAttack = beforeEnemyAttack;
	}

	public boolean isAfterEnemyAttack() {
		return afterEnemyAttack;
	}

	public void setAfterEnemyAttack(boolean afterEnemyAttack) {
		this.afterEnemyAttack = afterEnemyAttack;
	}

	public boolean isAfterNormalAttack() {
		return afterNormalAttack;
	}

	public void setAfterNormalAttack(boolean afterNormalAttack) {
		this.afterNormalAttack = afterNormalAttack;
	}

	public boolean isBeforeEnemySkill() {
		return beforeEnemySkill;
	}

	public void setBeforeEnemySkill(boolean beforeEnemySkill) {
		this.beforeEnemySkill = beforeEnemySkill;
	}

	public boolean isPermanent() {
		return permanent;
	}

	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}

	public boolean isCondition() {
		return condition;
	}

	public void setCondition(boolean condition) {
		this.condition = condition;
	}

	public boolean isOnEndOfRound() {
		return onEndOfRound;
	}

	public void setOnEndOfRound(boolean onEndOfRound) {
		this.onEndOfRound = onEndOfRound;
	}

}
