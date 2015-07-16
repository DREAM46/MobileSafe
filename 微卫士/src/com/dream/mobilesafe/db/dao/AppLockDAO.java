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
 * 程序锁数据库操作类
 * 
 * @author 温坤哲
 * 
 */
public class AppLockDAO {

	private AppLockDBOpenHelper helper;

	public AppLockDAO(Context context) {
		this.helper = new AppLockDBOpenHelper(context);
	}

	/**
	 * 向程序锁数据库插入一条数据
	 * 
	 * @param packageName
	 *            程序锁数据库的包名
	 * @return 插入记录的id号
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
	 * 删除程序锁包名的一条记录
	 * 
	 * @param packageName
	 *            要被删除的记录的包名参数
	 * @return 删除的记录数
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
	 * 查询程序锁包名数据库的全部记录
	 * 
	 * @return 全部记录的集合
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
	 * 查询一条程序锁包名记录是否存在
	 * 
	 * @param packageName
	 *            程序锁包名
	 * @return 是否存在的boolean值
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
