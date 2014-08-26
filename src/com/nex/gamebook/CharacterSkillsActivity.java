package com.nex.gamebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nex.gamebook.attack.special.SpecialSkill;
import com.nex.gamebook.entity.Character;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.SpecialSkillsMap;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.story.parser.StoryXmlParser;
import com.nex.gamebook.util.SkillInfoDialog;

public class CharacterSkillsActivity extends Activity {
	private List<String> keys;
	private Player _character;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_character_skills);
		StoryXmlParser parser = new StoryXmlParser(this);
		try {
			Story story = parser.loadStory(getIntent().getExtras().getString("story"), true);
			_character = story.getCharacter(getIntent().getExtras().getInt("character"));
			ListView list = (ListView) findViewById(R.id.skills);
			keys = new ArrayList<>(_character.getSpecialSkills().keySet());
			Collections.sort(keys, new Comparator<String>() {
				@Override
				public int compare(String lhs, String rhs) {
					Integer ls = _character.getSpecialSkills().get(lhs);
					Integer rs = _character.getSpecialSkills().get(rhs);
					return ls.compareTo(rs);
				}
			});
			list.setAdapter(new SkillsAdapter(this));
		} catch (Exception e) {
			Log.e("", "", e);
		}
		TextView title = (TextView) findViewById(R.id.textView1);
		title.setText(_character.getName() + " - " + getString(R.string.skills));
	}
	class SkillsAdapter extends ArrayAdapter<String> {
		Context context;
		public SkillsAdapter(Context context) {
			super(context, R.layout.list_item, new String[keys.size()]);
			this.context = context;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final String skillName = keys.get(position);
			int level = _character.getSpecialSkills().get(skillName);
			final SpecialSkill skill = SpecialSkillsMap.get(skillName);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.skill_item, parent, false);
			rowView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					SkillInfoDialog infoDialog = new SkillInfoDialog(CharacterSkillsActivity.this, _character, skill);
					infoDialog.show();
				}
			});
			TextView text = (TextView) rowView.findViewById(R.id.skill_level);
			text.setText(String.valueOf(level));
			text = (TextView) rowView.findViewById(R.id.skill_name);
			text.setText(skill.getNameId());
			return rowView;
		}
	}
}
