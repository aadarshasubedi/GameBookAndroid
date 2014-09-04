package com.nex.gamebook.game;

public class SkillRequiredLevel {

	private Integer level;
	private String skillName;

	public SkillRequiredLevel(Integer level, String skillName) {
		super();
		this.level = level;
		this.skillName = skillName;
	}

	public Integer getLevel() {
		return level;
	}

	public String getSkillName() {
		return skillName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((skillName == null) ? 0 : skillName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkillRequiredLevel other = (SkillRequiredLevel) obj;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (skillName == null) {
			if (other.skillName != null)
				return false;
		} else if (!skillName.equals(other.skillName))
			return false;
		return true;
	}

}
