package com.nex.gamebook.db;

import com.nex.gamebook.character.definition.Warrior;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GameBookSqlHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "gamebook.db";
	private static final int DATABASE_VERSION = 10;

	static interface TableCharacter {
		String TABLE = "characters";
		String ID = "_id";
		String MARKER = "marker";
		String HEALTH = "health";
		String ATTACK = "damage";
		String DEFENSE = "defense";
		String SKILL = "skill";
		String[] allColumns = { ID, MARKER, HEALTH, ATTACK, DEFENSE, SKILL};
		String CREATE_SQL = "create table " + TABLE + " (" + ID
				+ " integer primary key autoincrement, " + MARKER
				+ " text not null, " + HEALTH + " integer not null, "
				+ ATTACK + " integer not null, " + SKILL
				+ " integer not null, " + DEFENSE + " integer not null);";
		String[] data = new String[] { "insert into " + TABLE + " (" + MARKER
				+ ", " + HEALTH + ", " + ATTACK + ", " + DEFENSE + ", " + SKILL
				+ ") values ('"+Warrior.class.getCanonicalName()+"', 20, 6, 2, 2)" };
	}

	public GameBookSqlHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(TableCharacter.CREATE_SQL);
		for (String sql : TableCharacter.data) {
			database.execSQL(sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(GameBookSqlHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS characters");
		onCreate(db);
	}

}