package com.nex.gamebook.util;

import java.util.Properties;

public class StoryProperties extends Properties {
	private static final long serialVersionUID = -7316624442882799126L;

	@Override
	public String getProperty(String name) {
		return super.getProperty(name, name);
	}

}
