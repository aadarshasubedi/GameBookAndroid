package com.nex.gamebook.game;

import java.io.Serializable;

public class SerializationMetadata implements Serializable {
	private static final long serialVersionUID = -1051986476253620055L;
	private long time;
	private String metaFile;
	private String file;
	private String story;
	private int character;
	private int version;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public int getCharacter() {
		return character;
	}

	public void setCharacter(int character) {
		this.character = character;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getMetaFile() {
		return metaFile;
	}

	public void setMetaFile(String metaFile) {
		this.metaFile = metaFile;
	}

}
