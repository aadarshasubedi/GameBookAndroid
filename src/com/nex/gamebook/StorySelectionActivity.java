package com.nex.gamebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nex.gamebook.ads.AdFactory;
import com.nex.gamebook.ads.AdFactory.OnAdClosed;
import com.nex.gamebook.game.Story;
import com.nex.gamebook.story.parser.StoryXmlParser;

public class StorySelectionActivity extends BannerAdActivity {
	// private InterstitialAd mInterstitial;
	// private
	private OnImageListener activeListener;

	@Override
	protected void onPreCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loading_layout);
		new LoadViewTask().execute();

		// AdFactory.loadDefaultInterstitialAd(this, new OnAdClosed() {
		// @Override
		// public void closed() {
		//
		// }
		// });

		// AdFactory.loadInterstitialAd(this, R.string.ad_interistial_story);
	}

	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
		private List<Story> stories = new ArrayList<>();
		@Override
		protected Void doInBackground(Void... params) {
			StoryXmlParser parser = new StoryXmlParser(StorySelectionActivity.this);
			try {
				for (Story story : parser.loadStories()) {
					stories.add(story);
				}
			} catch (IOException e) {
				Log.e("GameBookStorySelection", "", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			setContentView(R.layout.activity_story_selection);
			LinearLayout layout = (LinearLayout) findViewById(R.id.stories);
			StoryListAdapter ad = new StoryListAdapter(StorySelectionActivity.this);
			for (Story story : stories) {
				layout.addView(ad.getView(story, layout));
			}
		}

	}

	class StoryListAdapter {
		Context context;

		public StoryListAdapter(Context context) {

			this.context = context;
		}

		@SuppressLint("NewApi")
		public View getView(Story story, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.list_story_layout, parent, false);
			LinearLayout imagerounding = (LinearLayout) rowView.findViewById(R.id.imagerounding);
			LinearLayout storyInfo = (LinearLayout) rowView.findViewById(R.id.story_info);

			// imagerounding.bringToFront();
			ImageView image = (ImageView) rowView.findViewById(R.id.story_image);
			if (story.getBackground() > 0)
				image.setBackground(context.getResources().getDrawable(story.getBackground()));
			Button button = (Button) storyInfo.findViewById(R.id.play_button);
			button.setTag(story);
			button.setOnClickListener(buttonClicked);
			OnImageListener list = new OnImageListener(context, storyInfo);
			image.setOnClickListener(list);
			list.hide();
			TextView name = (TextView) rowView.findViewById(R.id.storyName);
			TextView description = (TextView) rowView.findViewById(R.id.storyDescription);
			name.setText(story.getName());
			description.setText(story.getDescription());
			return rowView;
		}
	}

	class OnImageListener implements OnClickListener {
		private LinearLayout layout;
		private Context context;

		public OnImageListener(Context context, LinearLayout layout) {
			super();
			this.layout = layout;
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			if (activeListener != null && !activeListener.equals(this)) {
				activeListener.hide();
			}
			activeListener = this;
			toggle();
		}

		public void toggle() {
			int visibility = layout.getVisibility();
			if (visibility == View.GONE) {
				show();
			} else {
				hide();
			}
		}

		public void show() {
			layout.setVisibility(View.VISIBLE);
			// animate(R.anim.trans_show);
		}

		public void hide() {
			layout.setVisibility(View.GONE);

		}

		private void animate(int anim) {
			Animation animation = AnimationUtils.loadAnimation(context, anim);
			animation.setDuration(500);
			layout.setAnimation(animation);
			layout.animate();
			animation.start();
		}
	}

	OnClickListener buttonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Story story = (Story) v.getTag();
			Intent intent = new Intent(StorySelectionActivity.this, CharacterSelectionActivity.class);
			Bundle b = new Bundle();
			// intent
			b.putString("story", story.getFullpath());
			intent.putExtras(b);
			startActivity(intent);
		}
	};
}
