package com.nex.gamebook.story.parser;

import java.io.File;
import java.io.FileInputStream;
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

import com.nex.gamebook.entity.Bonus;
import com.nex.gamebook.entity.Bonus.BonusState;
import com.nex.gamebook.entity.Bonus.StatType;
import com.nex.gamebook.entity.Enemy;
import com.nex.gamebook.entity.Enemy.EnemyLevel;
import com.nex.gamebook.entity.EnemyAssign;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Stats;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.entity.StorySection;
import com.nex.gamebook.entity.StorySectionOption;
import com.nex.gamebook.entity.io.GameBookUtils;

public class StoryXmlParser {

	private final String CHARACTER = "character";
	private final String SECTION = "section";
	private final String TEXT = "text";
	private final String ALREADY_VISITED_TEXT = "alreadyVisitedText";
	private final String INCLUDE = "include";
	private final String VERSION = "version";
	private final String SPECIAL_ATTACK = "specialAttack";
	private final String SKILLS = "skills";
	private final String BACKGROUND = "background";
	private final String LEVEL_REQUIRED = "levelRequired";
	private final String LOSE_SECTION = "loseSection";
	private final String LEVEL = "level";
	private final String WIN_SECTION = "winSection";
	private final String SCORE_MULTIPLIER = "scoreMultiplier";
	private final String PRIMARY_STAT = "primaryStat";
	private final String RESET_ATTRIBUTES = "resetAttributes";
	private final String RESET_POSITIVE_ATTRIBUTES = "resetPositiveAttributes";
	private final String RESET_NEGATIVE_ATTRIBUTES = "resetNegativeAttributes";
	private final String STORY = "story";

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

	private final String NAME = "name";
	private final String DESCRIPTION = "description";
	private final String ID = "id";

	private final String TYPE = "type";
	private final String VALUE = "value";
	private final String OVERRIDE_SKILL = "overrideSkill";
	private final String OVERFLOWED = "overflowed";
	private final String PERMANENT = "permanent";
	
	private final String DEBUFF = "debuff";
	private final String STATE = "state";
	
	
//	private final String REF = "ref";
	
	Context context;

	public StoryXmlParser(Context context) {
		super();
		this.context = context;
	}

	

	public List<Story> loadStories() throws IOException {
		List<Story> stories = new ArrayList<Story>();
		InputStream stream = null;
		try {
			stream = new FileInputStream(GameBookUtils.getInstance().getApplicationFolder("stories.xml"));
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

	public Story loadStory(String xml, boolean sections, boolean characters)
			throws Exception {
		Story story = new Story();
		story.saveXmlPath(xml);
		GameBookUtils.getInstance().loadPropties(story);
		loadStory(story, story.getXml(), sections, characters);
		story.assignEnemiesToSections();
		return story;
	}

	

	

	public void loadStory(Story story, String xml, boolean sections, boolean characters) throws Exception {

		InputStream stream = null;
		try {
			File storiesFolder = GameBookUtils.getInstance().getApplicationFolder(story.getPath()
					+ File.separator + xml);
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
						story.setBackground(getIdentifier(background.item(0)
								.getTextContent()));
					NodeList version = el.getElementsByTagName(VERSION);
					if (version.getLength() > 0)
						story.setVersion(getInteger(version.item(0).getTextContent()));
					initializeStory(story, document, sections, characters);
				}
			}
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

	public void initializeStory(Story story, Document document,
			boolean sections, boolean characters) throws Exception {
		if (!sections && !characters)
			return;
		if (sections) {
			loadSections(document, story);
			loadEnemies(document, story);
		}
		if (characters)
			loadCharacters(document, story);

	}

	private void loadSections(Document document, Story story) throws Exception {
		NodeList nList = document.getElementsByTagName(SECTION);
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			if (node instanceof Element) {
				Element el = (Element) node;
				loadSection(story, el);
			}
		}
	}

	private void loadCharacters(Document document, Story story)
			throws Exception {
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
		NodeList nList = document.getElementsByTagName(ENEMY);
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			if (node instanceof Element) {
				Element el = (Element) node;
				loadEnemy(story, el);
			}
		}
	}
	
	private void loadEnemy(Story story, Element element) {
		Enemy enemy = new Enemy();
		enemy.setName(element.getAttribute(NAME));
		String id = element.getAttribute(ID);
		enemy.setEnemyLevel(EnemyLevel.getLevelByString(element.getAttribute(TYPE)));
		NodeList optionsList = element.getChildNodes();
		for (int i = 0; i < optionsList.getLength(); i++) {
			Node node = element.getChildNodes().item(i);
			putStats(enemy, node);
		}
		enemy.setStory(story);
		story.getEnemies().put(id, enemy);
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
		character.getStats().setPlayer(character);
		character.getCurrentStats().setPlayer(character);
		character.setStory(story);
		story.getCharacters().add(character);
		
	}

	private void putStats(com.nex.gamebook.entity.Character character, Node node) {
		if (node.getNodeName().equals(HEALTH)) {
			character.getStats().setHealth(
					getInteger(node.getTextContent()));
		} else if (node.getNodeName().equals(DEFENSE)) {
			character.getStats().setDefense(
					getInteger(node.getTextContent()));
		} else if (node.getNodeName().equals(SKILL)) {
			character.getStats()
					.setSkill(getInteger(node.getTextContent()));
		} else if (node.getNodeName().equals(LUCK)) {
			character.getStats().setLuck(getInteger(node.getTextContent()));
		} else if (node.getNodeName().equals(ATTACK)) {
			character.getStats().setAttack(getInteger(node.getTextContent()));
		} else if (node.getNodeName().equals(BASE_DAMAGE)) {
			character.getStats().setDamage(getInteger(node.getTextContent()));
		} else if (node.getNodeName().equals(SPECIAL_ATTACK)) {
			//this is for enemies
			character.setSkillName(node.getTextContent());
		} else if (node.getNodeName().equals(SKILLS)) {
			//this is for player
			NodeList list = node.getChildNodes();
			for(int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);
				if(n.getNodeName().equals(SKILL)) {
					Element element = (Element) n;
					String skillName = element.getAttribute(NAME);
					int skillLevel = getInteger(element.getAttribute(LEVEL_REQUIRED));
					character.getSpecialSkills().put(skillName, skillLevel);
				}
			}
			
		} else if (node.getNodeName().equals(SKILL_POWER)) {
			character.getStats().setSkillpower(getInteger(node.getTextContent()));
		} else if(node.getNodeName().equals(PRIMARY_STAT)) {
			StatType stat = StatType.valueOf(node.getTextContent().toUpperCase());
			if(StatType.DAMAGE.equals(stat)) {
				Log.w("StoryXMLParser", "damage cannot be primary attribute"); 
			} else {
				character.setPrimaryStat(stat);
			}
		}
		character.setCurrentStats(new Stats(character.getStats()));
	}
	
	private void loadSection(Story story, Element element) throws Exception {
		int position = getInteger(element.getAttribute(POSITION));
		if (position == 0) {
			throw new IllegalStateException("position attribute must exist on <section />");
		}

		StorySection section = new StorySection();
		section.setStory(story);
		int level = getIdentifier(element.getAttribute(LEVEL));
		if(level==0) {
			level = 1;
		}
		section.setLevel(level);
		section.setLoseSection(getBoolean(element.getAttribute(LOSE_SECTION)));
		section.setWinSection(getBoolean(element.getAttribute(WIN_SECTION)));
		section.setResetAttributes(getBoolean(element.getAttribute(RESET_ATTRIBUTES)));
		section.setResetPositiveAttributes(getBoolean(element.getAttribute(RESET_POSITIVE_ATTRIBUTES)));
		section.setResetNegativeAttributes(getBoolean(element.getAttribute(RESET_NEGATIVE_ATTRIBUTES)));
		section.setScoreMultiplier(getFloat(element.getAttribute(SCORE_MULTIPLIER)));
		
		String text = element.getAttribute(TEXT);
		section.setText(text);
		section.setAlreadyVisitedText(text);
		String alreadyVisitedText = element.getAttribute(ALREADY_VISITED_TEXT);
		if (alreadyVisitedText != null && alreadyVisitedText.length()>0)
			section.setAlreadyVisitedText(alreadyVisitedText);

		String enemiesDefeatedText = element
				.getAttribute("enemiesDefeatedText");
		String luckText = element.getAttribute(FIGHT_LUCK_TEXT_SECTION);
		if (luckText !=null && luckText.trim().length() > 0) {
			section.setLuckPossible(true);
			section.setLuckText(luckText);
		}
		String gameOverText = element.getAttribute(GAMEOVER_SECTION);
		if (gameOverText!=null && gameOverText.trim().length()> 0)
			section.setGameOverText(gameOverText);
		section.setLuckDefeatEnemies(getBoolean(element
				.getAttribute(LUCK_DEFEAT_ENEMIES)));
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
			throw new IllegalStateException(
					"enemiesDefeatedText attribute must exist if <section position="
							+ position + "/> has enemies");
		}
		section.setEnemiesDefeatedText(enemiesDefeatedText);
		story.getSections().put(position, section);
	}

	private int getIdentifier(String name) {
		String[] splited = name.split("@|/");
		if (splited.length > 1)
			return context.getResources().getIdentifier(splited[2], splited[1],
					context.getPackageName());
		return 0;
	}

	private void createEnemiesReferences(StorySection section, Node node) {
		NodeList optionsList = node.getChildNodes();
		for (int i = 0; i < optionsList.getLength(); i++) {
			Node n = optionsList.item(i);
			if (n instanceof Element) {
				Element enemyNode = (Element) n;
				section.getEnemiesIds().add(new EnemyAssign(enemyNode.getAttribute(VALUE), enemyNode.getAttribute(OVERRIDE_SKILL)));
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
				bonus.setType(StatType.valueOf(optionNode.getAttribute(TYPE)
						.toUpperCase()));
				bonus.setValue(getInteger(optionNode.getAttribute(VALUE)));
				bonus.setOverflowed(getBoolean(optionNode
						.getAttribute(OVERFLOWED)));
				Boolean coeff = getBoolean(optionNode.getAttribute(DEBUFF));
				bonus.setCoeff(coeff ? -1 : 1);
				bonus.setState(BonusState.getStateByString(optionNode
						.getAttribute(STATE)));
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
				option.setDisableWhenSelected(getBoolean(optionNode
						.getAttribute(DISABLE_WHEN_SELECTED)));
				option.setText(optionNode.getAttribute(TEXT));
				option.setSkill(getInteger(optionNode.getAttribute(SKILL)));
				option.setLuckAspect(getBoolean(optionNode
						.getAttribute(LUCK_ASPECT)));
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
		return s != null && !"".equals(s) ? Float.valueOf(s.trim()) : 0;
	}
	

}