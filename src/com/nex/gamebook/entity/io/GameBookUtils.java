package com.nex.gamebook.entity.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;

import com.nex.gamebook.entity.Mergable;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Story;
import com.nex.gamebook.story.parser.StoryXmlParser;

public class GameBookUtils {
	public static final String FOLDER = "stories";
	public static final String SAVE_GAME_PREFIX = "gb_story_";

	public static final String TIME = "time_";
	public static final String CHARACTER = "char_";
	public static final String STORY = "story_";
	public static final String VERSION = "version_";
	private static GameBookUtils instance;
	private Properties properties;
	private Context context;

	public static void initialize(Context context) {
		instance = new GameBookUtils();
		instance.context = context;
	}

	public static GameBookUtils getInstance() {
		return instance;
	}

	public SharedPreferences getPreferences() {
		SharedPreferences prefs = context.getSharedPreferences(
				"com.nex.gamebook", Context.MODE_PRIVATE);
		return prefs;
	}

	public String getValue(String what, Set<String> values) {
		for (String s : values) {
			if (s.startsWith(what)) {
				return s.substring(what.length());
			}
		}
		return null;
	}

	public void loadPropties(Story story) throws IOException {

		List<String> fileList = getRelevantLocalizedFiles(story);
		Properties prop = new Properties();
		for (String file : fileList) {
			try {
				InputStream fileStream = new FileInputStream(file);
				Reader reader = new InputStreamReader(fileStream, "Cp1250");

				prop.load(reader);
				fileStream.close();
			} catch (FileNotFoundException e) {
				Log.d("GameBookXmlParser", "Ignoring missing property file "
						+ file);
			}
		}
		this.properties = prop;
	}

	public String getText(String name) {
		if ("".equals(name) || name == null)
			return null;
		String text = this.properties.getProperty(name);
		return text == null ? "_" + name + "_" : text;
	}

	private List<String> getRelevantLocalizedFiles(Story story) {
		String defaultLang = "cs";
		String lang = Locale.getDefault().getLanguage();
		List<String> defaultTexts = new ArrayList<>();
		List<String> texts = new ArrayList<>();
		File root = getStoriesFolder(story.getPath() + File.separator + "texts"
				+ File.separator + lang + File.separator);
		boolean exist = root.exists();
		if (!exist) {
			getStoriesFolder(story.getPath() + File.separator + "texts"
					+ File.separator + defaultLang + File.separator);
		}
		if (exist && root.isDirectory()) {
			for (String file : root.list()) {
				boolean isDefault = isDefault(file);
				file = root + "/" + file;
				File test = new File(file);
				if (!test.exists()) {
					Log.w("GameBookProperties", "property not exists.");
				}
				if (isDefault) {
					defaultTexts.add(file);
				} else if (file.contains(lang)) {
					texts.add(file);
				}
			}
		}
		if (texts.isEmpty()) {
			texts = defaultTexts;
		}
		return texts;
	}

	public File getStoriesFolder(String subFolder) {
		return new File(getGamebookStorage(context) + File.separator + FOLDER
				+ File.separator + subFolder);
	}

	private boolean isDefault(String file) {
		int last_ = file.lastIndexOf("_");
		String dot = file.substring(last_, last_ + 2);
		return !".".endsWith(dot);
	}

	public Player loadCharacter(String filename) throws Exception {
		FileInputStream fis = context.openFileInput(filename);
		ObjectInputStream is = new ObjectInputStream(fis);
		Player simpleClass = (Player) is.readObject();
		is.close();
		return simpleClass;
	}

	public void saveCharacter(Player character) throws Exception {
		SharedPreferences prefs = getPreferences();
		String fileName = SAVE_GAME_PREFIX + character.getId() + "_"
				+ character.getStory().getId() + ".sav";
		FileOutputStream fos = context.openFileOutput(fileName,
				Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(character);
		os.close();
		Editor editor = prefs.edit();
		Set<String> values = new HashSet<>();
		values.add(TIME + String.valueOf(System.currentTimeMillis()));
		values.add(STORY + character.getStory().getFullpath());
		values.add(CHARACTER + String.valueOf(character.getId()));
		values.add(VERSION + String.valueOf(character.getStory().getVersion()));
		editor.putStringSet(fileName, values);
		editor.apply();
	}

	public static String getGamebookStorage(Context ctx) {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.nex.gamebook";
	}
	
	public static String createMethodName(String type, String fieldName) {
		String firstPart = fieldName.substring(0, 1).toUpperCase();
		String secondPart = fieldName.substring(1);
		return type + firstPart + secondPart;
	}
	
}
