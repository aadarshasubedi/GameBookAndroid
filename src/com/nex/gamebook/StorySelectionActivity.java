package com.nex.gamebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nex.gamebook.db.StoryDatasource;
import com.nex.gamebook.entity.Story;

public class StorySelectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_story_selection);
		LinearLayout layout = (LinearLayout)findViewById(R.id.stories);
		StoryDatasource sd = new StoryDatasource(this);
		sd.open();
		for(Story story: sd.findAll()) {
			Button button = new Button(this);
			button.setTag(story);
			button.setOnClickListener(buttonClicked);
			button.setText(story.getName());
			layout.addView(button);
		}
		sd.close();
	}
	
	OnClickListener buttonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Story story = (Story) v.getTag();
			Intent intent = new Intent(StorySelectionActivity.this, CharacterSelectionActivity.class);
			Bundle b = new Bundle();
			b.putLong("storyId", story.getId());
			intent.putExtras(b); 
			startActivity(intent);
		}
	};
}
