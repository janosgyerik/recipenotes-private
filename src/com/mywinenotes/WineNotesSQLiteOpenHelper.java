package com.mywinenotes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WineNotesSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sqlite3.db";
	private static final int DATABASE_VERSION = 1;
	private final String sql_create;

	WineNotesSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		//context.deleteDatabase(DATABASE_NAME);
		
		String tmp_sql_create = null;
		try {
			InputStream instream = context.getAssets().open("sql_create.sql");
			BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
			String line;
			StringBuilder builder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			tmp_sql_create = builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sql_create = tmp_sql_create;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql_create);
		db.execSQL("insert into main_wine (name) values ('Julienas');");
		db.execSQL("insert into main_wine (name) values ('Jura');");
		db.execSQL("insert into main_wine (name) values ('Bourgueil');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
}