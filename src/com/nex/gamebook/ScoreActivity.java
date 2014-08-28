package com.nex.gamebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Score;
import com.nex.gamebook.entity.io.GameBookUtils;
import com.nex.gamebook.fragment.GameBookFragment;
import com.nex.gamebook.playground.AbstractFragment;

public class ScoreActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_score);
		String fileName = getIntent().getExtras().getString("fileName");
		try {
			Score score = GameBookUtils.getInstance().loadScore(fileName);
			ScoreFragment fragment = new ScoreFragment(this, score);
			LinearLayout container = (LinearLayout) findViewById(R.id.container);
			container.addView(fragment.create(container));
		} catch (Exception e) {
			Log.e("GamebookScore", "failed load score", e);
		}
	}

	class ScoreFragment extends AbstractFragment {
		private Score score;
		public ScoreFragment(Context context, Score score) {
			super(context);
			this.score = score;
		}

		@Override
		public View create(final ViewGroup container) {
			
			
			final View view = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.score_fragment, container, false);
			TextView level = (TextView) view.findViewById(R.id.level);
			level.setText(String.valueOf(score.getLevel()));
			final TextView actualAttrs = (TextView) view.findViewById(R.id.actualStats);
			final TextView baseStats = (TextView) view.findViewById(R.id.base_stats);
			actualAttrs.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					showStats(view, score.getCurrentStats(), score.getStats(), true);
					decoreClickableTextView(ScoreActivity.this, baseStats, R.string.base_stats);
					actualAttrs.setText(R.string.actual_stats);
				}
			});
			baseStats.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					showStats(view, score.getStats(), score.getStats(), false);
					decoreClickableTextView(ScoreActivity.this, actualAttrs, R.string.actual_stats);
					baseStats.setText(R.string.base_stats);
				}
			});
			actualAttrs.callOnClick();
			TextView title = (TextView) view.findViewById(R.id.title);
			title.setText(this.score.getStoryname() + "\n" + this.score.getCharName());
			TextView text = (TextView) view.findViewById(R.id.score);
			text.setText(String.valueOf(this.score.getScore()));
			
			text = (TextView) view.findViewById(R.id.sections);
			text.setText(String.valueOf(this.score.getSections()));
			
			text = (TextView) view.findViewById(R.id.visitedSections);
			text.setText(String.valueOf(this.score.getVisitedSections()));
			Button button = (Button) view.findViewById(R.id.to_main_page);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ScoreActivity.this, MainScreenActivity.class);
					startActivity(intent);
					ScoreActivity.this.finish();
				}
			});
			return view;
		}

	}

}
