package com.nex.gamebook.story;

import com.nex.gamebook.entity.Story;

public abstract class StorySection {
	
	private int description;
	
	public StorySection(int description) {
		super();
		this.description = description;
	}

	public int getDescription() {
		return description;
	}

	public void move(Story story, int nextSection) {
		
	}
	
}
