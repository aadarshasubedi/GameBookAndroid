package com.nex.gamebook;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.fragment.FragmentTab;
import com.nex.gamebook.story.parser.StoryXmlParser;

public class CharacterSelectionActivity extends Activity {
	
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
				FragmentTab fragmentTab = new FragmentTab();
				fragmentTab.putCharacter(c);
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
		

	public class MyTabListener implements ActionBar.TabListener {
		FragmentTab fragment;

		
		
		public MyTabListener(FragmentTab fragment) {
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
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		selectedCharacter = null;
//	}
}
