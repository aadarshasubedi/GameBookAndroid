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
import java.lang.reflect.Method;
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

import com.nex.gamebook.entity.Bonus.StatType;
import com.nex.gamebook.entity.Player;
import com.nex.gamebook.entity.Stats;
import com.nex.gamebook.entity.Story;

public class GameBookUtils {
	public static final String FOLDER = "stories";
	public static final String SAVE_GAME_PREFIX = "gb_story_";
	public static final String SCORE_GAME_PREFIX = "gb_score_";
	
	public static final String TIME = "time_";
	public static final String CHARACTER = "char_";
	public static final String STORY = "story_";
	public static final String VERSION = "version_";
	private static GameBookUtils instance;
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
//		prop.
		for (String file : fileList) {
			try {
				InputStream fileStream = new FileInputStream(file);
				Reader reader = new InputStreamReader(fileStream);
				prop.load(reader);
				fileStream.close();
			} catch (FileNotFoundException e) {
				Log.d("GameBookXmlParser", "Ignoring missing property file "
						+ file);
			}
		}
		story.setProperties(prop);
	}

	public String getText(String name, Story story) {
		if ("".equals(name) || name == null)
			return null;
		String text = story.getProperties().getProperty(name);
		return text == null ? "_" + name + "_" : text;
	}

	private List<String> getRelevantLocalizedFiles(Story story) {
		List<String> texts = new ArrayList<>();
		addTexts(story.getPath(), texts);
		addTexts("main", texts);
		return texts;
	}

	private void addTexts(String path, List<String> texts) {
		String defaultLang = "cs";
		String lang = Locale.getDefault().getLanguage();
		File root = getApplicationFolder(path + File.separator + "texts"
				+ File.separator + lang + File.separator);
		boolean exist = root.exists();
		if (!exist) {
			getApplicationFolder(path + File.separator + "texts"
					+ File.separator + defaultLang + File.separator);
		}
		if (exist && root.isDirectory()) {
			for (String file : root.list()) {
				file = root + "/" + file;
				File test = new File(file);
				if (!test.exists()) {
					Log.w("GameBookProperties", "property not exists.");
				}
				texts.add(file);
			}
		}
	}

	public File getApplicationFolder(String subFolder) {
		return new File(getGamebookStorage(context) + File.separator + FOLDER
				+ File.separator + subFolder);
	}

	
	public Set<String> getFileNames(String prefix) {
		Set<String> keys = new HashSet<>();
		for(Map.Entry<String, ?> entry: getPreferences().getAll().entrySet()) {
			String key = entry.getKey();
			if(key.startsWith(prefix)) {
				keys.add(key);
			}
		}
		return keys;
	}
	
	public String saveCharacterForScore(Player character) throws Exception {
		int index = getFileNames(SCORE_GAME_PREFIX).size() + 1;
		String fileName = SCORE_GAME_PREFIX + character.getId() + "_" + character.getStory().getId() + "_"+index+".sav";
		save(character, fileName);
		return fileName;
	}
	public void saveCharacter(Player character) throws Exception {
		save(character, createSaveGameFileName(character));
	}
	private String createSaveGameFileName(Player character) {
		String fileName = SAVE_GAME_PREFIX + character.getId() + "_"
				+ character.getStory().getId() + ".sav";
		return fileName;
	}
	public Player loadCharacter(String filename) throws Exception {
		FileInputStream fis = context.openFileInput(filename);
		ObjectInputStream is = new ObjectInputStream(fis);
		Player simpleClass = (Player) is.readObject();
		loadPropties(simpleClass.getStory());
		is.close();
		return simpleClass;
	}

	

	private void save(Player character, String fileName) throws Exception {
		SharedPreferences prefs = getPreferences();
		
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
	
	public void removeSavedGame(Player player) {
		Editor editor = getPreferences().edit();
		String fileName = createSaveGameFileName(player);
		context.deleteFile(fileName);
		editor.remove(fileName);
		editor.commit();
	}
	
	public static String getGamebookStorage(Context ctx) {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + "com.nex.gamebook";
	}

	public static String createMethodName(String type, String fieldName) {
		String firstPart = fieldName.substring(0, 1).toUpperCase();
		String secondPart = fieldName.substring(1);
		return type + firstPart + secondPart;
	}
	
	public static int setStatByType(Stats destination, StatType type, int value) throws Exception {
		Method m = Stats.class.getDeclaredMethod(GameBookUtils.createMethodName("set", type.name().toLowerCase()), int.class);
		return (int) m.invoke(destination, value);
	}
	public static int getStatByType(Stats destination, StatType type) throws Exception {
		Method m = Stats.class.getDeclaredMethod(GameBookUtils.createMethodName("get", type.name().toLowerCase()), new Class<?>[0]);
		return (int) m.invoke(destination, new Object[0]);
	}
}
