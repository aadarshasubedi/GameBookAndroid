package com.nex.gamebook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.fragment.FragmentTab;
import com.nex.gamebook.story.parser.StoryXmlParser;

public class CharacterSelectionActivity extends Activity {
	ViewFlipListener listener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		try {
			StoryXmlParser parser = new StoryXmlParser(this);
			Story story = parser.loadStory(getIntent().getExtras().getString("story"), true);
			setTitle(story.getName());
			setContentView(R.layout.activity_character_selection);
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
				FragmentTab fragmentTab = new FragmentTab(this);
				fragmentTab.setCharacter(c);
				flipper.addView(fragmentTab.create(flipper));
			}
			listener.select(0);
			showTitle(flipper.getCurrentView(), title);
		} catch (Exception e) {
			Log.e("Gamebook", "", e);
		}
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
		return listener.onTouchEvent(event);
	}
}
