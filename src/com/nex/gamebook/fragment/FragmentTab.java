package com.nex.gamebook.fragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nex.gamebook.R;
import com.nex.gamebook.ScoreActivity;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.SpecialSkillsMap;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.entity.io.GameBookUtils;
import com.nex.gamebook.playground.PlaygroundActivity;
import com.nex.gamebook.util.DialogBuilder;
import com.nex.gamebook.util.SkillInfoDialog;

public class FragmentTab extends GameBookFragment {
	Spinner skills;
	public FragmentTab(Context context) {
		super(context);
	}

	public View create(ViewGroup container) {
		View view = ((Activity) getContext()).getLayoutInflater().inflate(
				R.layout.fragment_character_description, container, false);
		addSkillsToPinner(view);
		try {
			final Player _character = getCharacter();
			fillDefaultStats(view);

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
					bundle.putString("story", _character.getStory()
							.getFullpath());
					bundle.putString("skill", (String) skills.getSelectedItem());
					bundle.putBoolean("load", true);
					intent.putExtras(bundle);
					getContext().startActivity(intent);

				}
			});
		} catch (Exception e) {
			Log.e("GameBook CharacterSelection", "", e);
		}
		view.setTag(this);
		return view;
	}

	// add items into spinner dynamically
	public void addSkillsToPinner(View view) {
		skills = (Spinner) view.findViewById(R.id.skills);		
		List<String> keys = new ArrayList<String>(SpecialSkillsMap
				.getPlayerAttacks().keySet());
		skills.setAdapter(new SkillsAdapter(getContext(), keys));
		skills.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}

	class SkillsAdapter extends ArrayAdapter<String> {
		List<String> keys;
		Context context;

		public SkillsAdapter(Context context, List<String> keys) {
			super(context, R.layout.list_item, keys);
			this.context = context;
			this.keys = keys;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			return getCustomViewView(position, convertView, parent, R.layout.spinner_dropdown_item);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomViewView(position, convertView, parent, R.layout.spinner_single_item);
		}
		
		
		public View getCustomViewView(int position, View convertView, ViewGroup parent, int inflate) {
			String key = keys.get(position);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(inflate, parent,
					false);
			TextView name = (TextView) rowView.findViewById(R.id.name);
			SpecialSkill skill = SpecialSkillsMap.getPlayersAttack(key);
			rowView.setTag(key);
			name.setText(context.getString(skill.getNameId()));
			return rowView;
		}

	}

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {

		private boolean showDialog = false;
		
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			if(showDialog) {
				String i = (String) parent.getItemAtPosition(pos);
				SpecialSkill skill = SpecialSkillsMap.getPlayersAttack(i);
				SkillInfoDialog dialog = new SkillInfoDialog(parent.getContext(), skill);
				dialog.show();
			}
			showDialog = true;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}

	}
}