package com.nex.gamebook.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.story.parser.StoryXmlParser;

public abstract class GameBookFragment {
	private Context context;
	private Player character;

	public GameBookFragment(Context context) {
		this.context = context;
	}

	public abstract View create(ViewGroup container);

	// public Player getCharacter(Context context) throws Exception {
	// StoryXmlParser parser = new StoryXmlParser(context);
	// Story story = parser.loadStory(getArguments().getString("story"), true,
	// true);
	// Player _character = story.getCharacter(getArguments().getInt(
	// "character"));
	// return _character;
	// }
	//
	// public void putCharacter(Player character) {
	// Bundle bundle = new Bundle();
	// bundle.putString("story", character.getStory().getXml());
	// bundle.putInt("character", character.getId());
	// setArguments(bundle);
	// }

	public Bundle getArguments() {
		Activity activity = (Activity) getContext();
		return activity.getIntent().getExtras();
	}

	public void setArguments(Bundle bundle) {
		Activity activity = (Activity) getContext();
		activity.getIntent().putExtras(bundle);
	}

	public Context getContext() {
		return context;
	}

	public Player getCharacter() {
		return character;
	}

	public void setCharacter(Player character) {
		this.character = character;
	}

}
