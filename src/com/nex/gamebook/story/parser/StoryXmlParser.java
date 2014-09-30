package com.nex.gamebook.story.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;

import com.nex.gamebook.game.Bonus;
import com.nex.gamebook.game.Bonus.BonusState;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.game.Enemy;
import com.nex.gamebook.game.Enemy.EnemyLevel;
import com.nex.gamebook.game.EnemyAssign;
import com.nex.gamebook.game.ExperienceMap;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.SkillAssign;
import com.nex.gamebook.game.Stats;
import com.nex.gamebook.game.Story;
import com.nex.gamebook.game.StorySection;
import com.nex.gamebook.game.StorySectionOption;
import com.nex.gamebook.skills.active.SkillProperties;
import com.nex.gamebook.util.GameBookUtils;
import com.nex.gamebook.util.LoadingCallback;
import com.nex.gamebook.xsd.EnemyReference;
import com.nex.gamebook.xsd.SkillReference;

public class StoryXmlParser {
	private final String INCREASE = "increase";
	private final String CHARACTER = "character";
	private final String SECTION = "section";
	private final String TEXT = "text";
	private final String SPECIAL_SKILL = "specialSkill";
	private final String ALREADY_VISITED_TEXT = "alreadyVisitedText";
	private final String INCLUDE = "include";
	private final String VERSION = "version";
	private final String SKILLS = "skills";
	private final String BACKGROUND = "background";
	private final String LEVEL_REQUIRED = "levelRequired";
	private final String RESETATBATTLEEND = "resetAtBattleEnd";
	private final String SKILL_NAME = "skillName";
	private final String ATTEMPTS = "attempts";
	private final String TURNS = "turns";
	private final String PROPRIETARY_SKILL = "proprietarySkill";
	private final String LOSE_SECTION = "loseSection";
	private final String LEVEL = "level";
	private final String WIN_SECTION = "winSection";
	private final String SCORE_MULTIPLIER = "scoreMultiplier";
	private final String PRIMARY_STAT = "primaryStat";
	private final String RESET_ATTRIBUTES = "resetAttributes";
	private final String RESET_POSITIVE_ATTRIBUTES = "resetPositiveAttributes";
	private final String RESET_NEGATIVE_ATTRIBUTES = "resetNegativeAttributes";
	private final String STORY = "story";
	private final String XPCOEFF = "xpcoeff";
	private final String COEFF = "coeff";

	private final String OPTIONS = "options";
	private final String BASE = "base";
	private final String ENEMIES = "enemies";
	private final String ENEMY = "enemy";
	private final String BASE_DAMAGE = "damage";

	private final String BONUSES = "bonuses";
	private final String FIGHT_LUCK_TEXT_SECTION = "luckText";
	private final String GAMEOVER_SECTION = "gameOverText";
	private final String LUCK_DEFEAT_ENEMIES = "luckDefeatEnemies";
	private final String DISABLE_WHEN_SELECTED = "disableWhenSelected";

	private final String POSITION = "position";

	private final String ATTACK = "attack";
	private final String HEALTH = "health";
	private final String DEFENSE = "defense";
	private final String SKILL = "skill";
	private final String LUCK = "luck";
	private final String SKILL_POWER = "skillPower";

	private final String LUCK_ASPECT = "luckAspect";
	private final String ACTIVE_SKILL = "activeSkill";
	private final String NAME = "name";
	private final String DESCRIPTION = "description";
	private final String ID = "id";

	private final String TYPE = "type";

	private final String VALUE = "value";
	// private final String OVERRIDE_SKILL = "overrideSkill";
	private final String OVERFLOWED = "overflowed";
	private final String PERMANENT = "permanent";

	private final String DEBUFF = "debuff";
	private final String STATE = "state";
	private final String SKILL_BASED = "skillBased";

	private final String BEFOREENEMYSKILL = "beforeEnemySkill";
	private final String BEFOREENEMYATTACK = "beforeEnemyAttack";
	private final String AFTERENEMYATTACK = "afterEnemyAttack";
	private final String AFTERNORMALATTACK = "afterNormalAttack";
	private final String CONDITION = "condition";
	private final String ISBASE = "isBase";
	Context context;
	private long totalStorySize;
	private long loadedBytes = 0;
	private LoadingCallback cb;

	public StoryXmlParser(Context context) {
		super();
		this.context = context;

	}

	public StoryXmlParser(Context context, LoadingCallback cb) {
		super();
		this.context = context;
		this.cb = cb;
	}

	public List<Story> loadStories() throws IOException {
		List<Story> stories = new ArrayList<Story>();
		InputStream stream = null;
		try {
			stream = new FileInputStream(GameBookUtils.getInstance().getStoriesFolder("stories.xml"));
			Document document = getDocument(stream);
			NodeList nList = document.getElementsByTagName(STORY);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node node = nList.item(temp);
				if (node instanceof Element) {
					Element el = (Element) node;
					String storyXml = el.getAttribute(NAME);
					Story story = loadStory(storyXml, false);
					stories.add(story);
				}
			}
		} catch (Exception e) {
			Log.e("GameBookStoryParser", e.getMessage(), e);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		return stories;
	}

	public Story loadStory(String xml, boolean fully) throws Exception {
		return loadStory(xml, fully, fully);
	}

	public Story loadStory(String xml, boolean sections, boolean characters) throws Exception {
		Story story = new Story();
		story.saveXmlPath(xml);
		this.totalStorySize = GameBookUtils.getInstance().getTotalSize(story.getPath());
		setLoadedBytes(GameBookUtils.getInstance().loadProperties(story));
		loadStory(story, story.getXml(), sections, characters);
//		story.assignEnemiesToSections();
		story.assignSkillsToCharacters();
		Log.i("StoryParser", "all resources loaded");
		if(cb!=null)
		cb.finished();
		return story;
	}

	
	
	private void setLoadedBytes(long bytes) {
		this.loadedBytes += bytes;
		Log.i("StoryLoading", "totalsize:"+totalStorySize + " loadedsize:"+loadedBytes);
		if(cb!=null)
		cb.loaded(getCurrentLoaded());
	}

	public int getCurrentLoaded() {
		return (int) (((double)loadedBytes / (double)totalStorySize) * 100);
	}

	public void loadStory(Story story, String xml, boolean sections, boolean characters) throws Exception {

		InputStream stream = null;
		try {
			File storiesFolder = GameBookUtils.getInstance().getStoriesFolder(story.getPath() + File.separator + xml);
			stream = new FileInputStream(storiesFolder);
			Document document = getDocument(stream);
			NodeList nList = document.getElementsByTagName(STORY);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node node = nList.item(temp);
				if (node instanceof Element) {
					Element el = (Element) node;
					include(story, el, sections, characters);
					NodeList background = el.getElementsByTagName(BACKGROUND);
					if (background.getLength() > 0)
						story.setBackground(getIdentifier(background.item(0).getTextContent()));
					NodeList version = el.getElementsByTagName(VERSION);
					if (version.getLength() > 0)
						story.setVersion(getInteger(version.item(0).getTextContent()));
//					NodeList id = el.getElementsByTagName(ID);
//					if (id.getLength() > 0 && story.getId()==0)
//						story.setVersion(getInteger(version.item(0).getTextContent()));
//					else
//						throw new IllegalStateException("root.xml must contain <id></id> of story " + xml);
					initializeStory(story, document, sections, characters);
				}
			}
			setLoadedBytes(storiesFolder.length());
		} catch (Exception e) {
			throw e;
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	private void include(Story story, Element el, boolean sections, boolean characters) throws Exception {
		NodeList includes = el.getElementsByTagName(INCLUDE);
		for (int i = 0; i < includes.getLength(); i++) {
			Node node = includes.item(i);
			if (node instanceof Element) {
				String includeXml = ((Element) node).getAttribute(NAME);
				loadStory(story, includeXml, sections, characters);
			}

		}
	}

	public void initializeStory(Story story, Document document, boolean sections, boolean characters) throws Exception {
		if (!sections && !characters)
			return;
		if (sections) {
			loadSections(document, story);
			loadEnemies(document, story);
		}
		if (characters) {
			loadCharacters(document, story);
			loadSkills(document, story);
		}
	}

	private void loadSections(Document document, Story story) throws Exception {
		Log.i("StoryParser", "Loading sections....");
		NodeList nList = document.getElementsByTagName(SECTION);
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			if (node instanceof Element) {
				Element el = (Element) node;
				loadSection(story, el);
			}
		}
	}

	private void loadCharacters(Document document, Story story) throws Exception {
		Log.i("StoryParser", "Loading characters....");
		NodeList nList = document.getElementsByTagName(CHARACTER);
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			
			if (node instanceof Element) {
				Element el = (Element) node;
				loadCharacter(story, el);
			}
		}
	}

	private void loadEnemies(Document document, Story story) {
		Log.i("StoryParser", "Loading enemies....");
		NodeList nList = document.getElementsByTagName(ENEMY);
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			
			if (node instanceof Element) {
				Element el = (Element) node;
				loadEnemy(story, el);
			}
		}
	}

	private void loadSkills(Document document, Story story) {
		Log.i("StoryParser", "Loading skills....");
		NodeList nList = document.getElementsByTagName(SPECIAL_SKILL);
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			if (node instanceof Element) {
				Element el = (Element) node;
				loadSkill(story, el);
			}
		}
	}

	private void loadEnemy(Story story, Element element) {
		Enemy enemy = new Enemy();
		enemy.setName(element.getAttribute(NAME));
		String id = element.getAttribute(ID);
		enemy.setEnemyLevel(EnemyLevel.getLevelByString(element.getAttribute(TYPE)));
		enemy.setXpcoeff(getFloat(element.getAttribute(XPCOEFF), Enemy.DEFAULT_COEFF));
		NodeList optionsList = element.getChildNodes();
		for (int i = 0; i < optionsList.getLength(); i++) {
			Node node = element.getChildNodes().item(i);
			putStats(enemy, node);
		}
		enemy.setStory(story);
		story.getEnemies().put(id, enemy);
	}

	private void loadSkill(Story story, Element element) {
		SkillProperties skill = new SkillProperties();
		String id = element.getAttribute(ID);
		skill.setSkillName(element.getAttribute(SKILL_NAME));
		skill.setProprietarySkill(element.getAttribute(PROPRIETARY_SKILL));
		skill.setAttempts(getInteger(element.getAttribute(ATTEMPTS)));
		skill.setTurns(getInteger(element.getAttribute(TURNS)));
		skill.setLevelRequired(getInteger(element.getAttribute(LEVEL_REQUIRED)));
		String type = element.getAttribute(TYPE);
		if (type != null && type.length() > 0)
			skill.setType(StatType.valueOf(type.toUpperCase()));
		skill.setIncrease(getBoolean(element.getAttribute(INCREASE)));
		skill.setCoeff(getFloat(element.getAttribute(COEFF)));
		skill.setResetAtBattleEnd(getBoolean(element.getAttribute(RESETATBATTLEEND)));
		skill.setBeforeEnemyAttack(getBoolean(element.getAttribute(BEFOREENEMYATTACK)));
		skill.setBeforeEnemySkill(getBoolean(element.getAttribute(BEFOREENEMYSKILL)));
		skill.setAfterEnemyAttack(getBoolean(element.getAttribute(AFTERENEMYATTACK)));
		skill.setAfterNormalAttack(getBoolean(element.getAttribute(AFTERNORMALATTACK)));
		skill.setPermanent(getBoolean(element.getAttribute(PERMANENT)));
		skill.setCondition(getBoolean(element.getAttribute(CONDITION)));
		skill.setOnEndOfRound(getBoolean(element.getAttribute("onEndOfRound")));
		skill.setId(id);
		story.getSkills().put(id, skill);
	}

	private void loadCharacter(Story story, Element element) throws Exception {
		Player character = new Player();
		character.setName(element.getAttribute(NAME));
		character.setDescription(element.getAttribute(DESCRIPTION));
		character.setId(getInteger(element.getAttribute(ID)));
		character.setPosition(getInteger(element.getAttribute(POSITION)));
		NodeList optionsList = element.getChildNodes();
		for (int i = 0; i < optionsList.getLength(); i++) {
			Node node = element.getChildNodes().item(i);
			putStats(character, node);
		}
		character.getStats().setCharacter(character);
		character.getCurrentStats().setCharacter(character);
		character.setStory(story);
		ExperienceMap.getInstance().updateStatsByLevel(character);
		story.getCharacters().add(character);

	}

	private void putStats(com.nex.gamebook.game.Character character, Node node) {
		if (node.getNodeName().equals(HEALTH)) {
			character.getStats().setHealth(getInteger(node.getTextContent()));
		} else if (node.getNodeName().equals(DEFENSE)) {
			character.getStats().setDefense(getInteger(node.getTextContent()));
		} else if (node.getNodeName().equals(SKILL)) {
			character.getStats().setSkill(getInteger(node.getTextContent()));
		} else if (node.getNodeName().equals(LUCK)) {
			character.getStats().setLuck(getInteger(node.getTextContent()));
		} else if (node.getNodeName().equals(ATTACK)) {
			character.getStats().setAttack(getInteger(node.getTextContent()));
		} else if (node.getNodeName().equals(BASE_DAMAGE)) {
			character.getStats().setDamage(getInteger(node.getTextContent()));
		} else if (node.getNodeName().equals(SKILLS)) {
			NodeList optionsList = node.getChildNodes();
			for (int i = 0; i < optionsList.getLength(); i++) {
				Node n = optionsList.item(i);
				if (n instanceof Element) {
					Element element = (Element) n;
					String skillKey = element.getAttribute(VALUE);
					SkillReference r = new SkillReference();
					r.setValue(skillKey);
					character.getAssignedSkills().add(r);
				}
			}
		} else if (node.getNodeName().equals(SKILL_POWER)) {
			character.getStats().setSkillpower(getInteger(node.getTextContent()));
		} else if (node.getNodeName().equals(PRIMARY_STAT)) {
			StatType stat = StatType.valueOf(node.getTextContent().toUpperCase());
			if (StatType.DAMAGE.equals(stat)) {
				Log.w("StoryXMLParser", "damage cannot be primary attribute");
			} else {
				character.setPrimaryStat(stat);
			}
		}
		character.setCurrentStats(new Stats(character.getStats(), false));
	}

	private void loadSection(Story story, Element element) throws Exception {
		
		int position = getInteger(element.getAttribute(POSITION));
		if (position == 0) {
			throw new IllegalStateException("position attribute must exist on <section />");
		}
		StorySection section = new StorySection();
		section.setStory(story);
		int level = getInteger(element.getAttribute(LEVEL));
		if (level == 0) {
			level = 1;
		}
		section.setLevel(level);
		section.setLoseSection(getBoolean(element.getAttribute(LOSE_SECTION)));
		section.setWinSection(getBoolean(element.getAttribute(WIN_SECTION)));
		section.setResetAttributes(getBoolean(element.getAttribute(RESET_ATTRIBUTES)));
		section.setResetPositiveAttributes(getBoolean(element.getAttribute(RESET_POSITIVE_ATTRIBUTES)));
		section.setResetNegativeAttributes(getBoolean(element.getAttribute(RESET_NEGATIVE_ATTRIBUTES)));
		section.setScoreMultiplier(getFloat(element.getAttribute(SCORE_MULTIPLIER)));
		section.setXpcoeff(getFloat(element.getAttribute(XPCOEFF)));
		String text = element.getAttribute(TEXT);
		section.setText(text);
		section.setAlreadyVisitedText(text);
		String alreadyVisitedText = element.getAttribute(ALREADY_VISITED_TEXT);
		if (alreadyVisitedText != null && alreadyVisitedText.length() > 0)
			section.setAlreadyVisitedText(alreadyVisitedText);

		String enemiesDefeatedText = element.getAttribute("enemiesDefeatedText");
		String luckText = element.getAttribute(FIGHT_LUCK_TEXT_SECTION);
		if (luckText != null && luckText.trim().length() > 0) {
			section.setLuckPossible(true);
			section.setLuckText(luckText);
		}
		String gameOverText = element.getAttribute(GAMEOVER_SECTION);
		if (gameOverText != null && gameOverText.trim().length() > 0)
			section.setGameOverText(gameOverText);
		section.setLuckDefeatEnemies(getBoolean(element.getAttribute(LUCK_DEFEAT_ENEMIES)));
		NodeList optionsList = element.getChildNodes();
		for (int i = 0; i < optionsList.getLength(); i++) {
			Node node = element.getChildNodes().item(i);
			if (node.getNodeName().equals(OPTIONS)) {
				createOptions(section, node);
			} else if (node.getNodeName().equals(ENEMIES)) {
				createEnemiesReferences(section, node);
			} else if (node.getNodeName().equals(BONUSES)) {
				createBonuses(section, node);
			}
		}
		if (!section.getEnemies().isEmpty() && enemiesDefeatedText == null) {
			throw new IllegalStateException("enemiesDefeatedText attribute must exist if <section position=" + position + "/> has enemies");
		}
		section.setEnemiesDefeatedText(enemiesDefeatedText);
		story.getSections().put(position, section);
	}

	private int getIdentifier(String name) {
		String[] splited = name.split("@|/");
		if (splited.length > 1)
			return context.getResources().getIdentifier(splited[2], splited[1], context.getPackageName());
		return 0;
	}

	private void createEnemiesReferences(StorySection section, Node node) {
		NodeList optionsList = node.getChildNodes();
		for (int i = 0; i < optionsList.getLength(); i++) {
			Node n = optionsList.item(i);
			if (n instanceof Element) {
				Element enemyNode = (Element) n;
				EnemyReference r = new EnemyReference();
				r.setValue(enemyNode.getAttribute(VALUE));
				r.setXpcoeff(getFloat(enemyNode.getAttribute(XPCOEFF)));
				section.getEnemiesIds().add(r);
			}
		}
	}

	private void createBonuses(StorySection section, Node node) {
		NodeList optionsList = node.getChildNodes();
		for (int i = 0; i < optionsList.getLength(); i++) {
			Node n = optionsList.item(i);
			if (n instanceof Element) {
				Element optionNode = (Element) n;
				Bonus bonus = new Bonus();
				bonus.setType(StatType.valueOf(optionNode.getAttribute(TYPE).toUpperCase()));
				bonus.setValue(getInteger(optionNode.getAttribute(VALUE)));
				bonus.setOverflowed(getBoolean(optionNode.getAttribute(OVERFLOWED)));
				Boolean coeff = getBoolean(optionNode.getAttribute(DEBUFF));
				bonus.setCoeff(coeff ? -1 : 1);
				bonus.setState(BonusState.getStateByString(optionNode.getAttribute(STATE)));
				bonus.setPermanent(getBoolean(optionNode.getAttribute(PERMANENT), true));
				bonus.setBase(getBoolean(optionNode.getAttribute(BASE)));
				section.getBonuses().add(bonus);
			}
		}
	}

	private void createOptions(StorySection section, Node node) {
		NodeList optionsList = node.getChildNodes();
		for (int i = 0; i < optionsList.getLength(); i++) {
			Node n = optionsList.item(i);
			if (n instanceof Element) {
				Element optionNode = (Element) n;
				StorySectionOption option = new StorySectionOption();
				option.setSection(getInteger(optionNode.getAttribute(SECTION)));
				option.setDisableWhenSelected(getBoolean(optionNode.getAttribute(DISABLE_WHEN_SELECTED)));
				option.setText(optionNode.getAttribute(TEXT));
				option.setSkill(getInteger(optionNode.getAttribute(SKILL)));
				option.setLuckAspect(getBoolean(optionNode.getAttribute(LUCK_ASPECT)));
				option.setActiveSkill(optionNode.getAttribute(ACTIVE_SKILL));
				option.setStory(section.getStory());
				section.getOptions().add(option);
			}
		}
	}

	// Returns the entire XML document
	public Document getDocument(InputStream inputStream) {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = factory.newDocumentBuilder();
			InputSource inputSource = new InputSource(inputStream);
			document = db.parse(inputSource);
		} catch (ParserConfigurationException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (SAXException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		}
		return document;
	}

	private boolean getBoolean(String s) {
		return getBoolean(s, false);
	}

	private boolean getBoolean(String s, boolean defaultValue) {
		return s != null && !"".equals(s) ? Boolean.valueOf(s) : defaultValue;
	}

	private int getInteger(String s) {
		return s != null && !"".equals(s) ? Integer.valueOf(s.trim()) : 0;
	}

	private float getFloat(String s) {
		return getFloat(s, 0f);
	}

	private float getFloat(String s, float defaultvalue) {
		return s != null && !"".equals(s) ? Float.valueOf(s.trim()) : defaultvalue;
	}

}