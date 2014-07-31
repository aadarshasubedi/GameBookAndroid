package com.nex.gamebook.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nex.gamebook.character.definition.Character;
import com.nex.gamebook.db.GameBookSqlHelper.TableCharacter;

import static com.nex.gamebook.db.GameBookSqlHelper.TableCharacter.allColumns;
public class CharactersDatasource {
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

	  public List<Character> getAllCharacters() {
	    List<Character> characters = new ArrayList<Character>();

	    Cursor cursor = database.query(GameBookSqlHelper.TableCharacter.TABLE,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      
			try {
				Character _char = cursorToCharacter(cursor);
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

	  private Character cursorToCharacter(Cursor cursor) throws Exception {
	    String marker = cursor.getString(cursor.getColumnIndex(TableCharacter.MARKER));
		Class<Character> _impl = (Class<Character>) Class.forName(marker);
	    Character _ch = _impl.newInstance();
		_ch.setHealth(cursor.getInt(cursor.getColumnIndex(TableCharacter.HEALTH)));
		_ch.setAttack(cursor.getInt(cursor.getColumnIndex(TableCharacter.ATTACK)));
		_ch.setDefense(cursor.getInt(cursor.getColumnIndex(TableCharacter.DEFENSE)));
		_ch.setSkill(cursor.getInt(cursor.getColumnIndex(TableCharacter.SKILL)));
		return _ch;
	  }
}
