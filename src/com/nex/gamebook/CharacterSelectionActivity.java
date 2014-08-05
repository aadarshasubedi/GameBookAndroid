package com.nex.gamebook;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.playground.PlaygroundActivity;
import com.nex.gamebook.story.parser.StoryXmlParser;

public class CharacterSelectionActivity extends Activity {
	public Player selectedCharacter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		try {
			StoryXmlParser parser = new StoryXmlParser(this);
			Story story = parser.loadStory(getIntent().getExtras().getString("story"), true);
			setTitle(story.getName());
			setContentView(R.layout.activity_character_selection);
			overridePendingTransition(R.anim.trans_left_in, R.anim.trans_right_out);
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayShowHomeEnabled(false);
			
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			for (Player c : story.getCharacters()) {
				ActionBar.Tab tab = actionBar.newTab().setText(c.getName());
				FragmentTab fragmentTab = new FragmentTab(c);
				tab.setTabListener(new MyTabListener(fragmentTab));
				actionBar.addTab(tab);
			}
		} catch (Exception e) {
			Log.e("Gamebook", "", e);
		}
		
	}
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}
		public void play(View view) {
			Intent intent = new Intent(view.getContext(), PlaygroundActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("character", selectedCharacter.getId());
			bundle.putString("story", selectedCharacter.getStory().getXml());
			intent.putExtras(bundle);
			startActivity(intent);
		}

	public class FragmentTab extends Fragment {

		private Player _character;

		public FragmentTab(Player ch) {
			this._character = ch;
		}

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_character_description,
					container, false);
			TextView attr = (TextView) view.findViewById(R.id.sel_attr_health);
			attr.setText(String.valueOf(_character.getStats().getHealth()));

			attr = (TextView) view.findViewById(R.id.sel_attr_attack);
			attr.setText(String.valueOf(_character.getStats().getAttack()));
			attr = (TextView) view.findViewById(R.id.sel_attr_defense);
			attr.setText(String.valueOf(_character.getStats().getDefense()));

			attr = (TextView) view.findViewById(R.id.sel_attr_skill);
			attr.setText(String.valueOf(_character.getStats().getSkill()) + " ("+_character.getStats().getSkillPercentage() + "%)");
			
			attr = (TextView) view.findViewById(R.id.sel_attr_luck);
			attr.setText(String.valueOf(_character.getStats().getLuck()) + " ("+_character.getStats().getLuckPercentage() + "%)");

			
			TextView textview = (TextView) view
					.findViewById(R.id.sel_char_description);
			textview.setText(_character.getDescription());
			return view;
		}
	}

	public class MyTabListener implements ActionBar.TabListener {
		FragmentTab fragment;

		public MyTabListener(FragmentTab fragment) {
			this.fragment = fragment;
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_container, fragment);
			CharacterSelectionActivity.this.selectedCharacter = fragment._character;
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment);
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {

		}
	}
}
