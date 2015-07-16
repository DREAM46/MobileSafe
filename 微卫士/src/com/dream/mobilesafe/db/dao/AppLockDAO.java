package com.dream.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dream.mobilesafe.db.AppLockDBOpenHelper;
import com.dream.mobilesafe.db.DBMsg;

/**
 * ���������ݿ������
 * 
 * @author ������
 * 
 */
public class AppLockDAO {

	private AppLockDBOpenHelper helper;

	public AppLockDAO(Context context) {
		this.helper = new AppLockDBOpenHelper(context);
	}

	/**
	 * ����������ݿ����һ������
	 * 
	 * @param packageName
	 *            ���������ݿ�İ���
	 * @return �����¼��id��
	 */
	public long insert(String packageName) {
		long id = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBMsg.APP_TABLE.COL_PACKAGE, packageName);
		id = db.insert(DBMsg.APP_TABLE.TABLE_NAME, null, values);
		db.close();
		return id;
	}

	/**
	 * ɾ��������������һ����¼
	 * 
	 * @param packageName
	 *            Ҫ��ɾ���ļ�¼�İ�������
	 * @return ɾ���ļ�¼��
	 */
	public int delete(String packageName) {
		int count = -1;
		SQLiteDatabase db = helper.getWritableDatabase();
		count = db.delete(DBMsg.APP_TABLE.TABLE_NAME,
				DBMsg.APP_TABLE.COL_PACKAGE + "=?",
				new String[] { packageName });
		db.close();
		return count;
	}

	/**
	 * ��ѯ�������������ݿ��ȫ����¼
	 * 
	 * @return ȫ����¼�ļ���
	 */
	public List<String> queryAll() {
		List<String> names = new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(DBMsg.APP_TABLE.TABLE_NAME,
				new String[] { DBMsg.APP_TABLE.COL_PACKAGE }, null, null, null,
				null, null);

		while (cursor.moveToNext()) {
			names.add(cursor.getString(0));
		}

		cursor.close();
		db.close();

		return names;
	}

	/**
	 * ��ѯһ��������������¼�Ƿ����
	 * 
	 * @param packageName
	 *            ����������
	 * @return �Ƿ���ڵ�booleanֵ
	 */
	public boolean queryIsExsit(String packageName) {
		boolean isExsit = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(DBMsg.APP_TABLE.TABLE_NAME, null,
				DBMsg.APP_TABLE.COL_PACKAGE + "=?",
				new String[] { packageName }, null, null, null);
		isExsit = cursor.moveToNext();
		cursor.close();
		db.close();
		return isExsit;
	}

}
