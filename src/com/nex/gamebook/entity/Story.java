package com.nex.gamebook.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.nex.gamebook.entity.character.Character;
import com.nex.gamebook.story.section.StorySection;

public class Story implements Entity {
	private String xml;
	private int name;
	private long id;
	private Map<Integer, StorySection> sections = new HashMap<>();
	private List<Character> characters = new ArrayList<Character>();

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
	public List<Character> getCharacters() {
		return characters;
	}
	
	public Character getCharacter(int id) {
		for(Character ch: characters) {
			if(ch.getId() == id) return ch;
		}
		return null;
	}
	
}
