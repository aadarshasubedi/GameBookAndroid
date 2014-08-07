package com.nex.gamebook.entity;

import java.io.Serializable;

public class StorySectionOption implements Serializable {

	private int section;
	private String text;
	private int skill;
	private boolean luckAspect;
	private boolean alreadyDisplayed;
	private boolean disableWhenSelected;
	private boolean disabled;
	private boolean displayed;

	public int getSection() {
		return section;
	}

	public void setSection(int section) {
		this.section = section;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public boolean isLuckAspect() {
		return luckAspect;
	}

	public void setLuckAspect(boolean luckAspect) {
		this.luckAspect = luckAspect;
	}

	public boolean isAlreadyDisplayed() {
		return alreadyDisplayed;
	}

	public void setAlreadyDisplayed(boolean alreadyDisplayed) {
		this.alreadyDisplayed = alreadyDisplayed;
	}

	public boolean isBothAspects() {
		return isLuckAspect() && getSkill() > 0;
	}

	public boolean isAlwaysDisplayed() {
		return !this.luckAspect && skill == 0;
	}

	public boolean isDisableWhenSelected() {
		return disableWhenSelected;
	}

	public void setDisableWhenSelected(boolean disableWhenSelected) {
		this.disableWhenSelected = disableWhenSelected;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}

}