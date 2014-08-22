package com.nex.gamebook.entity;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.entity.io.GameBookUtils;

public class Enemy extends com.nex.gamebook.entity.Character {

	public enum EnemyLevel {
		CREATURE(R.string.enemy_creature), MINION(R.string.enemy_minion), BOSS(R.string.enemy_boss);
		private int code;
		private EnemyLevel(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

		public static EnemyLevel getLevelByString(String s) {
			if (s == null || "".equals(s)) {
				return EnemyLevel.CREATURE;
			}
			return EnemyLevel.valueOf(s.toUpperCase());
		}
	}

	private static final long serialVersionUID = -1969817378614053107L;
	private String name;
	private int index;
	private boolean affectPlayer;
	private EnemyLevel level;

	public Enemy() {
	}

	public Enemy(Enemy enemy, SpecialSkill specialSkill) {
		this(enemy);
		setSpecialSkill(specialSkill);
	}
	
	public Enemy(Enemy enemy) {
		super(enemy);
		this.name = enemy.name;
		this.level = enemy.level;
	}

	public String getName() {
		return GameBookUtils.getInstance().getText(name, getStory());
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAffectPlayer() {
		return affectPlayer;
	}

	public void setAffectPlayer(boolean affectPlayer) {
		this.affectPlayer = affectPlayer;
	}

	@Override
	public CharacterType getType() {
		return CharacterType.ENEMY;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public EnemyLevel getLevel() {
		return level;
	}

	public void setLevel(EnemyLevel level) {
		this.level = level;
	}

}
