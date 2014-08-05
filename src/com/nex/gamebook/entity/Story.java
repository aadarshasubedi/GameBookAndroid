package com.nex.gamebook.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nex.gamebook.story.section.StorySection;

public class Story implements Serializable {
	private String xml;
	private int name;
	private int background;
	private int description;
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

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
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

	public int getDescription() {
		return description;
	}

	public void setDescription(int description) {
		this.description = description;
	}

}
