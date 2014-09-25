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
import com.nex.gamebook.util.StoryIOUtils;

public class StoryXmlParserAsJaxB {
	Context context;

	public StoryXmlParserAsJaxB(Context context) {
		super();
		this.context = context;
	}
	public Story loadStory(String xml, boolean sections, boolean characters) throws Exception {
		return StoryIOUtils.loadFullContentAsStory(GameBookUtils.getInstance().getStoriesPath(xml));
	}
	public Story loadStory(String xml, boolean fully) throws Exception {
		return loadStory(xml, fully, fully);
	}
	
	public List<Story> loadStories() throws IOException {
		List<Story> stories = new ArrayList<Story>();
		InputStream stream = null;
		try {
			stream = new FileInputStream(GameBookUtils.getInstance().getStoriesFolder("stories.xml"));
			Document document = getDocument(stream);
			NodeList nList = document.getElementsByTagName("story");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node node = nList.item(temp);
				if (node instanceof Element) {
					Element el = (Element) node;
					String storyXml = el.getAttribute("name");
					//com.nex.gamebook.xsd.Story s = StoryIOUtils.loadStoryType(GameBookUtils.getInstance().getStoriesPath(storyXml));
					Story story = new Story();
					story.saveXmlPath(storyXml);
					GameBookUtils.getInstance().loadProperties(story);
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
}