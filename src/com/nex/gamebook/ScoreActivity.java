package com.nex.gamebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.io.GameBookUtils;
import com.nex.gamebook.fragment.GameBookFragment;

public class ScoreActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_score);
		String fileName = getIntent().getExtras().getString("fileName");
		try {
			Player _character = GameBookUtils.getInstance().loadCharacter(fileName);
			ScoreFragment fragment = new ScoreFragment(this);
			fragment.setCharacter(_character);
			LinearLayout container = (LinearLayout) findViewById(R.id.container);
			container.addView(fragment.create(container));
		} catch (Exception e) {
			Log.e("GamebookScore", "failed load score", e);
		}
	}

	class ScoreFragment extends GameBookFragment {

		public ScoreFragment(Context context) {
			super(context);
		}

		@Override
		public View create(ViewGroup container) {
			View view = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.score_fragment,
					container, false);
			Player player = getCharacter();
			TextView title = (TextView) view.findViewById(R.id.title);
			title.setText(player.getStory().getName() + "\n" + player.getName());
			showStats(view, player.getCurrentStats(), player.getStats(), true);
			fillDefaultStats(view);
			TextView text = (TextView) view.findViewById(R.id.score);
			text.setText(String.valueOf(player.getScore()));
			
			text = (TextView) view.findViewById(R.id.sections);
			text.setText(String.valueOf(player.getSections()));
			
			text = (TextView) view.findViewById(R.id.visitedSections);
			text.setText(String.valueOf(player.getVisitedSections()));
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
