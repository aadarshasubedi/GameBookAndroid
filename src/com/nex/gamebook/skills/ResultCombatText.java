package com.nex.gamebook.skills;

public class ResultCombatText {
	private int color;
	private String text;

	public ResultCombatText(int color, String text) {
		super();
		this.color = color;
		this.text = text;
	}

	public int getColor() {
		return color;
	}

	public String getText() {
		return text;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setText(String text) {
		this.text = text;
	}

}
