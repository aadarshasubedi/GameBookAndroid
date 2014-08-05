package com.nex.gamebook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.entity.io.IOGameOperation;
import com.nex.gamebook.playground.PlaygroundActivity;
import com.nex.gamebook.story.parser.StoryXmlParser;

public class LoadGameActivity extends Activity {
	private Map<String, Set<String>> savedGames;
	private List<String> keys;
	private StoryXmlParser parser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_load_game);
		savedGames = new HashMap<String, Set<String>>();
		this.keys = new ArrayList<String>();
		parser = new StoryXmlParser(this);
		SharedPreferences prefs = IOGameOperation.getPreferences(this);
		for(Map.Entry<String, ?> entry: prefs.getAll().entrySet()) {
			Set<String> value = (Set<String>) entry.getValue();
			String key = entry.getKey();
			if(key.startsWith(IOGameOperation.SAVE_GAME_PREFIX)) {
				savedGames.put(key,value);
				keys.add(key);
			}
		}
		ListView list = (ListView) findViewById(R.id.saved_games);
		list.setAdapter(new SavedGameItem(this));
	}
	class SavedGameItem extends ArrayAdapter<String> {
		Context context;
		public SavedGameItem(Context context) {
			super(context, R.layout.saved_game_item_layout, new String[keys.size()]);
			this.context = context;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final String fileName = keys.get(position);
			Set<String> values = savedGames.get(fileName);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.saved_game_item_layout, parent, false);
			TextView storyName = (TextView) rowView.findViewById(R.id.story_name);
			Iterator<String> it = values.iterator();
			String xml = it.next();
			String charId = it.next();
			TextView characterName = (TextView) rowView.findViewById(R.id.character);
			try {
				Story story = parser.loadStory(xml, false, true);
				Player character = story.getCharacter(Integer.valueOf(charId));
				storyName.setText(story.getName());
				characterName.setText(character.getName());
			} catch (Exception e) {
				Log.e("GameBookLoadActivity", "", e);
			}
			rowView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LoadGameActivity.this, PlaygroundActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("load_game", fileName);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			return rowView;
		}
	}
}
