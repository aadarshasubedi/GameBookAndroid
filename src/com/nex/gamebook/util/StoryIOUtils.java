package com.nex.gamebook.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import ae.com.sun.xml.bind.v2.ContextFactory;
import ae.javax.xml.bind.JAXBContext;
import ae.javax.xml.bind.JAXBException;
import ae.javax.xml.bind.Unmarshaller;
import android.util.Log;

import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Bonus.BonusState;
import com.nex.gamebook.game.Character;
import com.nex.gamebook.game.Enemy;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.Stats;
import com.nex.gamebook.game.StorySectionOption;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Enemy.EnemyLevel;
import com.nex.gamebook.game.StorySection;
import com.nex.gamebook.skills.active.SkillProperties;
import com.nex.gamebook.xsd.BonusType;
import com.nex.gamebook.xsd.BonusesType;
import com.nex.gamebook.xsd.CharacterType;
import com.nex.gamebook.xsd.CharactersType;
import com.nex.gamebook.xsd.EnemiesType;
import com.nex.gamebook.xsd.EnemyType;
import com.nex.gamebook.xsd.IncludeType;
import com.nex.gamebook.xsd.OptionType;
import com.nex.gamebook.xsd.OptionsType;
import com.nex.gamebook.xsd.SectionType;
import com.nex.gamebook.xsd.SectionsType;
import com.nex.gamebook.xsd.Skill;
import com.nex.gamebook.xsd.Skills;
import com.nex.gamebook.xsd.Story;

public class StoryIOUtils {

	public static Story loadStoryType(String filePath) throws JAXBException {
		try {
			Class.forName(ContextFactory.class.getName());
		} catch (ClassNotFoundException e) {
			throw new JAXBException(e);
		}
		JAXBContext jc = JAXBContext.newInstance(Story.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		File file = new File(filePath);
		Story storyRoot = (Story) unmarshaller.unmarshal(file);
		return storyRoot;
	}

	public static com.nex.gamebook.game.Story loadFullContentAsStory(String path) throws JAXBException {
		Story story = loadStoryType(path + File.separator + "root.xml");
		List<Story> data = new ArrayList<Story>();
		for (IncludeType i : story.getInclude()) {
			data.add(loadStoryType(path + File.separator + i.getName()));
		}
		return process(data);
	}

	public static com.nex.gamebook.game.Story process(List<Story> stories) {
		com.nex.gamebook.game.Story story = new com.nex.gamebook.game.Story();
		for (Story s : stories) {
			if(s.getCharacters()!=null) {
				processCharacters(story, s.getCharacters());
			}
			if(s.getSkills()!=null) {
				processSkills(story, s.getSkills());
			}
			if(s.getEnemies()!=null) {
				processEnemies(story, s.getEnemies());
			}
			if(s.getSections()!=null) {
				processSections(story, s.getSections());
			}
		}
		
		story.assignEnemiesToSections();
		story.assignSkillsToCharacters();
		return story;
	}

	private static void processCharacters(com.nex.gamebook.game.Story story, CharactersType characters) {
		for(CharacterType t: characters.getCharacter()) {
			Player player = new Player();
			Stats s = new Stats(true);
			s.setCharacter(player);
			s.setDamage(t.getBaseDamage());
			s.setAttack(t.getAttack());
			s.setSkill(t.getSkill());
			s.setSkillpower(t.getSkillPower());
			s.setHealth(t.getHealth());
			s.setLuck(t.getLuck());
			s.setDefense(t.getDefense());
			player.setStats(s);
			player.setCurrentStats(new Stats(s, false));
			player.setName(t.getName());
			player.setDescription(t.getDescription());
			player.setPrimaryStat(StatType.valueOf(t.getPrimaryStat().value()));
			player.setPosition(t.getPosition());
			player.getAssignedSkills().addAll(t.getSkills().getRef());
			story.getCharacters().add(player);
		}
	}

	private static void processEnemies(com.nex.gamebook.game.Story story, EnemiesType enemies) {
		for(EnemyType t: enemies.getEnemy()) {
			Enemy e = new Enemy();
			Stats s = new Stats(true);
			s.setCharacter(e);
			s.setDamage(t.getBaseDamage());
			s.setAttack(t.getAttack());
			s.setSkill(t.getSkill());
			s.setSkillpower(t.getSkillPower());
			s.setHealth(t.getHealth());
			s.setLuck(t.getLuck());
			s.setDefense(t.getDefense());
			if(t.getSkills()!=null) {
				e.getAssignedSkills().addAll(t.getSkills().getRef());
			}
			e.setName(t.getName());
			e.setEnemyLevel(EnemyLevel.valueOf(t.getType().value()));
			e.setXpcoeff(t.getXpcoeff());
			story.getEnemies().put(t.getId(), e);
		}
	}

	private static void processSkills(com.nex.gamebook.game.Story story, Skills skills) {
		for(Skill s: skills.getSpecialSkill()) {
			SkillProperties p = loadSkill(s);
			story.getSkills().put(s.getId(), p);
		}
	}
	private static SkillProperties loadSkill(Skill s) {
		SkillProperties skill = new SkillProperties();
		skill.setSkillName(s.getSkillName());
		skill.setProprietarySkill(s.getProprietarySkill().value());
		skill.setAttempts(s.getAttempts());
		skill.setTurns(s.getTurns());
		skill.setLevelRequired(s.getLevelRequired());
		String type = s.getType().value();
		if(type!=null && type.length()>0)
		skill.setType(StatType.valueOf(type.toUpperCase()));
		skill.setIncrease(s.isIncrease());
		skill.setCoeff(s.getCoeff());
		skill.setBeforeEnemyAttack(s.isBeforeEnemyAttack());
		skill.setBeforeEnemySkill(s.isBeforeEnemySkill());
		skill.setAfterEnemyAttack(s.isAfterEnemyAttack());
		skill.setAfterNormalAttack(s.isAfterNormalAttack());
		skill.setPermanent(s.isPermanent());
		skill.setCondition(s.isCondition());
		skill.setOnEndOfRound(s.isOnEndOfRound());
		skill.setId(s.getId());
		return skill;
	}
	private static void processSections(com.nex.gamebook.game.Story story, SectionsType sections) {
		for(SectionType s: sections.getSection()) {
			StorySection section = new StorySection();
			section.setStory(story);
			if(s.getBonuses()!=null)
				addBonuses(section, s.getBonuses());
			if(s.getEnemies()!=null)
				section.getEnemiesIds().addAll(s.getEnemies().getRef());
			if(s.getOptions()!=null)
				addOptions(section, s.getOptions());
			section.setText(s.getText());
			section.setAlreadyVisitedText(s.getAlreadyVisitedText());
			section.setEnemiesDefeatedText(s.getEnemiesDefeatedText());
			section.setLuckText(s.getLuckText());
			section.setLevel(s.getLevel());
			section.setScoreMultiplier(s.getScoreMultiplier());
			section.setXpcoeff(s.getXpcoeff());
			section.setLoseSection(s.isLoseSection());
			section.setWinSection(s.isWinSection());
			section.setResetAttributes(s.isResetAttributes());
			section.setResetNegativeAttributes(s.isResetNegativeAttributes());
			section.setResetPositiveAttributes(s.isResetPositiveAttributes());
			section.setLuckDefeatEnemies(s.isLuckDefeatEnemies());
			story.getSections().put(s.getPosition(), section);
		}
	}
	private static void addOptions(StorySection s, OptionsType t) {
		for(OptionType o: t.getOption()) {
			StorySectionOption option = new StorySectionOption();
			option.setText(o.getText());
			option.setSkill(o.getSkill());
			option.setLuckAspect(o.isLuckAspect());
			option.setDisableWhenSelected(o.isDisableWhenSelected());
			option.setStory(s.getStory());
			s.getOptions().add(option);
		}
	}
	private static void addBonuses(StorySection s, BonusesType t) {
		for(BonusType b: t.getBonus()) {
			Bonus bonus = new Bonus();
			bonus.setState(BonusState.valueOf(b.getState().value()));
			bonus.setType(StatType.valueOf(b.getType().value()));
			bonus.setValue(b.getValue());
			bonus.setBase(b.isBase());
			bonus.setCoeff(b.isDebuff()?-1:1); 
			bonus.setOverflowed(b.isOverflowed());
			bonus.setPermanent(b.isPermanent());
			s.getBonuses().add(bonus);
		}
	}
	
}
