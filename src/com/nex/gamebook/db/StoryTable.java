package com.nex.gamebook.db;

import com.nex.gamebook.entity.Story;
import com.nex.gamebook.entity.ZombieApocalypseStory;


public interface StoryTable extends TableDataSource<Story>{
	String TABLE = "story";
	String ID = "id";
	String CLASS = "_class";
	
	String[] allColumns = { ID, CLASS };
	String CREATE_SQL = "create table " + TABLE + " (" + ID
			+ " integer primary key, "+CLASS+" text);";
	
	
	String[] data = new String[] { "insert into " + TABLE + " ("+ID+", " + CLASS
			+ ") values (1, '"+ZombieApocalypseStory.class.getCanonicalName()+"')" };
}
