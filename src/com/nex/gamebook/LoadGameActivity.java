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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.SerializationMetadata;
import com.nex.gamebook.game.Story;
import com.nex.gamebook.playground.PlaygroundActivity;
import com.nex.gamebook.story.parser.StoryXmlParser;
import com.nex.gamebook.util.DialogBuilder;
import com.nex.gamebook.util.GameBookUtils;

public class LoadGameActivity extends BannerAdActivity {
	private Map<SerializationMetadata, Story> data = new HashMap<SerializationMetadata, Story>();
	private List<SerializationMetadata> keys;
	@Override
	protected void onPreCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.loading_layout);
	    new LoadViewTask().execute();
	}
	
	private class LoadViewTask extends AsyncTask<Void, Integer, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			List<SerializationMetadata> metas = GameBookUtils.getInstance().getSavedGames();
			for(SerializationMetadata m: metas) {
				String xml = m.getStory();
				try {
					StoryXmlParser parser = new StoryXmlParser(LoadGameActivity.this);
					LoadGameActivity.this.data.put(m, parser.loadStory(xml, false, true));
				} catch (Exception e) {
					Log.e("GameBookLoadActivity", "", e);
				}
			}
			LoadGameActivity.this.keys = new ArrayList<SerializationMetadata>(LoadGameActivity.this.data.keySet());
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			setContentView(R.layout.activity_load_game);
			ListView list = (ListView) findViewById(R.id.saved_games);
			list.setAdapter(new SavedGameItem(LoadGameActivity.this));
		}
	}
	class SavedGameItem extends ArrayAdapter<String> {
		Context context;
		public SavedGameItem(Context context) {
			super(context, R.layout.list_item, new String[keys.size()]);
			this.context = context;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final SerializationMetadata saveGame = keys.get(position);
//			Set<String> values = savedGames.get(fileName);
//			final String fileName = saveGame.getFile();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.list_item, parent, false);
			TextView storyName = (TextView) rowView.findViewById(R.id.story_name);
			int charId = saveGame.getCharacter();
			long timeInString = saveGame.getTime();
			String xml = saveGame.getStory();
			final int version = saveGame.getVersion();
			
			TextView characterName = (TextView) rowView.findViewById(R.id.character);
			TextView time = (TextView) rowView.findViewById(R.id.time);
			DateFormat df = DateFormat.getDateTimeInstance();
			String formatedTime = df.format(new Date(Long.valueOf(timeInString)));
			time.setText(formatedTime);
			try {
				final Story story = LoadGameActivity.this.data.get(saveGame);
				final Player _character = story.getCharacter(Integer.valueOf(charId));
				rowView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(story.getVersion() == version) {
							load();
							 LoadGameActivity.this.finish();
							return;
						}
						final DialogBuilder dialog = new DialogBuilder(LoadGameActivity.this)
				        .setTitle(R.string.story_changed)
				        .setText(R.string.story_changed_description);
						dialog.setNegativeButton(R.string.no, new OnClickListener() {
							@Override
							public void onClick(View v) {
								load();
								dialog.dismiss();
				            	LoadGameActivity.this.finish();
							}
						}).setPositiveButton(R.string.yes, new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(LoadGameActivity.this, PlaygroundActivity.class);
								Bundle bundle = new Bundle();
								bundle.putInt("character", _character.getId());
								bundle.putString("story", _character.getStory().getFullpath());
								bundle.putBoolean("load", true);
								intent.putExtras(bundle);
								getContext().startActivity(intent);
								dialog.dismiss();
								LoadGameActivity.this.finish();
							}
						}).show();
					}
					public void load() {
						Intent intent = new Intent(LoadGameActivity.this, PlaygroundActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("metadata", saveGame.getMetaFile());
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
