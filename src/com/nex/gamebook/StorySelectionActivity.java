package com.nex.gamebook;

import java.io.IOException;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.nex.gamebook.entity.Story;
import com.nex.gamebook.story.parser.StoryXmlParser;

public class StorySelectionActivity extends Activity {
	
//	private 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_story_selection);
		StoryXmlParser parser = new StoryXmlParser(this);
		LinearLayout layout = (LinearLayout) findViewById(R.id.stories);
		StoryListAdapter ad = new StoryListAdapter(this);
		try {
			for(Story story: parser.loadStories()) {
				 layout.addView(ad.getView(story, layout));
			}
		} catch (IOException e) {
			Log.e("GameBookStorySelection",  "", e);
		}
	}
	class StoryListAdapter {
		Context context;

		public StoryListAdapter(Context context) {
			
			this.context = context;
		}


		@SuppressLint("NewApi")
		public View getView(Story story, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.list_story_layout, parent,
					false);
			LinearLayout imagerounding = (LinearLayout) rowView.findViewById(R.id.imagerounding);
			LinearLayout storyInfo = (LinearLayout) rowView.findViewById(R.id.story_info);
			
//			imagerounding.bringToFront();
			ImageView image = (ImageView) rowView.findViewById(R.id.story_image);
			image.setBackground(context.getResources().getDrawable(story.getBackground()));
			Button button = (Button) storyInfo.findViewById(R.id.play_button);
			button.setTag(story);
			button.setOnClickListener(buttonClicked);
			image.setOnClickListener(new OnImageListener(context, storyInfo));
//			storyInfo.setVisibility(View.GONE);
			return rowView;
		}
	}

	class OnImageListener implements OnClickListener {
		private LinearLayout layout;
		private int height;
		private Context context;
		public OnImageListener(Context context, LinearLayout layout) {
			super();
			this.layout = layout;
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			
			 Animation animation   =    AnimationUtils.loadAnimation(context, R.anim.trans_top_down);
			    animation.setDuration(500);
			    layout.setAnimation(animation);
			    layout.animate();
			    animation.start();
//			int visibility = layout.getVisibility();
//			if(visibility == View.GONE) {
//				layout.setVisibility(View.VISIBLE);
//			} else {
//				layout.setVisibility(View.GONE);
//			}
		}

	}
	
	OnClickListener buttonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Story story = (Story) v.getTag();
			Intent intent = new Intent(StorySelectionActivity.this,
					CharacterSelectionActivity.class);
			Bundle b = new Bundle();
			// intent
			b.putString("story", story.getFullpath());
			intent.putExtras(b);
			startActivity(intent);
		}
	};
}
