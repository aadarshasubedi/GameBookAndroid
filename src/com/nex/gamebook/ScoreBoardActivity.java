package com.nex.gamebook;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.SerializationMetadata;
import com.nex.gamebook.game.Story;
import com.nex.gamebook.story.parser.StoryXmlParser;
import com.nex.gamebook.util.GameBookUtils;

public class ScoreBoardActivity extends BannerAdActivity {
	// private Map<String, Set<String>> savedGames;
	private Map<SerializationMetadata, Story> data = new HashMap<SerializationMetadata, Story>();
	private List<SerializationMetadata> keys;

	@Override
	protected void onPreCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loading_layout);
		new LoadViewTask().execute();
	}

	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			List<SerializationMetadata> metas = GameBookUtils.getInstance().getScores();
			for (SerializationMetadata m : metas) {
				String xml = m.getStory();
				try {
					StoryXmlParser parser = new StoryXmlParser(ScoreBoardActivity.this);
					ScoreBoardActivity.this.data.put(m, parser.loadStory(xml, false, true));
				} catch (Exception e) {
					Log.e("GameBookLoadActivity", "", e);
				}
			}
			ScoreBoardActivity.this.keys = new ArrayList<SerializationMetadata>(ScoreBoardActivity.this.data.keySet());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			setContentView(R.layout.activity_score_board);
			ListView list = (ListView) findViewById(R.id.scores);
			list.setAdapter(new ScoreItem(ScoreBoardActivity.this));
		}
	}

	class ScoreItem extends ArrayAdapter<String> {
		Context context;

		public ScoreItem(Context context) {
			super(context, R.layout.list_item, new String[keys.size()]);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final SerializationMetadata saveGame = keys.get(position);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.list_item, parent, false);
			TextView storyName = (TextView) rowView.findViewById(R.id.story_name);
			int charId = saveGame.getCharacter();
			long timeInString = saveGame.getTime();
			TextView characterName = (TextView) rowView.findViewById(R.id.character);
			TextView time = (TextView) rowView.findViewById(R.id.time);
			DateFormat df = DateFormat.getDateTimeInstance();
			String formatedTime = df.format(new Date(timeInString));
			time.setText(formatedTime);

			final Story story = ScoreBoardActivity.this.data.get(saveGame);
			final Player _character = story.getCharacter(charId);
			rowView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), ScoreActivity.class);
					Bundle b = new Bundle();
					b.putString("fileName", saveGame.getRelativeMetaFile());
					intent.putExtras(b);
					startActivity(intent);
				}
			});
			storyName.setText(story.getName());
			characterName.setText(_character.getName());

			return rowView;
		}

	}
}
