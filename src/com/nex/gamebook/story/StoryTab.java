package com.nex.gamebook.story;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nex.gamebook.R;
import com.nex.gamebook.entity.character.Character;

public class StoryTab extends Fragment {
	private Character _character;

	public StoryTab(Character _character) {
		super();
		this._character = _character;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_playground_story,
				container, false);
		
		return view;
	}
}
