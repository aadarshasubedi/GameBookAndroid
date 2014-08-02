package com.nex.gamebook.story.parser;

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

import com.nex.gamebook.entity.Story;
import com.nex.gamebook.entity.character.Character;
import com.nex.gamebook.story.Bonus.BonusState;
import com.nex.gamebook.story.Bonus.BonusType;
import com.nex.gamebook.story.Bonus;
import com.nex.gamebook.story.Enemy;
import com.nex.gamebook.story.StorySectionOption;
import com.nex.gamebook.story.section.StorySection;

public class StoryXmlParser {
	
	private final String CHARACTER = "character";
	private final String SECTION = "section";
	private final String TEXT = "text";
	private final String STORY = "story";
	
	private final String OPTIONS = "options";
	private final String ENEMIES = "enemies";
	private final String BONUSES = "bonuses";
	private final String FIGHT_LUCK_TEXT_SECTION = "luckText";
	private final String GAMEOVER_SECTION = "gameOverText";
	
	private final String POSITION = "position";
	
	private final String ATTACK = "attack";
	private final String HEALTH = "health";
	private final String DEFENSE = "defense";
	private final String SKILL = "skill";
	private final String LUCK = "luck";
	
	private final String NAME = "name";
	private final String DESCRIPTION = "description";
	private final String ID = "id";
	
	private final String TYPE = "type";
	private final String VALUE = "value";
	private final String OVERFLOWED = "overflowed";
	
	
	private final String FIGHT_WITHOUTDAMAGE = "fightWithoutDamageText";
	private final String FIGHT_WITHDAMAGE = "fightWithDamageText";
	private final String FIGHT_SKILL = "fightSkillText";
	private final String FIGHT_LOSE = "fightLoseText";
	private final String FIGHT_LUCK = "fightLuckText";
	
	Context context;

	public StoryXmlParser(Context context) {
		super();
		this.context = context;
	}

	public List<Story> loadStories() throws IOException {
		List<Story> stories = new ArrayList<Story>();
		InputStream stream = null;
		try {
			stream = context.getAssets().open("stories.xml");
			Document document = getDocument(stream);
			NodeList nList = document.getElementsByTagName(STORY);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node node = nList.item(temp);
				if(node instanceof Element) {
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
	
	public Story loadStory(String xml, boolean fully) throws IOException {
		InputStream stream = null;
		try {
			stream = context.getAssets().open(xml);
			Document document = getDocument(stream);
			NodeList nList = document.getElementsByTagName(STORY);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node node = nList.item(temp);
				if(node instanceof Element) {
					Element el = (Element) node;
					Story story = new Story();
					story.setXml(xml);
					story.setName(getIdentifier(el.getAttribute(NAME)));
					if(fully) {
						initializeStory(story);
					}
					return story;
				}
			}			
		} catch (Exception e) {
			Log.e("GameBookStoryParser", e.getMessage(), e);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		return null;
	}
	
	public void initializeStory(Story story) throws IOException {

		InputStream stream = null;
		try {
			stream = context.getAssets().open(story.getXml());
			Document document = getDocument(stream);
			NodeList nList = document.getElementsByTagName(SECTION);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node node = nList.item(temp);
				if(node instanceof Element) {
					Element el = (Element) node;
					loadSection(story, el);
				}
			}
			nList = document.getElementsByTagName(CHARACTER);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node node = nList.item(temp);
				if(node instanceof Element) {
					Element el = (Element) node;
					loadCharacter(story, el);
				}
			}			
		} catch (Exception e) {
			Log.e("GameBookStoryParser", e.getMessage(), e);
		} finally {
			if (stream != null) {
				stream.close();
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

	private void loadCharacter(Story story, Element element) throws Exception {
		Character character = new Character();
		character.setName(getIdentifier(element.getAttribute(NAME)));
		character.setDescription(getIdentifier(element.getAttribute(DESCRIPTION)));
		character.setId(getInteger(element.getAttribute(ID)));
		character.setPosition(getInteger(element.getAttribute(POSITION)));
		
		NodeList optionsList = element.getChildNodes();
		for (int i = 0; i < optionsList.getLength(); i++) {
			Node node = element.getChildNodes().item(i);
			if (node.getNodeName().equals(HEALTH)) {
				character.getStats().setHealth(getInteger(node.getTextContent()));
			} else if(node.getNodeName().equals(DEFENSE)) {
				character.getStats().setDefense(getInteger(node.getTextContent()));
			} else if(node.getNodeName().equals(SKILL)) {
				character.getStats().setSkill(getInteger(node.getTextContent()));
			} else if(node.getNodeName().equals(LUCK)) {
				character.getStats().setLuck(getInteger(node.getTextContent()));
			}
		}
		character.setStory(story);
		story.getCharacters().add(character);
	}
	
	private void loadSection(Story story, Element element) throws Exception {
		int position = getInteger(element.getAttribute(POSITION));
		if(position == 0) {
			throw new IllegalStateException("position attribute must existk on <section />");
		}
		
		StorySection section = new StorySection();		
		section.setEndGame(getBoolean(element.getAttribute("endgame")));
		int text = getIdentifier(element.getAttribute(TEXT));
		section.setText(text);
		
		int enemiesDefeatedText = getIdentifier(element.getAttribute("enemiesDefeatedText"));
		int luckText = getIdentifier(element.getAttribute(FIGHT_LUCK_TEXT_SECTION));
		if(luckText > 0) section.setLuckText(luckText);
		
		int gameOverText = getIdentifier(element.getAttribute(GAMEOVER_SECTION));
		if(gameOverText > 0) section.setGameOverText(gameOverText);
		
		NodeList optionsList = element.getChildNodes();
		for (int i = 0; i < optionsList.getLength(); i++) {
			Node node = element.getChildNodes().item(i);
			if (node.getNodeName().equals(OPTIONS)) {
				createOptions(section, node);
			} else if(node.getNodeName().equals(ENEMIES)) {
				createEnemies(section, node);
			} else if(node.getNodeName().equals(BONUSES)) {
				createBonuses(section, node);
			}
		}
		if(!section.getEnemies().isEmpty() && enemiesDefeatedText == 0) {
			throw new IllegalStateException("enemiesDefeatedText attribute must exist if <section position="+position+"/> has enemies");
		}
		section.setEnemiesDefeatedText(enemiesDefeatedText);
		story.getSections().put(position, section);
	}
	
	private int getIdentifier(String name) {
		String[] splited = name.split("@|/");
		if(splited.length > 1)
		return context.getResources().getIdentifier(splited[2], splited[1], context.getPackageName());
		return 0;
	}
	
	private void createEnemies(StorySection section, Node node) {
		NodeList optionsList = node.getChildNodes();
		for (int i = 0; i < optionsList.getLength(); i++) {
			Node n = optionsList.item(i);
			if (n instanceof Element) {
				Element optionNode = (Element) n;
				Enemy enemy = new Enemy();
				enemy.setAttack(getInteger(optionNode.getAttribute(ATTACK)));
				enemy.setSkill(getInteger(optionNode.getAttribute(SKILL)));
				enemy.setName(getIdentifier(optionNode.getAttribute(NAME)));
				int text = getIdentifier(optionNode.getAttribute(FIGHT_WITHOUTDAMAGE));
				if(text > 0) enemy.setWithoutDamageText(text);
				
				text = getIdentifier(optionNode.getAttribute(FIGHT_WITHDAMAGE));
				if(text > 0) enemy.setWithDamageText(text);
				
				text = getIdentifier(optionNode.getAttribute(FIGHT_SKILL));
				if(text > 0) enemy.setSkillText(text);
				
				text = getIdentifier(optionNode.getAttribute(FIGHT_LOSE));
				if(text > 0) enemy.setLoseText(text);
				
				text = getIdentifier(optionNode.getAttribute(FIGHT_LUCK));
				if(text > 0) enemy.setLuckText(text);
				
				section.getEnemies().add(enemy);
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
				bonus.setType(BonusType.valueOf(optionNode.getAttribute(TYPE).toUpperCase()));
				bonus.setValue(getInteger(optionNode.getAttribute(VALUE)));
				bonus.setOverflowed(getBoolean(optionNode.getAttribute(OVERFLOWED)));
				Boolean coeff = getBoolean(optionNode.getAttribute("debuff"));
				bonus.setCoeff(coeff?-1:1);
				bonus.setState(BonusState.getStateByString(optionNode.getAttribute("state")));
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
				option.setText(getIdentifier(optionNode.getAttribute(TEXT)));
				option.setSkill(getInteger(optionNode.getAttribute(SKILL)));
				section.getOptions().add(option);
			}
		}
	}
	private boolean getBoolean(String s ) {
		return s != null && !"".equals(s) ? Boolean.valueOf(s):false;
	}
	private int getInteger(String s ) {
		return s != null && !"".equals(s) ? Integer.valueOf(s):0;
	}
	
}
