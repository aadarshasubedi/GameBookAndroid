package com.nex.gamebook;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.Score;
import com.nex.gamebook.game.SerializationMetadata;
import com.nex.gamebook.game.Story;
import com.nex.gamebook.story.parser.StoryXmlParser;
import com.nex.gamebook.util.GameBookUtils;

public class ScoreBoardActivity extends Activity {
//	private Map<String, Set<String>> savedGames;
	private List<SerializationMetadata> keys;
	private StoryXmlParser parser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_score_board);
		this.keys = GameBookUtils.getInstance().getScores();
		parser = new StoryXmlParser(this);
		ListView list = (ListView) findViewById(R.id.scores);
		list.setAdapter(new ScoreItem(this));
		
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
			final String fileName = saveGame.getFile();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.list_item, parent, false);
			TextView storyName = (TextView) rowView.findViewById(R.id.story_name);
			int charId = saveGame.getCharacter();
			long timeInString = saveGame.getTime();
			String xml = saveGame.getStory();
			TextView characterName = (TextView) rowView.findViewById(R.id.character);
			TextView time = (TextView) rowView.findViewById(R.id.time);
			DateFormat df = DateFormat.getDateTimeInstance();
			String formatedTime = df.format(new Date(timeInString));
			time.setText(formatedTime);
			try {
				final Story story = parser.loadStory(xml, false, true);
				final Player _character = story.getCharacter(charId);
				rowView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(v.getContext(), ScoreActivity.class);
						Bundle b = new Bundle();
						b.putString("fileName", fileName);
						intent.putExtras(b);
						startActivity(intent);
					}
				});
				storyName.setText(story.getName());
				characterName.setText(_character.getName());
			} catch (Exception e) {
				Log.e("GameBookLoadActivity", "", e);
			}
			return rowView;
		}
		
	}
}
