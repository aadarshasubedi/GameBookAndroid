package com.nex.gamebook.combat;

import java.util.ArrayList;
import java.util.List;

import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.Enemy;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Enemy.EnemyLevel;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.ResultCombat;
import com.nex.gamebook.playground.BattleLogCallback;
import com.nex.gamebook.skills.active.OvertimeSkill;
import com.nex.gamebook.skills.active.Skill;
import com.nex.gamebook.skills.passive.AreaOfDamage;
import com.nex.gamebook.skills.passive.AttackBuff;
import com.nex.gamebook.skills.passive.DefenseBuff;
import com.nex.gamebook.skills.passive.DodgeIsSkill;
import com.nex.gamebook.skills.passive.Leech;
import com.nex.gamebook.skills.passive.LuckIsSkillpower;

public class CombatProcess {

	private Enemy enemy;
	private List<Enemy> otherEnemies;
	int turn = 0;

	public CombatProcess(Enemy enemy, List<Enemy> otherEnemies) {
		super();
		this.enemy = enemy;
		this.otherEnemies = otherEnemies;
	}

	// public ResultCombat doNormalAttack(Character attacker, Character
	// attacked, float modification, boolean allowLuck) {
	// doNormalAttack(attacker, attacked, modification, true);
	// }
	public ResultCombat doNormalAttack(Character attacker, Character attacked, float modification, boolean allowLuck) {
		ResultCombat resultCombat = new ResultCombat();
		resultCombat.setType(attacker.getType());
		if(!attacker.isCanAttack())  {
			resultCombat.setCannotAttack(true);
			attacker.setCanAttack(true);
			return resultCombat;
		}
		if (allowLuck)
			resultCombat.setLuck(attacked.hasLuck());
		if (!resultCombat.isLuck()) {
			resultCombat.setCritical(attacker.isCriticalChance());
			int attack = attacker.getCurrentStats().getTotalPureDamage();
			int defense = attacked.getCurrentStats().getDefensePercentage();
			int totalDamage = (attack - (int) (((double) attack / 100d) * defense));
			totalDamage *= modification;
			attacker.getStatistics().addAttackGivenDamage(totalDamage);
			attacked.getStatistics().addAttackTakenDamage(totalDamage);
			attacker.getStatistics().addAttackReducedDamage(attack - totalDamage);
			if (resultCombat.isCritical()) {
				attacker.getStatistics().addCriticalHit();
				double criticalMultiplier = attacker.hasLuck() ? 1 : 0.5;
				resultCombat.setMultiply(criticalMultiplier);
				totalDamage += totalDamage * criticalMultiplier;
				processPassiveWhenCriticalHit(attacker, attacked);
			}
			processPassiveSkills(attacker, totalDamage);
			resultCombat.setDamage(totalDamage);
			int attackedHealth = attacked.getCurrentStats().getRealHealth();
			attacked.getCurrentStats().setHealth(attackedHealth - resultCombat.getDamage());
		} else {
			attacker.getStatistics().addMissedAttack();
			attacked.getStatistics().addDodgedAttack();
			DodgeIsSkill askill = (DodgeIsSkill) attacker.findPassiveSkill(DodgeIsSkill.class);
			if(askill!=null) {
				Bonus b = createBonus(StatType.SKILL, askill.power(attacked));
				attacked.addBonus(b);
			}
		}
		resultCombat.setEnemyName(enemy.getName());
		return resultCombat;
	}
	private void processPassiveWhenCriticalHit(Character attacker, Character attacked) {
		AttackBuff askill = (AttackBuff) attacker.findPassiveSkill(AttackBuff.class);
		if(askill!=null) {
			int buff = askill.power(attacker);
			attacker.getConditions().add(createBonus(StatType.ATTACK, buff));
		}
		DefenseBuff dskill = (DefenseBuff) attacker.findPassiveSkill(DefenseBuff.class);
		if(dskill!=null) {
			int buff = dskill.power(attacked);
			attacked.getConditions().add(createBonus(StatType.DEFENSE, buff));
		}
	}
	
	private Bonus createBonus(StatType type, int val) {
		Bonus b = new Bonus();
		b.setType(type);
		b.setCoeff(1);
		b.setValue(val);
		b.setTurns(4);
		return b;
	}
	
	private void processPassiveSkills(Character attacker, int totaldmg) {
		processAOEDmg(attacker, totaldmg);
		processLeech(attacker);
		processLuckIsSkillPower(attacker);
	}
	private void processLuckIsSkillPower(Character attacker) {
		LuckIsSkillpower skill = (LuckIsSkillpower) attacker.findPassiveSkill(LuckIsSkillpower.class);
		if(skill!=null) {
			if(attacker.hasLuck()) {
				attacker.getConditions().add(createBonus(StatType.SKILLPOWER, skill.power(attacker)));
			}
		}
	}
	private void processLeech(Character attacker) {
		Leech leechSkill = (Leech) attacker.findPassiveSkill(Leech.class);
		if(leechSkill!=null) {
			int currentHealth = attacker.getCurrentStats().getRealHealth();
			currentHealth += (attacker.getStats().getRealHealth() / 100) * leechSkill.power(attacker);
			if(currentHealth>attacker.getStats().getRealHealth()) {
				currentHealth = attacker.getStats().getRealHealth();
			}
			attacker.getCurrentStats().setHealth(currentHealth);
		}
	}
	
	private void processAOEDmg(Character attacker, int totaldmg) {
		AreaOfDamage aoeSkill = (AreaOfDamage) attacker.findPassiveSkill(AreaOfDamage.class);
		if(aoeSkill!=null) {
			int aoeDamage = (int) (((double)totaldmg / 100d) * aoeSkill.power(attacker));
			for(Enemy e: this.otherEnemies) {
				e.getCurrentStats().setHealth(e.getCurrentStats().getRealHealth() - aoeDamage);
			}
		}
	}
	public ResultCombat doNormalAttack(Character attacker, Character attacked, boolean allowLuck) {
		return doNormalAttack(attacker, attacked, 1, allowLuck);
	}

	public void fight(BattleLogCallback callback) {
		Player player = (Player) callback.getCharacter();

		callback.divide(++turn);
		boolean enemyBegin = !player.hasLuck();
		doOvertimeSkills(player, enemy, callback);
		if (enemyBegin) {
			if (!doSpecialAttack(enemy, player, callback, true)) {
				doSpecialAttack(player, enemy, callback, false);
			}
			Skill playerSkill = player.getSelectedSkill();
			Skill enemySkill = enemy.getSelectedSkill();
			if(enemySkill!=null && enemySkill.isTriggerOnEndOfRound()) {
				doSkill(enemy, player, enemy, enemySkill, callback, null);
			}
			if(playerSkill!=null && playerSkill.isTriggerOnEndOfRound()) {
				doSkill(player, enemy, player, playerSkill, callback, null);
			}
		} else {
			if (!doSpecialAttack(player, enemy, callback, true)) {
				doSpecialAttack(enemy, player, callback, false);
			}
			Skill playerSkill = player.getSelectedSkill();
			Skill enemySkill = enemy.getSelectedSkill();
			if(playerSkill!=null && playerSkill.isTriggerOnEndOfRound()) {
				doSkill(player, enemy, player, playerSkill, callback, null);
			}
			if(enemySkill!=null && enemySkill.isTriggerOnEndOfRound()) {
				doSkill(enemy, player, enemy, enemySkill, callback, null);
			}
		}
		doConditionEffects(player, enemy);
		
		if (enemy.isDefeated() || player.isDefeated()) {
			player.cleanActiveSkillsAfterFightEnd();
			long exp = enemy.getXp(player.getLevel());
			if (player.isDefeated()) {
				exp = 0;
			}
			player.setSelectedSkill(null);
			if(!player.isDefeated()) {
				player.getStatistics().addKilledEnemy();
				if(enemy.getEnemyLevel().equals(EnemyLevel.BOSS)) {
					player.getStatistics().addKilledBoss();
				} else if(enemy.getEnemyLevel().equals(EnemyLevel.MINION)) {
					player.getStatistics().addKilledMinion();
				} else {
					player.getStatistics().addKilledMob();
				}
			}
			callback.fightEnd(exp);
		}
	}

	private void doConditionEffects(Character c1, Character c2) {
		doConditionEffects(c1);
		doConditionEffects(c2);
	}
	
	private void doConditionEffects(Character c) {
		List<Bonus> releaseThese = new ArrayList<Bonus>();
		for(Bonus b: c.getConditions()) {
			
			if(b.isExhausted()) {
				releaseThese.add(b);
			}
			b.setCurrentTurn(b.getCurrentTurn() + 1);
		}
		c.getConditions().removeAll(releaseThese);
	}
	
	private void doOvertimeSkills(Character attacker, Character attacked, BattleLogCallback callback) {
		doOvertimeSkill(attacker, attacked, callback);
		doOvertimeSkill(attacked, attacker, callback);
	}

	private void doOvertimeSkill(Character attacker, Character attacked, BattleLogCallback callback) {
		List<OvertimeSkill> releaseThese = new ArrayList<>();
		for (OvertimeSkill skill : attacker.getOvertimeSkills()) {
			boolean available = skill.execute(attacker, attacked, callback, null);
			if (!available)
				releaseThese.add(skill);
		}
		attacker.getOvertimeSkills().removeAll(releaseThese);
	}
	
	private void choseSkillForAI(Character attacker, Character attacked) {
		if (attacker instanceof Enemy) {
			attacker.chooseBestSkill(attacked, true);
		} else {
			attacked.chooseBestSkill(attacker, false);
		}
	}

	private boolean doSpecialAttack(Character attacker, Character attacked, BattleLogCallback callback, boolean canChooseAISkill) {
		if (canChooseAISkill)
			choseSkillForAI(attacker, attacked);
		Skill attackerSkill = attacker.getSelectedSkill();
		Skill skill = attacked.getSelectedSkill();
		if(attackerSkill!=null && attackerSkill.isTriggerOnEndOfRound()) {
			attackerSkill = null;
		}
		if(skill!=null && skill.isTriggerOnEndOfRound()) {
			skill = null;
		}
		boolean usedBeforeSkill = false;
		boolean doAttack = true;
		if (skill != null && (skill.isTriggerBeforeEnemyAttack() || skill.isTriggerBeforeEnemySpecialAttack())) {
			if (attackerSkill != null && attackerSkill.isTriggerBeforeEnemySpecialAttack() && !skill.isTriggerBeforeEnemySpecialAttack()) {
				doSkill(attacker, attacked, attacker, attackerSkill, callback, null);
				usedBeforeSkill = true;
			}
			doSkill(attacked, attacker, attacked, skill, callback, null);
			if (attacker.isDefeated()) {
				return true;
			}
		}

		if (!usedBeforeSkill && attackerSkill != null && attackerSkill.afterNormalAttack()) {
			ResultCombat result = doNormalAttack(attacker, attacked, true);
			callback.logAttack(result);
			if (skill != null && skill.isTriggerAfterEnemyAttack()) {
				doSkill(attacked, attacker, attacked, skill, callback, result);
				if (attacker.isDefeated()) {
					return true;
				}
			}
			doSkill(attacker, attacked, attacker, attackerSkill, callback, result);
		} else {

			if (!usedBeforeSkill && attackerSkill != null && !attackerSkill.isTriggerAfterEnemyAttack() && !attackerSkill.isTriggerBeforeEnemyAttack()) {
				doSkill(attacker, attacked, attacker, attackerSkill, callback, null);
			}
			if (doAttack) {
				ResultCombat result = doNormalAttack(attacker, attacked, true);
				callback.logAttack(result);
				if (skill != null && skill.isTriggerAfterEnemyAttack()) {
					doSkill(attacked, attacker, attacked, skill, callback, result);
					if (attacker.isDefeated()) {
						return true;
					}
				}
			}
		}

		return attacked.isDefeated();
	}
	
	private void doSkill(Character attacker, Character attacked, Character skillOwner, Skill skill, BattleLogCallback callback, ResultCombat resultCombat) {
		skillOwner.getStatistics().addUsedSkill();
		if(skillOwner.isCanCastSkill()) {
			skill.doAttack(attacker, attacked, callback, resultCombat);
		} else {
			logIfCannotCast(callback, skillOwner);
			skillOwner.setCanCastSkill(true);
		}
	}
	
	private void logIfCannotCast(BattleLogCallback callback, Character c) {
		ResultCombat interruptRC = new ResultCombat();
		Skill skill = c.getSelectedSkill();
		
		if(!c.isCanCastSkill() && skill!=null && skill.canUse()) {
			interruptRC.setType(c.getType());
			interruptRC.setCannotCast(true);
			callback.logAttack(interruptRC);
		}
	}
	
}
