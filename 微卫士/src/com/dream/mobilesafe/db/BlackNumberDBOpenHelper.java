package com.dream.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {

	public BlackNumberDBOpenHelper(Context context) {
		super(context, DBMsg.BLACKNUM_DB.DB_NAME, null,
				DBMsg.BLACKNUM_DB.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + DBMsg.BLACKNUM_TABLE.TABLE_NAME + " ("
				+ DBMsg.BLACKNUM_TABLE.COL_ID
				+ " integer primary key autoincrement,"
				+ DBMsg.BLACKNUM_TABLE.COL_NUM + " varchar(30) ,"
				+ DBMsg.BLACKNUM_TABLE.COL_MODE + " integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
