package com.dream.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppLockDBOpenHelper extends SQLiteOpenHelper {

	public AppLockDBOpenHelper(Context context) {
		super(context, DBMsg.APP_DB.DB_NAME, null, DBMsg.APP_DB.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + DBMsg.APP_TABLE.TABLE_NAME + " ("
				+ DBMsg.APP_TABLE.COL_ID
				+ " integer primary key autoincrement,"
				+ DBMsg.APP_TABLE.COL_PACKAGE + " varchar(30) )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
