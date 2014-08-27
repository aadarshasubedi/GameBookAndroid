package com.nex.gamebook.playground;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nex.gamebook.R;
import com.nex.gamebook.ViewFlipListener;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.SpecialSkillsMap;
import com.nex.gamebook.entity.Stats;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.entity.StorySection;
import com.nex.gamebook.entity.io.GameBookUtils;
import com.nex.gamebook.story.parser.StoryXmlParser;
import com.nex.gamebook.util.DialogBuilder;

public class PlaygroundActivity extends Activity {
	private Player _character;
	private ViewFlipper flipper;
	private ImageView left;
	private ImageView right;
	private TextView title;
	private PlaygroundBattleLogCharacterView characterFragment;
	private PlaygroundStoryView storyFragment;
	private ViewFlipListener listener;
	private static List<View> battleLog = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_playground);
		try {
			_character = load();
			battleLog.clear();
			flipper = (ViewFlipper) findViewById(R.id.viewSwitcher1);
			left = (ImageView) findViewById(R.id.imageView1);
			right = (ImageView) findViewById(R.id.imageView2);
			title = (TextView) findViewById(R.id.textView1);
			characterFragment = new PlaygroundBattleLogCharacterView(this);
			characterFragment.setCharacter(_character);
			flipper.addView(characterFragment.create(flipper));
			storyFragment = new PlaygroundStoryView(this);
			storyFragment.setCharacter(_character);
			changeToStory();
		} catch (Exception e) {
			Log.e("GameBook", "", e);
		}

	}
	
	private void createListener() {
		listener = new ViewFlipListener(left, right, flipper, title) {
			
			@Override
			public void viewChanged(View currentView) {
				if(currentView.getTag().getClass().equals(PlaygroundStoryView.class)) {
					title.setText(_character.getStory().getName());
				} else {
					title.setText(_character.getName());
				}
			}
			
			@Override
			public Context getContext() {
				return PlaygroundActivity.this;
			}
		};
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		_character = null;
		flipper = null;
		left = null;
		right = null;
		title = null;
		characterFragment = null;
		storyFragment = null;
		listener = null;
	}
	
	public Player getCharacter() {
		return _character;
	}
	
	private Player load() throws Exception {
		
		String loadedGame = getIntent().getExtras().getString("load_game");
		if(loadedGame!=null && !"".equals(loadedGame)) {
			return GameBookUtils.getInstance().loadCharacter(loadedGame);
		}
		StoryXmlParser parser = new StoryXmlParser(this);
		Story story = parser.loadStory(getIntent().getExtras().getString("story"), true);
		Player character = story.getCharacter(getIntent().getExtras().getInt("character"));
		return character;
	}

	public PlaygroundBattleLogCharacterView getCharacterFragment() {
		return characterFragment;
	}

	public PlaygroundStoryView getStoryFragment() {
		return storyFragment;
	}
	
	public void changeToBattle(StorySection section) {
		setFighting(true);
		getCharacterFragment().fight(section);
		flipper.removeAllViews();
		flipper.addView(characterFragment.create(flipper));
		listener = characterFragment.createListener(left, right, title);
		characterFragment.getSwitcher().setVisibility(View.VISIBLE);
	}
	public void changeToStory() {
		setFighting(false);
		title.setText(_character.getStory().getName());
		flipper.removeAllViews();
		flipper.addView(storyFragment.create(flipper));
		flipper.addView(characterFragment.create(flipper));
		createListener();
		characterFragment.getSwitcher().setVisibility(View.GONE);
	}
	boolean fighting;
	public void setFighting(boolean val) {
		fighting = val;
	}
	public boolean isFighting() {
		return fighting;
	}
	public List<View> getBattleLog() {
		return battleLog;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(this.listener==null) return false;
		return this.listener.onTouchEvent(event);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    //Handle the back button
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
	        //Ask the user if they want to quit
	    	final DialogBuilder dialog = new DialogBuilder(this)
	    	.setTitle(R.string.close_book)
	    	.setText(R.string.close_book_description)
	        .setNegativeButton(R.string.no, null);
	    	dialog.setPositiveButton(R.string.yes, new OnClickListener() {
				@Override
				public void onClick(View v) {
					//Intent intent = new Intent(PlaygroundActivity.this, MainScreenActivity.class);
					//PlaygroundActivity.this.startActivity(intent);
					dialog.dismiss();
					PlaygroundActivity.this.finish();
				}
			}).show();


	        return true;
	    }
	    else {
	        return super.onKeyDown(keyCode, event);
	    }

	}
	
	
	
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev){
//	    super.dispatchTouchEvent(ev);
//	    if(this.listener==null) return false;
//	    return listener.onTouchEvent(ev);
//	}
}