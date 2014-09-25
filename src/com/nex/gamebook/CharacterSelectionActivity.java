package com.nex.gamebook;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nex.gamebook.ads.AdFactory;
import com.nex.gamebook.ads.AdFactory.OnAdClosed;
import com.nex.gamebook.fragment.FragmentTab;
import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.Story;
import com.nex.gamebook.story.parser.StoryXmlParser;

public class CharacterSelectionActivity extends BannerAdActivity {
	ViewFlipListener listener;

	@Override
	protected void onPreCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loading_layout);
		AdFactory.loadDefaultInterstitialAd(this, new OnAdClosed() {
			@Override
			public void closed() {
				setContentView(R.layout.activity_character_selection);
				try {
					StoryXmlParser parser = new StoryXmlParser(CharacterSelectionActivity.this, null);
					Story story = parser.loadStory(getIntent().getExtras().getString("story"), false, true);
					setTitle(story.getName());

					overridePendingTransition(R.anim.trans_left_in, R.anim.trans_right_out);

					final ViewFlipper flipper = (ViewFlipper) findViewById(R.id.viewSwitcher1);
					final TextView title = (TextView) findViewById(R.id.textView1);
					ImageView left = (ImageView) findViewById(R.id.imageView1);
					ImageView right = (ImageView) findViewById(R.id.imageView2);
					listener = new ViewFlipListener(left, right, flipper, title) {
						@Override
						public void viewChanged(View currentView) {
							showTitle(currentView, title);
						}

						@Override
						public Context getContext() {
							return CharacterSelectionActivity.this;
						}
					};

					for (Player c : story.getCharacters()) {
						FragmentTab fragmentTab = new FragmentTab(CharacterSelectionActivity.this);
						fragmentTab.setCharacter(c);
						flipper.addView(fragmentTab.create(flipper));
					}
					listener.select(0);
					showTitle(flipper.getCurrentView(), title);
				} catch (Exception e) {
					Log.e("Gamebook", "", e);
				}

			}
		});
	}

	private void showTitle(View currentView, TextView title) {
		FragmentTab selectedTab = (FragmentTab) currentView.getTag();
		title.setText(selectedTab.getCharacter().getName());
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (listener != null)
			return listener.onTouchEvent(event);
		return false;
	}
}
