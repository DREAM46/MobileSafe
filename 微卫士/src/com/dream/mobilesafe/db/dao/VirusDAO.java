package com.dream.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * �������ݿ������
 * 
 * @author ������
 * 
 */
public class VirusDAO {

	public static final String path = "/data/data/com.dream.mobilesafe/files/antivirus.db";

	/**
	 * ��ѯ�Ƿ��ǲ���
	 * 
	 * @param md5
	 * @return ������������Ϣ�����Ϊnull���ǲ���
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
