package com.dream.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 病毒数据库操作类
 * 
 * @author 温坤哲
 * 
 */
public class VirusDAO {

	public static final String path = "/data/data/com.dream.mobilesafe/files/antivirus.db";

	/**
	 * 查询是否是病毒
	 * 
	 * @param md5
	 * @return 病毒的描述信息，如果为null不是病毒
	 */
	public static String isVirus(String md5) {
		String result = null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.query("datable", new String[] { "desc" }, "md5=?",
				new String[] { md5 }, null, null, null);
		if (cursor.moveToNext()) {
			result = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return result;
	}
}
