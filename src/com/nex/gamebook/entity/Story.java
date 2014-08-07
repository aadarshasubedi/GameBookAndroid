package com.nex.gamebook.entity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nex.gamebook.story.section.StorySection;

public class Story implements Serializable {
	private static final long serialVersionUID = -4844842607652119895L;
	private String xml;
	private String path;
	private String fullpath;
	private String name;
	private String description;
	private int background;
	private long id;
	private Map<Integer, StorySection> sections = new HashMap<>();
	private List<Player> characters = new ArrayList<Player>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public StorySection getSection(int position) {
		StorySection section = this.sections.get(position);
		section.reset();
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
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
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
		this.xml = xml.substring(t+1);
		this.path = xml.substring(0, t);
	}
	public String getFullpath() {
		return fullpath;
	}
}
