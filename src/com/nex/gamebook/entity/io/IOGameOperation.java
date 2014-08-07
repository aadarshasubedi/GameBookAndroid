package com.nex.gamebook.entity.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.nex.gamebook.entity.Player;

public class IOGameOperation {

	public static final String SAVE_GAME_PREFIX = "gb_story_";
	
	public static final String TIME = "time_";
	public static final String CHARACTER = "char_";
	public static final String STORY = "story_";
	
	public static Player loadCharacter(Context context, String filename) throws Exception {
		FileInputStream fis = context.openFileInput(filename);
		ObjectInputStream is = new ObjectInputStream(fis);
		Player simpleClass = (Player) is.readObject();
		is.close();
		return simpleClass;
	}
	
	public static void saveCharacter(Context context, Player character) throws Exception {
		SharedPreferences prefs = getPreferences(context);
		String fileName = SAVE_GAME_PREFIX + character.getId() + "_" + character.getStory().getId() + ".sav";
		FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(character);
		os.close();
		Editor editor = prefs.edit();
		Set<String> values = new HashSet<>();
		values.add(TIME + String.valueOf(System.currentTimeMillis()));
		values.add(STORY + character.getStory().getFullpath());
		values.add(CHARACTER + String.valueOf(character.getId()));
		
		editor.putStringSet(fileName, values);
		editor.apply();
	}
	
	public static SharedPreferences getPreferences(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("com.nex.gamebook", Context.MODE_PRIVATE);
		return prefs;
	}
	
	public static String getValue(String what, Set<String> values) {
		for(String s: values) {
			if(s.startsWith(what)) {
				return s.substring(what.length());
			}
		}
		return null;
	}
	
}
