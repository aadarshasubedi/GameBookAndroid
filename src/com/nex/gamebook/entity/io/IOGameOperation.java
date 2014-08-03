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

import com.nex.gamebook.entity.character.Character;

public class IOGameOperation {

	public static final String SAVE_GAME_PREFIX = "gb_story_";
	
	public static Character loadCharacter(Context context, String filename) throws Exception {
		FileInputStream fis = context.openFileInput(filename);
		ObjectInputStream is = new ObjectInputStream(fis);
		Character simpleClass = (Character) is.readObject();
		is.close();
		return simpleClass;
	}
	
	public static void saveCharacter(Context context, Character character) throws Exception {
		SharedPreferences prefs = getPreferences(context);
		String fileName = SAVE_GAME_PREFIX + character.getId() + "_" + character.getStory().getId() + ".sav";
		FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(character);
		os.close();
		Editor editor = prefs.edit();
		Set<String> values = new HashSet<>();
		values.add(character.getStory().getXml());
		values.add(String.valueOf(character.getId()));
		editor.putStringSet(fileName, values);
		editor.apply();
	}
	
	public static SharedPreferences getPreferences(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("com.nex.gamebook", Context.MODE_PRIVATE);
		return prefs;
	}
	
}
