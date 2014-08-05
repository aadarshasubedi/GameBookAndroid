package com.nex.gamebook.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.story.parser.StoryXmlParser;

public class GameBookFragment extends Fragment {

	public Player getCharacter(Context context) throws Exception {
		StoryXmlParser parser = new StoryXmlParser(context);
		Story story = parser.loadStory(getArguments().getString("story"), true,
				true);
		Player _character = story.getCharacter(getArguments().getInt(
				"character"));
		return _character;
	}

	public void putCharacter(Player character) {
		Bundle bundle = new Bundle();
		bundle.putString("story", character.getStory().getXml());
		bundle.putInt("character", character.getId());
		setArguments(bundle);
	}

}
