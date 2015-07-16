package com.dream.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dream.mobilesafe.db.BlackNumberDBOpenHelper;
import com.dream.mobilesafe.db.DBMsg;
import com.dream.mobilesafe.domin.BlackNumInfo;

/**
 * 黑名单数据库操作类
 * 
 * @author 温坤哲
 * 
 */
public class BlackNumDAO {

	private BlackNumberDBOpenHelper helper;

	public BlackNumDAO(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}

	/**
	 * 向黑名单数据库添加记录
	 * 
	 * @param blackNum
	 * @return 添加的记录的id，不成功为-1
	 */
	public long insert(BlackNumInfo blackNum) {

		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBMsg.BLACKNUM_TABLE.COL_NUM, blackNum.getNumber());
		values.put(DBMsg.BLACKNUM_TABLE.COL_MODE, blackNum.getMode());
		long id = db.insert(DBMsg.BLACKNUM_TABLE.TABLE_NAME, null, values);
		db.close();
		return id;
	}

	/**
	 * 向黑名单数据库删除记录
	 * 
	 * @param number
	 *            要删除的记录的电话号码
	 */
	public void delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(DBMsg.BLACKNUM_TABLE.TABLE_NAME, DBMsg.BLACKNUM_TABLE.COL_NUM
				+ "=?", new String[] { number });
		db.close();
	}

	/**
	 * 删除数据库中的所有记录
	 */
	public void deleteAll() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(DBMsg.BLACKNUM_TABLE.TABLE_NAME, null, null);
		db.close();
	}

	/**
	 * 向黑名单数据库查询全部记录
	 * 
	 * @return 全部记录的集合
	 */
	public List<BlackNumInfo> queryAll() {
		List<BlackNumInfo> list = new ArrayList<BlackNumInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(DBMsg.BLACKNUM_TABLE.TABLE_NAME, null, null,
				null, null, null, null);
		int indexOfNum = cursor.getColumnIndex(DBMsg.BLACKNUM_TABLE.COL_NUM);
		int indexOfMode = cursor.getColumnIndex(DBMsg.BLACKNUM_TABLE.COL_MODE);
		if (cursor.moveToLast()) {
			do {
				BlackNumInfo num = new BlackNumInfo();
				num.setNumber(cursor.getString(indexOfNum));
				num.setMode(cursor.getInt(indexOfMode));
				list.add(num);
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 查询数据库的部分数据
	 * 
	 * @param offset
	 *            开始的查询位置
	 * @param maxnumber
	 *            最多查询的记录数
	 * @return
	 */
	public List<BlackNumInfo> queryPart(int offset, int maxnumber) {
		List<BlackNumInfo> list = new ArrayList<BlackNumInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select " + DBMsg.BLACKNUM_TABLE.COL_NUM + ","
						+ DBMsg.BLACKNUM_TABLE.COL_MODE + " from "
						+ DBMsg.BLACKNUM_TABLE.TABLE_NAME + " order by "
						+ DBMsg.BLACKNUM_TABLE.COL_ID
						+ " desc  limit ? offset ? ",
				new String[] { String.valueOf(maxnumber),
						String.valueOf(offset) });
		int indexOfNum = cursor.getColumnIndex(DBMsg.BLACKNUM_TABLE.COL_NUM);
		int indexOfMode = cursor.getColumnIndex(DBMsg.BLACKNUM_TABLE.COL_MODE);
		while (cursor.moveToNext()) {
			BlackNumInfo num = new BlackNumInfo();
			num.setNumber(cursor.getString(indexOfNum));
			num.setMode(cursor.getInt(indexOfMode));
			list.add(num);
		}

		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 查询号码是否会在数据库中的记录中
	 * 
	 * @param number
	 *            要查询的号码
	 * @return 号码是否会在数据库中的记录中
	 */
	public boolean findNum(String number) {

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(DBMsg.BLACKNUM_TABLE.TABLE_NAME, null,
				DBMsg.BLACKNUM_TABLE.COL_NUM + "=?", new String[] { number },
				null, null, null);
		boolean flag = cursor.moveToNext();
		cursor.close();
		db.close();
		return flag;
	}

	/**
	 * 根据拦截号码查询拦截模式
	 * 
	 * @param number
	 *            拦截号码
	 * @return 拦截模式
	 */
	public int queryMode(String number) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(DBMsg.BLACKNUM_TABLE.TABLE_NAME, null,
				DBMsg.BLACKNUM_TABLE.COL_NUM + "=?", new String[] { number },
				null, null, null);
		int mode = -1;
		if (cursor.moveToNext())
			mode = cursor.getInt(cursor
					.getColumnIndex(DBMsg.BLACKNUM_TABLE.COL_MODE));

		cursor.close();
		db.close();
		return mode;
	}

	/**
	 * 修改黑名单号码的拦截模式
	 * 
	 * @param number
	 *            要进行修改的黑名单号码
	 * @param mode
	 *            新的拦截模式
	 */
	public void updateMode(String number, int mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBMsg.BLACKNUM_TABLE.COL_MODE, mode);
		db.update(DBMsg.BLACKNUM_TABLE.TABLE_NAME, values,
				DBMsg.BLACKNUM_TABLE.COL_NUM + "=?", new String[] { number });
		db.close();
	}

}
