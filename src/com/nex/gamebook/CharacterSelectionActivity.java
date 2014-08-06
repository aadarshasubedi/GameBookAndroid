package com.nex.gamebook;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ViewSwitcher;

import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.fragment.FragmentTab;
import com.nex.gamebook.story.parser.StoryXmlParser;

public class CharacterSelectionActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		try {
			StoryXmlParser parser = new StoryXmlParser(this);
			Story story = parser.loadStory(getIntent().getExtras().getString("story"), true);
			setTitle(story.getName());
			setContentView(R.layout.activity_character_selection);
			overridePendingTransition(R.anim.trans_left_in, R.anim.trans_right_out);
			
			ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.viewSwitcher1);
			
			for (Player c : story.getCharacters()) {
				
				FragmentTab fragmentTab = new FragmentTab(this);
				fragmentTab.putCharacter(c);
				switcher.addView(fragmentTab.create(switcher));
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
}
