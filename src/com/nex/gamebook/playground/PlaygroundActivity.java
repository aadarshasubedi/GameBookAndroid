package com.nex.gamebook.playground;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

import com.nex.gamebook.BannerAdActivity;
import com.nex.gamebook.R;
import com.nex.gamebook.ViewFlipListener;
import com.nex.gamebook.ads.AdFactory;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.SerializationMetadata;
import com.nex.gamebook.game.Story;
import com.nex.gamebook.game.StorySection;
import com.nex.gamebook.story.parser.StoryXmlParser;
import com.nex.gamebook.util.DialogBuilder;
import com.nex.gamebook.util.GameBookUtils;
import com.nex.gamebook.util.LoadingCallback;
import com.thoughtworks.xstream.XStream;

public class PlaygroundActivity extends BannerAdActivity {
	private Player _character;
	private ViewFlipper flipper;
	private ImageView left;
	private ImageView right;
	private TextView title;
	private PlaygroundBattleLogCharacterView characterFragment;
	private PlaygroundStoryView storyFragment;
	private ViewFlipListener listener;
	private static List<View> battleLog = new ArrayList<>();
	private int SHOW_AD_AFTER_CHANGE_FRAGMENTS = 20;
	private int fragmentsDisplayed = SHOW_AD_AFTER_CHANGE_FRAGMENTS;
	private long startTime;

	@Override
	protected void onPreCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loading_layout);
		new LoadViewTask().execute();

	}

	// To use the AsyncTask, it must be subclassed
	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {

		// The code to be executed in a background thread.
		@Override
		protected Void doInBackground(Void... params) {
			try {
				_character = PlaygroundActivity.this.load(null);
			} catch (Exception e) {
				Log.e("LoadingResources", "", e);
			}
			return null;
		}

		// after executing the code in the thread
		@Override
		protected void onPostExecute(Void result) {
			// close the progress dialog
			// progressDialog.dismiss();
			setContentView(R.layout.activity_playground);
			try {
				battleLog.clear();
				flipper = (ViewFlipper) findViewById(R.id.viewSwitcher1);
				left = (ImageView) findViewById(R.id.imageView1);
				left.setAlpha(0.5f);
				right = (ImageView) findViewById(R.id.imageView2);
				right.setAlpha(0.5f);
				title = (TextView) findViewById(R.id.textView1);
				characterFragment = new PlaygroundBattleLogCharacterView(PlaygroundActivity.this);
				characterFragment.setCharacter(_character);
				flipper.addView(characterFragment.create(flipper));
				storyFragment = new PlaygroundStoryView(PlaygroundActivity.this);
				storyFragment.setCharacter(_character);
				PlaygroundActivity.this.startTime = System.currentTimeMillis();
				loadAd();
				changeToStory(_character.getCurrentSection());
			} catch (Exception e) {
				Log.e("GameBook", "", e);
			}
		}

	}

	@Override
	protected void onStop() {
		_character.save();
		super.onStop();
	}

	private void createListener() {
		listener = new ViewFlipListener(left, right, flipper, title) {

			@Override
			public void viewChanged(View currentView) {
				if (currentView.getTag().getClass().equals(PlaygroundStoryView.class)) {
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

	private Player load(LoadingCallback cb) throws Exception {
		XStream stream = GameBookUtils.getInstance().createXStream();
		String metadata = getIntent().getExtras().getString("metadata");
		if (metadata != null && metadata.length() > 0) {
			SerializationMetadata loadedGame = GameBookUtils.getInstance().loadSingleMetadata(stream, new File(metadata));
			if (loadedGame != null) {
				return GameBookUtils.getInstance().loadCharacter(loadedGame, cb);
			}
		}
		StoryXmlParser parser = new StoryXmlParser(this, cb);
		Story story = parser.loadStory(getIntent().getExtras().getString("story"), true);
		Player character = story.getCharacter(getIntent().getExtras().getInt("character"));
		character.fullsave();
		return character;
	}

	public PlaygroundBattleLogCharacterView getCharacterFragment() {
		return characterFragment;
	}

	public PlaygroundStoryView getStoryFragment() {
		return storyFragment;
	}

	public void saveInBackground() {
		new AsyncTask<Void, Integer, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				_character.save();
				return null;
			}
		}.execute();
	}
	
	public void changeToBattle(StorySection section) {
		saveInBackground();
		// unloadAd();
		hideAd();
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		checkAndDisplayAd();
		section.setFighting(true);
		getCharacterFragment().setSection(section);
		flipper.removeAllViews();
		flipper.addView(characterFragment.create(flipper));
		listener = characterFragment.createListener(left, right, title);
		characterFragment.getSwitcher().setVisibility(View.VISIBLE);
		_character.getStatistics().addBattle();
	}

	public void changeToStory(StorySection section) {
		storeTime();
		showAd();
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		checkAndDisplayAd();
		section.setFighting(false);
		title.setText(_character.getStory().getName());
		flipper.removeAllViews();
		flipper.addView(storyFragment.create(flipper));
		flipper.addView(characterFragment.create(flipper));
		createListener();
		characterFragment.getSwitcher().setVisibility(View.GONE);
	}

	public List<View> getBattleLog() {
		return battleLog;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.listener == null)
			return false;
		return this.listener.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (PlaygroundActivity.this._character == null)
			return false;
		final boolean fighting = PlaygroundActivity.this._character.getCurrentSection().isFighting();
		// Handle the back button
		int text = R.string.close_book_description;
		if (fighting) {
			text = R.string.close_book_when_fighting_description;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Ask the user if they want to quit
			final DialogBuilder dialog = new DialogBuilder(this).setTitle(R.string.close_book).setText(text).setNegativeButton(R.string.no, null);
			dialog.setPositiveButton(R.string.yes, new OnClickListener() {
				@Override
				public void onClick(View v) {
					// Intent intent = new Intent(PlaygroundActivity.this,
					// MainScreenActivity.class);
					// PlaygroundActivity.this.startActivity(intent);
					dialog.dismiss();
					setContentView(R.layout.saving_layout);
					storeTime();
					new SaveGameTask().execute();
				}
			}).show();

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	/**
	 * Run this method before saving game or score for real time spent saving.
	 */
	public void storeTime() {
		_character.getStatistics().addTimeSpent(System.currentTimeMillis() - startTime);
		startTime = System.currentTimeMillis();
	}

	public class SaveGameTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			_character.save();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			PlaygroundActivity.this.finish();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		storeTime();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.startTime = System.currentTimeMillis();
	}

	void checkAndDisplayAd() {
		if (fragmentsDisplayed % SHOW_AD_AFTER_CHANGE_FRAGMENTS == 0) {
			fragmentsDisplayed = 0;
			// AdFactory.loadDefaultInterstitialAd(this);
		}
		fragmentsDisplayed++;
	}

	// @Override
	// public boolean dispatchTouchEvent(MotionEvent ev){
	// super.dispatchTouchEvent(ev);
	// if(this.listener==null) return false;
	// return listener.onTouchEvent(ev);
	// }
}