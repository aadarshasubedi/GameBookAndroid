package com.nex.gamebook;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nex.gamebook.entity.Story;
import com.nex.gamebook.story.parser.StoryXmlParser;

public class StorySelectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_story_selection);
		LinearLayout layout = (LinearLayout) findViewById(R.id.stories);
		StoryXmlParser parser = new StoryXmlParser(this);
		try {
			for(Story story: parser.loadStories()) {
				Button button = new Button(this);
				button.setTag(story);
				button.setOnClickListener(buttonClicked);
				button.setText(story.getName());
				layout.addView(button);
			}
		} catch (IOException e) {
			Log.e("GameBook", "", e);
		}
	}
	
	OnClickListener buttonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Story story = (Story) v.getTag();
			Intent intent = new Intent(StorySelectionActivity.this, CharacterSelectionActivity.class);
			Bundle b = new Bundle();
//			intent
			b.putString("story", story.getXml());
			intent.putExtras(b); 
			startActivity(intent);
		}
	};
}
