package com.nex.gamebook.story;

import java.util.ArrayList;
import java.util.List;

import com.nex.gamebook.entity.Story;

public abstract class StorySection {

	private int description;
	private int position;
	private List<StorySectionOption> options = new ArrayList<StorySectionOption>();
	public StorySection() {
		super();
	}

	public int getDescription() {
		return description;
	}
	
	public void setDescription(int description) {
		this.description = description;
	}
	
	public void move(Story story, int nextSection) {

	}

	public int getPosition() {
		return position;
	}

	public List<StorySectionOption> getOptions() {
		return options;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}

}
