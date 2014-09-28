package com.nex.gamebook.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nex.gamebook.CharacterSkillsActivity;
import com.nex.gamebook.R;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.playground.PlaygroundActivity;
public class FragmentTab extends GameBookFragment {
	Button button;
	public FragmentTab(Context context) {
		super(context);
	}

	public View create(ViewGroup container) {
		View view = ((Activity) getContext()).getLayoutInflater().inflate(
				R.layout.fragment_character_description, container, false);
		try {
			final Player _character = getCharacter();
			button = (Button) view.findViewById(R.id.play_button);
			showStats(view, _character, true);
			TextView textview = (TextView) view.findViewById(R.id.sel_char_description);
			textview.setText(_character.getDescription());
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), PlaygroundActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("character", _character.getId());
					bundle.putString("story", _character.getStory().getFullpath());
					bundle.putBoolean("load", true);
					intent.putExtras(bundle);
					getContext().startActivity(intent);
				}
			});
			button = (Button) view.findViewById(R.id.skill_list);
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getContext(), CharacterSkillsActivity.class);
					Bundle bundle = new Bundle();
					
					bundle.putInt("character", _character.getId());
					bundle.putString("story", _character.getStory().getFullpath());
					intent.putExtras(bundle);
					getContext().startActivity(intent, bundle);
				}
			});
		} catch (Exception e) {
			Log.e("GameBook CharacterSelection", "", e);
		}
		view.setTag(this);
		return view;
	}

}