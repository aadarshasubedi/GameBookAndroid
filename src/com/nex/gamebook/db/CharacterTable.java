package com.nex.gamebook.db;

import com.nex.gamebook.entity.character.story1.Homeless;
import com.nex.gamebook.entity.character.story1.Soldier;

public interface CharacterTable extends TableDataSource<com.nex.gamebook.entity.character.Character>{
	String TABLE = "characters";
	String ID = "id";
	String CLASS = "class";
	String HEALTH = "health";
	String ATTACK = "damage";
	String DEFENSE = "defense";
	String SKILL = "skill";
	String STORY_ID = "story_id";
	
	String[] allColumns = { ID, CLASS, HEALTH, ATTACK, DEFENSE, SKILL};
	String CREATE_SQL = "create table " + TABLE + " (" 
	+ ID + " integer primary key, " 
	+ CLASS + " text not null, " 
	+ HEALTH + " integer not null, "
	+ ATTACK + " integer not null, " 
	+ SKILL + " integer not null, " 
	+ STORY_ID + " text not null, "
	+ DEFENSE + " integer not null, FOREIGN KEY("+STORY_ID+") REFERENCES "+StoryTable.TABLE+"("+StoryTable.ID+"));";
	String[] data = new String[] { 
			//soldier of zombie apocalypse story
			"insert into " + TABLE + " ("+ID+", " + CLASS
			+ ", " + HEALTH + ", " + ATTACK + ", " + DEFENSE + ", " + SKILL
			+ ", "+STORY_ID+") values (1, '"+Soldier.class.getCanonicalName()+"', 20, 6, 2, 2, 1)",
			//homeless of zombie apocalypse story
			"insert into " + TABLE + " ("+ID+", " + CLASS
			+ ", " + HEALTH + ", " + ATTACK + ", " + DEFENSE + ", " + SKILL
			+ ", "+STORY_ID+") values (2, '"+Homeless.class.getCanonicalName()+"', 15, 4, 3, 3, 1)"};
	
}
