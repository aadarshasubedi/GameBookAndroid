package com.nex.gamebook.playground;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nex.gamebook.R;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Stats;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.entity.io.IOGameOperation;
import com.nex.gamebook.story.parser.StoryXmlParser;
import com.nex.gamebook.story.section.StorySection;

public class PlaygroundActivity extends Activity {
	public static Player _character;
	private ActionBar.Tab characterTab;
	private ActionBar.Tab storyTab;
	private PlaygroundBattleLogCharacterTab characterFragment;
	private PlaygroundStoryTab storyFragment;
	private static List<View> battleLog = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		

		try {
			if(getIntent().getExtras().getBoolean("load")) {
				battleLog.clear();
				_character = load();
			}
			setTitle(_character.getStory().getName());
			setContentView(R.layout.activity_character_selection);

			characterFragment = new PlaygroundBattleLogCharacterTab();
			storyFragment = new PlaygroundStoryTab();
			storyFragment.putCharacter(_character);
			characterFragment.putCharacter(_character);
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setDisplayShowHomeEnabled(false);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			characterTab = actionBar.newTab().setText(R.string.character);
			characterTab.setTabListener(new MyTabListener(characterFragment));
			actionBar.addTab(characterTab);
			storyTab = actionBar.newTab().setText(R.string.story);
			storyTab.setTabListener(new MyTabListener(storyFragment));
			actionBar.addTab(storyTab);
			storyTab.select();
		} catch (Exception e) {
			Log.e("GameBook", "", e);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Bundle b = new Bundle();
		b.putBoolean("load", false);
		getIntent().putExtras(b);
	}
	
	public Player getCharacter() {
		return _character;
	}
	
	private Player load() throws Exception {
		
		String loadedGame = getIntent().getExtras().getString("load_game");
		if(loadedGame!=null && !"".equals(loadedGame)) {
			return IOGameOperation.loadCharacter(this, loadedGame);
		}
		StoryXmlParser parser = new StoryXmlParser(this);
		Story story = parser.loadStory(getIntent().getExtras().getString("story"), true);
		Player character = story.getCharacter(getIntent().getExtras().getInt("character"));
		character.setCurrentStats(new Stats(character.getStats()));
		return character;
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

	public void removeTab(Tab tab) {
		getActionBar().removeTab(tab);
	}

	public void addTab(Tab tab) {
		getActionBar().addTab(tab);
	}

	public ActionBar.Tab getCharacterTab() {
		return characterTab;
	}

	public ActionBar.Tab getStoryTab() {
		return storyTab;
	}

	public PlaygroundBattleLogCharacterTab getCharacterFragment() {
		return characterFragment;
	}

	public PlaygroundStoryTab getStoryFragment() {
		return storyFragment;
	}
	
	public void changeToBattle(StorySection section) {
		getCharacterFragment().fight(section);
		getCharacterTab().setText(R.string.fight);
		getCharacterTab().select();
		removeTab(getStoryTab());
		setFighting(true);
	}
	
	public void changeToStory() {
		setFighting(false);
		getActionBar().addTab(getStoryTab());
		getCharacterTab().setText(R.string.character);
		getStoryTab().select();
		getStoryFragment().setTabclick(false);
		setSelectedEnemy(0);
	}
	
	public void setFighting(boolean val) {
		Bundle b = new Bundle();
		b.putBoolean("fighting", val);
		getIntent().putExtras(b);
	}
	
	public void setSelectedEnemy(int i) {
		Bundle b = new Bundle();
		b.putInt("selectedEnemy", i);
		getIntent().putExtras(b);
	}
	public int getSelectedEnemy() {
		return getIntent().getExtras().getInt("selectedEnemy");
	}
	public boolean isFighting() {
		return getIntent().getExtras().getBoolean("fighting");
	}
	public List<View> getBattleLog() {
		return battleLog;
	}
}