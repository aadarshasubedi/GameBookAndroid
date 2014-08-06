package com.nex.gamebook.playground;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nex.gamebook.R;
import com.nex.gamebook.ViewFlipListener;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Stats;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.entity.io.IOGameOperation;
import com.nex.gamebook.story.parser.StoryXmlParser;
import com.nex.gamebook.story.section.StorySection;

public class PlaygroundActivity extends Activity {
	public static Player _character;
	private ViewFlipper flipper;
	private ImageView left;
	private ImageView right;
	private TextView title;
	private PlaygroundBattleLogCharacterTab characterFragment;
	private PlaygroundStoryTab storyFragment;
	private static List<View> battleLog = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_playground);
		try {
			if(getIntent().getExtras().getBoolean("load")) {
				battleLog.clear();
				_character = load();
			}
			flipper = (ViewFlipper) findViewById(R.id.viewSwitcher1);
			left = (ImageView) findViewById(R.id.imageView1);
			right = (ImageView) findViewById(R.id.imageView2);
			title = (TextView) findViewById(R.id.textView1);
			characterFragment = new PlaygroundBattleLogCharacterTab(this);
			characterFragment.setCharacter(_character);
			flipper.addView(characterFragment.create(flipper));
			storyFragment = new PlaygroundStoryTab(this);
			storyFragment.setCharacter(_character);
			flipper.addView(storyFragment.create(flipper));
			changeToStory();
		} catch (Exception e) {
			Log.e("GameBook", "", e);
		}

	}

	private ViewFlipListener createListener() {
		ViewFlipListener listener = new ViewFlipListener(left, right, flipper) {
			
			@Override
			public void viewChanged(View currentView) {
				if(currentView.getTag().getClass().equals(PlaygroundStoryTab.class)) {
					title.setText(R.string.story);
				} else {
					title.setText(R.string.character);
				}
			}
			
			@Override
			public Context getContext() {
				return PlaygroundActivity.this;
			}
		};
		return listener;
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

	public PlaygroundBattleLogCharacterTab getCharacterFragment() {
		return characterFragment;
	}

	public PlaygroundStoryTab getStoryFragment() {
		return storyFragment;
	}
	
	public void changeToBattle(StorySection section) {
		getCharacterFragment().fight(section);
		flipper.removeAllViews();
		flipper.addView(characterFragment.create(flipper));
		setFighting(true);
		characterFragment.createListeners(left, right, title);
		characterFragment.getSwitcher().setVisibility(View.VISIBLE);
	}
	
	public void changeToStory() {
		setFighting(false);
		title.setText(R.string.story);
		flipper.removeAllViews();
		flipper.addView(characterFragment.create(flipper));
		flipper.addView(storyFragment.create(flipper));
		createListener().select(1);
		setSelectedEnemy(0);
		characterFragment.getSwitcher().setVisibility(View.GONE);
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