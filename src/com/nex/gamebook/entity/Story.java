package com.nex.gamebook.entity;

import java.util.HashMap;
import java.util.Map;

import com.nex.gamebook.story.StorySection;

public abstract class Story implements Entity {

	private long id;
	private int position = 1;
	protected Map<Integer, StorySection> sections = new HashMap<>();
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public StorySection getSection() {
		return this.sections.get(this.position);
	}

	public abstract int getName();

}
