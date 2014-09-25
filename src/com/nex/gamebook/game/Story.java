package com.nex.gamebook.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.nex.gamebook.skills.active.SkillProperties;
import com.nex.gamebook.util.GameBookUtils;
import com.nex.gamebook.util.SaveGameSectionState;

public class Story implements Serializable, Mergable {
	private static final long serialVersionUID = -4844842607652119895L;
	private String xml;
	private String path;
	private String fullpath;
	private int background;
	private long id;
	private int version;
	private Map<Integer, StorySection> sections = new HashMap<>();
	private List<Player> characters = new ArrayList<Player>();
	private Map<String, Enemy> enemies = new HashMap<>();
	private Map<String, SkillProperties> skills = new HashMap<>();
	private transient Properties properties;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public StorySection getSection(Player pl, int position) {
		StorySection section = this.sections.get(position);
		SaveGameSectionState state = null;
		if(pl!=null && pl.getSaveState()!=null && !section.isInitialized()) {
			state = pl.getSaveState().getSectionsState().get(position);
		}
		section.setInitialized(true);
		section.reset(state);
		return section;
	}

	public Boolean canStart() {
		return sections.size() > 0;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public Map<Integer, StorySection> getSections() {
		return sections;
	}

	public String getName() {
		return GameBookUtils.getInstance().getText("name", this);
	}

	public String getDescription() {
		return GameBookUtils.getInstance().getText("description", this);
	}

	public List<Player> getCharacters() {
		return characters;
	}

	public Player getCharacter(int id) {
		for (Player ch : characters) {
			if (ch.getId() == id)
				return ch;
		}
		return null;
	}

	public int getBackground() {
		return background;
	}

	public void setBackground(int background) {
		this.background = background;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void saveXmlPath(String xml) {
		this.fullpath = xml;
		int t = xml.lastIndexOf("/");
		this.xml = xml.substring(t + 1);
		this.path = xml.substring(0, t);
	}

	public String getFullpath() {
		return fullpath;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Enemy findEnemy(String id) {
		return this.enemies.get(id);
	}

	public Map<String, Enemy> getEnemies() {
		return enemies;
	}

	public void assignEnemiesToSections() {
		if (enemies.isEmpty())
			return;
		for (Map.Entry<Integer, StorySection> entry : sections.entrySet()) {
			entry.getValue().assignEnemies();
		}
		enemies.clear();
	}

	public void assignSkillsToCharacters() {
		if (skills.isEmpty())
			return;
		for (Character c : this.characters) {
			c.createSkills(this.skills);
		}
//		skills.clear();
	}

	public Map<String, SkillProperties> getSkills() {
		return skills;
	}
}
