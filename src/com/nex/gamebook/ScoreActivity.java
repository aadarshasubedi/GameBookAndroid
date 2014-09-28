package com.nex.gamebook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.SerializationMetadata;
import com.nex.gamebook.game.SkillMap;
import com.nex.gamebook.playground.AbstractFragment;
import com.nex.gamebook.playground.SkillsSpinner;
import com.nex.gamebook.util.GameBookUtils;
import com.nex.gamebook.util.LoadingCallback;
import com.nex.gamebook.util.PassiveSkillInfoDialogAnSelection;
import com.nex.gamebook.util.PassiveSkillInfoDialogAnSelection.DismissCallBack;
import com.nex.gamebook.util.Statistics.StatisticItem;
import com.thoughtworks.xstream.XStream;

public class ScoreActivity extends BannerAdActivity {
	boolean fromPlayground = false;
	@Override
	protected void onPreCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loading_layout);
		fromPlayground = getIntent().getExtras().getBoolean("playground", false);
		new LoadViewTask().execute();
	}

	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
		ScoreFragment fragment;

		@Override
		protected Void doInBackground(Void... params) {
			try {
				fragment = new ScoreFragment(ScoreActivity.this, load(null));
			} catch (Exception e) {
				Log.e("GamebookScore", "failed load score", e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			setContentView(R.layout.activity_score);
			LinearLayout container = (LinearLayout) findViewById(R.id.container);
			container.addView(fragment.create(container));
			loadAd();
		}
	}

	private Player load(LoadingCallback cb) throws Exception {
		XStream stream = GameBookUtils.getInstance().createXStream();
		File metaFile = GameBookUtils.getInstance().getScoreFolder("meta", getIntent().getExtras().getString("fileName"), false);
		SerializationMetadata loadedGame = GameBookUtils.getInstance().loadSingleMetadata(stream, metaFile);
		if (loadedGame != null) {
			return GameBookUtils.getInstance().loadScore(loadedGame, cb);
		}
		return null;
	}

	class ScoreFragment extends AbstractFragment {
		private Player player;

		public ScoreFragment(Context context, Player player) {
			super(context);
			this.player = player;
		}

		
		
		@Override
		public View create(final ViewGroup container) {

			final View view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.score_fragment, container, false);
			TextView level = (TextView) view.findViewById(R.id.level);
			level.setText(String.valueOf(player.getLevel()));
			showStats(view, player, false);
			TextView title = (TextView) view.findViewById(R.id.title);
			title.setText(player.getStory().getName() + "\n" + player.getName());
			TextView text = (TextView) view.findViewById(R.id.score);
			text.setText(String.valueOf(player.getSaveState().getScore()));
			ListView log = (ListView) view.findViewById(R.id.statistics_list);
			final List<StatisticItem> items = player.getStatistics().asList();
			
			SkillsSpinner skills = (SkillsSpinner) view.findViewById(R.id.passive_skills);
			skills.setAdapter(new PassiveSkillsAdapter(getContext(), new ArrayList<>(player.getLearnedPassiveSkills()), skills));
			log.setAdapter(new ArrayAdapter<StatisticItem>(view.getContext(), R.layout.statistics_item, items) {
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View rowView = inflater.inflate(R.layout.statistics_item, parent, false);
					StatisticItem item = items.get(position);
					TextView text = (TextView) rowView.findViewById(R.id.stat_title);
					text.setText(item.getId());
					text = (TextView) rowView.findViewById(R.id.stat_summary);
					text.setText(String.valueOf(item.getValue()));
					return rowView;
				}
			});
			Button button = (Button) view.findViewById(R.id.to_main_page);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					if(fromPlayground) {
						setContentView(R.layout.loading_layout);
						loadAd();
						new AsyncTask<Void, Integer, Void>() {
							@Override
							protected Void doInBackground(Void... params) {
								Intent intent = new Intent(ScoreActivity.this, MainScreenActivity.class);
								startActivity(intent);
								ScoreActivity.this.finish();
								return null;
							}
						}.execute();
						
					} else {
						ScoreActivity.this.finish();
					}
				}
			});
			return view;
		}

		class PassiveSkillsAdapter extends ArrayAdapter<String> {
			private List<String> skills;
			SkillsSpinner owner;
			Context context;

			public PassiveSkillsAdapter(Context context, List<String> keys, SkillsSpinner owner) {
				super(context, R.layout.list_item, keys);
				this.skills = keys;
				this.context = context;
				this.owner = owner;
			}

			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {
				final String skill = skills.get(position);
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View rowView = inflater.inflate(R.layout.spinner_dropdown_item, parent, false);
				TextView name = (TextView) rowView.findViewById(R.id.name);
				name.setText(SkillMap.getPassive(skill).getName(player.getStory().getProperties()));
				rowView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// owner.onDetachedFromWindow();
						PassiveSkillInfoDialogAnSelection dialog = new PassiveSkillInfoDialogAnSelection(context, player, skill);
						dialog.show(new DismissCallBack() {
							@Override
							public void dismiss() {
								owner.onDetachedFromWindow();
							}
						});
					}
				});
				return rowView;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View rowView = inflater.inflate(R.layout.spinner_single_item_skill, parent, false);
				TextView name = (TextView) rowView.findViewById(R.id.name);
				name.setTextColor(getContext().getResources().getColor(R.color.passive_skill_triggered));
				name.setText(context.getString(R.string.passive_skils));
				decoreClickableTextView(getContext(), name, String.valueOf(name.getText()));
				return rowView;
			}
		}
		
	}

}
