package com.nex.gamebook.fragment;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nex.gamebook.R;
import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.SpecialSkillsMap;
import com.nex.gamebook.playground.PlaygroundActivity;
import com.nex.gamebook.util.SkillInfoDialog;
public class FragmentTab extends GameBookFragment {
	Spinner skills;
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
			addSkillsToPinner(view, _character);
			fillDefaultStats(view);

			TextView textview = (TextView) view.findViewById(R.id.sel_char_description);
			textview.setText(_character.getDescription());
			
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), PlaygroundActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("character", _character.getId());
					bundle.putString("story", _character.getStory().getFullpath());
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
	public void addSkillsToPinner(View view, Character applicator) {
		skills = (Spinner) view.findViewById(R.id.skills);
		List<String> keys = new ArrayList<String>(SpecialSkillsMap.getSkills().keySet());
		keys.add(0, null);
		skills.setAdapter(new SkillsAdapter(getContext(), keys));
		skills.setOnItemSelectedListener(new CustomOnItemSelectedListener(applicator));
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
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			String key = keys.get(position);
			if(key == null) {
				return new LinearLayout(getContext());
			}
			return getCustomViewView(position, convertView, parent, R.layout.spinner_dropdown_item);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomViewView(position, convertView, parent,
					R.layout.spinner_single_item);
		}

		public View getCustomViewView(int position, View convertView, ViewGroup parent, int inflate) {
			String key = keys.get(position);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(inflate, parent, false);
			TextView name = (TextView) rowView.findViewById(R.id.name);
			if(key != null) {
				SpecialSkill skill = SpecialSkillsMap.get(key);
				rowView.setTag(key);
				name.setText(context.getString(skill.getNameId()));
			} else {
				name.setText(R.string.select_skill);
			}
			return rowView;
		}

	}

	public class CustomOnItemSelectedListener implements OnItemSelectedListener {

		private Character applicator;

		private CustomOnItemSelectedListener(Character applicator) {
			super();
			this.applicator = applicator;
		}


		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				String i = (String) parent.getItemAtPosition(pos);
				if(i == null) {
					button.setVisibility(View.GONE);
					return;
				} else {
					button.setVisibility(View.VISIBLE);	
				}
				SpecialSkill skill = SpecialSkillsMap.get(i);
				applicator.setSpecialSkill(skill);
				SkillInfoDialog dialog = new SkillInfoDialog(parent.getContext(), applicator);
				dialog.show();

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}

	}
}