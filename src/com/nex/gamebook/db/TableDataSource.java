package com.nex.gamebook.db;

import java.util.List;

import android.database.Cursor;

import com.nex.gamebook.entity.Entity;

public interface TableDataSource<E extends Entity> {

	
	List<E> findAll();
	E findById(Long id);
	E cursorToEntity(Cursor cursor) throws Exception;
	void open();
	void close();
}
