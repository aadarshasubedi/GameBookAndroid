package com.nex.gamebook.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nex.gamebook.entity.Story;
import com.nex.gamebook.entity.character.Character;
public class CharactersDatasource implements CharacterTable {
	  // Database fields
	  private SQLiteDatabase database;
	  private GameBookSqlHelper dbHelper;
	  public CharactersDatasource(Context context) {
	    dbHelper = new GameBookSqlHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public List<Character> findAll() {
	    List<Character> characters = new ArrayList<Character>();

	    Cursor cursor = database.query(TABLE,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      
			try {
				Character _char = cursorToEntity(cursor);
			    characters.add(_char);
			    cursor.moveToNext();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return characters;
	  }

	@Override
	public Character cursorToEntity(Cursor cursor) throws Exception {
		String marker = cursor.getString(cursor.getColumnIndex(CLASS));
		@SuppressWarnings("unchecked")
		Class<Character> _impl = (Class<Character>) Class.forName(marker);
	    Character _ch = _impl.newInstance();
	    _ch.setId(cursor.getLong(cursor.getColumnIndex(ID)));
		_ch.getStats().setHealth(cursor.getInt(cursor.getColumnIndex(HEALTH)));
		_ch.getStats().setAttack(cursor.getInt(cursor.getColumnIndex(ATTACK)));
		_ch.getStats().setDefense(cursor.getInt(cursor.getColumnIndex(DEFENSE)));
		_ch.getStats().setSkill(cursor.getInt(cursor.getColumnIndex(SKILL)));
		_ch.setStoryId(cursor.getLong(cursor.getColumnIndex(STORY_ID)));
		return _ch;
	}
	@Override
	public Character findById(Long id) {
		Cursor cursor = database.rawQuery("select * from " + TABLE + " where id=?", new String[]{String.valueOf(id)});
		cursor.moveToFirst();
		Character story = null;
		try {
			story = cursorToEntity(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		cursor.close();
		return story;
	}
	
	public List<Character> findByStory(Story story) {
		 List<Character> characters = new ArrayList<Character>();

		    Cursor cursor = database.rawQuery("select * from " + TABLE + " where "+STORY_ID+"=?", new String[]{String.valueOf(story.getId())});

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      
				try {
					Character _char = cursorToEntity(cursor);
				    characters.add(_char);
				    cursor.moveToNext();
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		    // make sure to close the cursor
		    cursor.close();
		    return characters;
	}
		
}
