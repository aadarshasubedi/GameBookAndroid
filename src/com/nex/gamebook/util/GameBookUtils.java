package com.nex.gamebook.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import com.nex.gamebook.game.Player;
import com.nex.gamebook.game.Score;
import com.nex.gamebook.game.SerializationMetadata;
import com.nex.gamebook.game.Stats;
import com.nex.gamebook.game.Story;
import com.nex.gamebook.game.Bonus.StatType;
import com.nex.gamebook.story.parser.StoryXmlParser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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
		SharedPreferences prefs = context.getSharedPreferences("com.nex.gamebook", Context.MODE_PRIVATE);
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

	public void loadProperties(Story story) throws IOException {
		Properties prop = loadProperties(story.getPath());
		story.setProperties(prop);
	}
	public Properties loadProperties(String storypath) throws IOException {

		List<String> fileList = getRelevantLocalizedFiles(storypath);
		Properties prop = new Properties();
		// prop.
		for (String file : fileList) {
			try {
				InputStream fileStream = new FileInputStream(file);
				Reader reader = new InputStreamReader(fileStream);
				prop.load(reader);
				fileStream.close();
			} catch (FileNotFoundException e) {
				Log.d("GameBookXmlParser", "Ignoring missing property file " + file);
			}
		}
		return prop;
	}
	public String getText(String name, Story story) {
		if ("".equals(name) || name == null)
			return null;
		String text = story.getProperties().getProperty(name);
		return text == null ? "_" + name + "_" : text;
	}

	private List<String> getRelevantLocalizedFiles(String storypath) {
		List<String> texts = new ArrayList<>();
		addTexts(storypath, texts);
		addTexts("main", texts);
		return texts;
	}

	private void addTexts(String path, List<String> texts) {
		String defaultLang = "cs";
		String lang = Locale.getDefault().getLanguage();
		File root = getStoriesFolder(path + File.separator + "texts" + File.separator + lang + File.separator);
		boolean exist = root.exists();
		if (!exist) {
			getStoriesFolder(path + File.separator + "texts" + File.separator + defaultLang + File.separator);
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

	public File getStoriesFolder(String subFolder) {
		return new File(getGamebookStorage(context) + File.separator + FOLDER + File.separator + subFolder);
	}

	public File getSavesFolder(String subfolder, String fileName, boolean createSubfolders) {
		File f = new File(getGamebookStorage(context) + File.separator + "saves" + File.separator + subfolder + File.separator + fileName);
		if(!f.exists() && createSubfolders) {
			f.mkdirs();
		}
		return f;
	}
	public File getScoreFolder(String subfolder, String fileName, boolean createSubfolders) {
		File f = new File(getGamebookStorage(context) + File.separator + "scores" + File.separator + subfolder + File.separator + fileName);
		if(!f.exists() && createSubfolders) {
			f.mkdirs();
		}
		return f;
	}
	

	public String saveScore(Player character) throws Exception {
		File file = getScoreFolder("", "", true);
		File metaFile = getScoreFolder("meta", "", true);
		int index = metaFile.list().length + 1;
		String fileName = SCORE_GAME_PREFIX + character.getId() + "_" + character.getStory().getId() + "_" + index + ".sav";
		file = getScoreFolder("", fileName, false);
		metaFile = getScoreFolder("meta", fileName, false);
		file.createNewFile();
		metaFile.createNewFile();
		saveMetada(character, metaFile, fileName);
		Score score = new Score();
		score.saveScoreData(character);
		FileOutputStream fos = new FileOutputStream(file);
		XStream xStream = createXStream();
		xStream.toXML(score, fos);
		fos.close();
		return fileName;
	}

	public void saveGame(Player character) throws Exception {
		String fileName = createSaveGameFileName(character);
		File file = getSavesFolder("", "", true);
		File metaFile = getSavesFolder("meta", "", true);
		file = getSavesFolder("", fileName, false);
		metaFile = getSavesFolder("meta", fileName, false);
		file.createNewFile();
		metaFile.createNewFile();
		saveMetada(character, metaFile, fileName);
		SaveGameState state = character.createSaveGameState();
		FileOutputStream fos = new FileOutputStream(file);
		XStream xStream = createXStream();
		xStream.toXML(state, fos);
		fos.close();
	}

	private String createSaveGameFileName(Player character) {
		String fileName = SAVE_GAME_PREFIX + character.getId() + "_" + character.getStory().getId() + ".sav";
		return fileName;
	}
	public List<SerializationMetadata> getScores() {
		return loadMetadata(getScoreFolder("meta", "", false));
	}
	public List<SerializationMetadata> getSavedGames() {
		return loadMetadata(getSavesFolder("meta", "", false));
	}
	
	private List<SerializationMetadata> loadMetadata(File metaFolder) {
		List<SerializationMetadata> ls = new ArrayList<>();
		XStream stream = createXStream();
		if(metaFolder!=null && metaFolder.list()!=null)
		for(String metaFile:metaFolder.list()) {
			ls.add(loadSingleMetadata(stream, new File(metaFolder.getAbsoluteFile() + File.separator + metaFile)));
		}
		return ls;
	}
	public SerializationMetadata loadSingleMetadata(XStream stream, File file) {
		SerializationMetadata m = (SerializationMetadata) stream.fromXML(file);
		m.setMetaFile(file.getAbsolutePath());
		return m;
	}
	
	public Score loadScore(String filename) throws Exception {
		File file = getScoreFolder("", filename, false);
		FileInputStream fis = new FileInputStream(file);
		XStream xStream = createXStream();
		Score simpleClass = (Score) xStream.fromXML(fis);
		simpleClass.setProperties(GameBookUtils.getInstance().loadProperties(simpleClass.getStoryPath()));
		fis.close();
		return simpleClass;
	}
	public Player loadCharacter(SerializationMetadata metadata) throws Exception {
		File file = getSavesFolder("", metadata.getFile(), false);
		FileInputStream fis = new FileInputStream(file);
		StoryXmlParser parser = new StoryXmlParser(context);
		XStream xStream = createXStream();
		SaveGameState simpleClass = (SaveGameState) xStream.fromXML(fis);
		Story story = parser.loadStory(metadata.getStory(), true);
		Player pl = story.getCharacter(metadata.getCharacter());
		pl.updateSavedGameStates(simpleClass);
		fis.close();
		return pl;
	}

	private void saveMetada(Player character, File metaFile, String saveFile) throws Exception {
		FileOutputStream fos = new FileOutputStream(metaFile);
		XStream xStream = createXStream();
		SerializationMetadata sg = new SerializationMetadata();
		sg.setTime(System.currentTimeMillis());
		sg.setFile(saveFile);
		sg.setStory(character.getStory().getFullpath());
		sg.setCharacter(character.getId());
		sg.setVersion(character.getStory().getVersion());
		xStream.toXML(sg, fos);
		fos.close();
	}
	
	public void removeSavedGame(Player player) {
		String fileName = createSaveGameFileName(player);
		File delete = getSavesFolder("meta", fileName, false);
		delete.delete();
		delete = getSavesFolder("", fileName, false);
		delete.delete();
	}

	public static String getGamebookStorage(Context ctx) {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.nex.gamebook";
	}

	public static String createMethodName(String type, String fieldName) {
		String firstPart = fieldName.substring(0, 1).toUpperCase();
		String secondPart = fieldName.substring(1);
		return type + firstPart + secondPart;
	}

	public XStream createXStream() {
		XStream xstream = new XStream(new DomDriver());
		return xstream;
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
