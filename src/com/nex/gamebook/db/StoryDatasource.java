package com.nex.gamebook.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nex.gamebook.entity.Story;

public class StoryDatasource implements StoryTable {
	// Database fields
	private SQLiteDatabase database;
	private GameBookSqlHelper dbHelper;

	public StoryDatasource(Context context) {
		dbHelper = new GameBookSqlHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	@Override
	public Story cursorToEntity(Cursor cursor) throws Exception {
		String marker = cursor.getString(cursor.getColumnIndex(CLASS));
		@SuppressWarnings("unchecked")
		Class<Story> _impl = (Class<Story>) Class.forName(marker);
		Story _ch = _impl.newInstance();
		_ch.setId(cursor.getLong(cursor.getColumnIndex(ID)));
		return _ch;
	}

	@Override
	public List<Story> findAll() {
		List<Story> stories = new ArrayList<Story>();

		Cursor cursor = database.query(TABLE, allColumns, null, null, null,
				null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {

			try {
				Story _char = cursorToEntity(cursor);
				stories.add(_char);
				cursor.moveToNext();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// make sure to close the cursor
		cursor.close();
		return stories;
	}

	@Override
	public Story findById(Long id) {
		Cursor cursor = database.rawQuery("select * from " + TABLE + " where id=?", new String[]{String.valueOf(id)});
		cursor.moveToFirst();
		Story story = null;
		try {
			story = cursorToEntity(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		cursor.close();
		return story;
	}
}
