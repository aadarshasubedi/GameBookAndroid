package com.nex.gamebook;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nex.gamebook.db.CharactersDatasource;
import com.nex.gamebook.db.StoryDatasource;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.entity.character.Character;
import com.nex.gamebook.entity.character.Stats;
import com.nex.gamebook.story.StoryTab;

public class PlaygroundActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		CharactersDatasource ds = new CharactersDatasource(this);
		StoryDatasource sds = new StoryDatasource(this);
		sds.open();
		ds.open();
		
		Character character = ds.findById(getIntent().getExtras().getLong("characterId"));
		Story story = sds.findById(character.getStoryId());
		load(character, story);
		ds.close();
		sds.close();
		setTitle(story.getName());
		setContentView(R.layout.activity_character_selection);
		
		
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.Tab tab = actionBar.newTab().setText(R.string.character);
		Fragment fragmentTab = new CharacterTab(character);
		tab.setTabListener(new MyTabListener(fragmentTab));
		actionBar.addTab(tab);
		
		
		tab = actionBar.newTab().setText(R.string.story);
		fragmentTab = new StoryTab(character);
		tab.setTabListener(new MyTabListener(fragmentTab));
		actionBar.addTab(tab);
		tab.select();
		
	}
	private void load(Character character, Story story) {
		Long saveId = getIntent().getExtras().getLong("saveId");
		//load game
		if(saveId != null) {
			character.setCurrentStats(new Stats(character.getStats()));
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.playground, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class CharacterTab extends Fragment {

		private Character _character;

		public CharacterTab(Character ch) {
			this._character = ch;
		}

		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_character_description,
					container, false);
			TextView attr = (TextView) view.findViewById(R.id.sel_attr_health);
			attr.setText(String.valueOf(_character.getCurrentStats().getHealth() + "/" + _character.getStats().getHealth()));

			attr = (TextView) view.findViewById(R.id.sel_attr_attack);
			attr.setText(String.valueOf(_character.getCurrentStats().getAttack() + "/" + _character.getStats().getAttack()));

			attr = (TextView) view.findViewById(R.id.sel_attr_defense);
			attr.setText(String.valueOf(_character.getCurrentStats().getDefense() + "/" + _character.getStats().getDefense()));

			attr = (TextView) view.findViewById(R.id.sel_attr_skill);
			attr.setText(String.valueOf(_character.getCurrentStats().getSkill() + "/" + _character.getStats().getSkill()));

			TextView textview = (TextView) view.findViewById(R.id.sel_char_description);
			textview.setText(_character.getDescription());
			return view;
		}
	}

	public class MyTabListener implements ActionBar.TabListener {
		Fragment fragment;

		public MyTabListener(Fragment fragment) {
			this.fragment = fragment;
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_container, fragment);
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment);
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {

		}
	}
	
}
