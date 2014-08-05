package com.nex.gamebook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nex.gamebook.R;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.playground.PlaygroundActivity;

public class FragmentTab extends GameBookFragment {

	public FragmentTab() {
		// TODO Auto-generated constructor stub
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_character_description,
				container, false);

		try {

			final Player _character = getCharacter(view.getContext());
			TextView attr = (TextView) view.findViewById(R.id.sel_attr_health);
			attr.setText(String.valueOf(_character.getStats().getHealth()));

			attr = (TextView) view.findViewById(R.id.sel_attr_attack);
			attr.setText(String.valueOf(_character.getStats().getAttack()));
			attr = (TextView) view.findViewById(R.id.sel_attr_defense);
			attr.setText(String.valueOf(_character.getStats().getDefense()));

			attr = (TextView) view.findViewById(R.id.sel_attr_skill);
			attr.setText(String.valueOf(_character.getStats().getSkill())
					+ " (" + _character.getStats().getSkillPercentage() + "%)");

			attr = (TextView) view.findViewById(R.id.sel_attr_luck);
			attr.setText(String.valueOf(_character.getStats().getLuck()) + " ("
					+ _character.getStats().getLuckPercentage() + "%)");

			TextView textview = (TextView) view
					.findViewById(R.id.sel_char_description);
			textview.setText(_character.getDescription());
			Button button = (Button) view.findViewById(R.id.play_button);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(),
							PlaygroundActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("character", _character.getId());
					bundle.putString("story", _character.getStory().getXml());
					bundle.putBoolean("load", true);
					
					intent.putExtras(bundle);
					startActivity(intent);

				}
			});
		} catch (Exception e) {
			Log.e("GameBook CharacterSelection", "", e);
		}

		return view;
	}
}