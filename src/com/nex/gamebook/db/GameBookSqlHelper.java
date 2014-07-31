package com.nex.gamebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GameBookSqlHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "gamebook.db";
	private static final int DATABASE_VERSION = 17;
	
	public GameBookSqlHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		createStories(database);
		createCharacters(database);
	}
	
	private void createStories(SQLiteDatabase database) {
		database.execSQL(StoryTable.CREATE_SQL);
		for(String storySql: StoryTable.data) {
			database.execSQL(storySql);
		}
	}
	
	private void createCharacters(SQLiteDatabase database) {
		database.execSQL(CharacterTable.CREATE_SQL);
		for (String sql : CharacterTable.data) {
			database.execSQL(sql);
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(GameBookSqlHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS "+CharacterTable.TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+StoryTable.TABLE);
		onCreate(db);
	}

}