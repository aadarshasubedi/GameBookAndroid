package com.nex.gamebook.attack.special;

import com.nex.gamebook.attack.special.skill.conditional.DecreaseAttribute;
import com.nex.gamebook.attack.special.skill.conditional.IncreaseAttribute;
import com.nex.gamebook.attack.special.skill.overtime.DecreaseHealthOvertime;
import com.nex.gamebook.attack.special.skill.overtime.DecreaseHealthOvertimeGreater;
import com.nex.gamebook.attack.special.skill.overtime.IncreaseHealthOvertime;
import com.nex.gamebook.attack.special.skill.overtime.IncreaseHealthOvertimeGreater;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.SpecialSkillsMap;
import com.nex.gamebook.game.Story;

public class SkillProperties {

	private int attempts = SpecialAttackSkill.NO_VALUE;
	private int turns = SpecialAttackSkill.NO_VALUE;
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
	private boolean permanent;
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

	public SpecialSkill createSpecialSkill(Story story) {
		SpecialSkill skill = resolveSkill();
		skill.setData(this, story.getProperties().getProperty(skillName));
		return skill;
	}

	public boolean isIncrease() {
		return increase;
	}

	public void setIncrease(boolean increase) {
		this.increase = increase;
	}

	private SpecialSkill resolveSkill() {
		if (proprietarySkill != null && proprietarySkill.length() > 0) {
			SpecialSkill skill = SpecialSkillsMap.get(proprietarySkill);
			return skill;
		}
		if (increase) {
			if (turns > 0) {
				if (type != null) {
					return new IncreaseHealthOvertimeGreater(type);
				}
				return new IncreaseHealthOvertime();
			} else {
				return new IncreaseAttribute(type);
			}
		} else {
			if (turns > 0) {
				if (type != null) {
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

}
