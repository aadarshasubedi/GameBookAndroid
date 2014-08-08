package com.nex.gamebook;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.nex.gamebook.entity.io.GameBookUtils;
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
		SharedPreferences prefs = GameBookUtils.getInstance().getPreferences();
		for(Map.Entry<String, ?> entry: prefs.getAll().entrySet()) {
			Set<String> value = (Set<String>) entry.getValue();
			String key = entry.getKey();
			if(key.startsWith(GameBookUtils.SAVE_GAME_PREFIX)) {
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
			super(context, R.layout.list_saved_game_item_layout, new String[keys.size()]);
			this.context = context;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final String fileName = keys.get(position);
			Set<String> values = savedGames.get(fileName);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.list_saved_game_item_layout, parent, false);
			TextView storyName = (TextView) rowView.findViewById(R.id.story_name);
			String charId = GameBookUtils.getInstance().getValue(GameBookUtils.CHARACTER, values);
			String timeInString = GameBookUtils.getInstance().getValue(GameBookUtils.TIME, values);
			String xml = GameBookUtils.getInstance().getValue(GameBookUtils.STORY, values);
			final int version = Integer.valueOf(GameBookUtils.getInstance().getValue(GameBookUtils.VERSION, values));
			
			TextView characterName = (TextView) rowView.findViewById(R.id.character);
			TextView time = (TextView) rowView.findViewById(R.id.time);
			DateFormat df = DateFormat.getDateTimeInstance();
			String formatedTime = df.format(new Date(Long.valueOf(timeInString)));
			time.setText(formatedTime);
			try {
				final Story story = parser.loadStory(xml, false, true);
				final Player _character = story.getCharacter(Integer.valueOf(charId));
				rowView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//Ask the user if they want to quit
						if(story.getVersion() == version) {
							load();
							 LoadGameActivity.this.finish();
							return;
						}
				        new AlertDialog.Builder(LoadGameActivity.this)
				        .setIcon(android.R.drawable.ic_dialog_alert)
				        .setTitle(R.string.story_changed)
				        .setMessage(R.string.story_changed_description)
				        .setPositiveButton(R.string.continue_story, new DialogInterface.OnClickListener() {
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	load();
				            	 LoadGameActivity.this.finish();
				            }
	
				        }).setNegativeButton(R.string.begin_again, new DialogInterface.OnClickListener() {
	
				            @Override
				            public void onClick(DialogInterface dialog, int which) {
				            	Intent intent = new Intent(LoadGameActivity.this,
										PlaygroundActivity.class);
								Bundle bundle = new Bundle();
								bundle.putInt("character", _character.getId());
								bundle.putString("story", _character.getStory().getFullpath());
								bundle.putBoolean("load", true);
								
								intent.putExtras(bundle);
								getContext().startActivity(intent);
								 LoadGameActivity.this.finish();
				            }
	
				        }).show();
				       
					}
					public void load() {
						Intent intent = new Intent(LoadGameActivity.this, PlaygroundActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("load_game", fileName);
						bundle.putBoolean("load", true);
						intent.putExtras(bundle);
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
