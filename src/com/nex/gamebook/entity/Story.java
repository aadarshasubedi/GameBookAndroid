package com.nex.gamebook.entity;

public abstract class Story implements Entity {

	private long id;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public abstract int getName();
}
