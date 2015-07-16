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
 * ���������ݿ������
 * 
 * @author ������
 * 
 */
public class BlackNumDAO {

	private BlackNumberDBOpenHelper helper;

	public BlackNumDAO(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}

	/**
	 * ����������ݿ���Ӽ�¼
	 * 
	 * @param blackNum
	 * @return ��ӵļ�¼��id�����ɹ�Ϊ-1
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
	 * ����������ݿ�ɾ����¼
	 * 
	 * @param number
	 *            Ҫɾ���ļ�¼�ĵ绰����
	 */
	public void delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(DBMsg.BLACKNUM_TABLE.TABLE_NAME, DBMsg.BLACKNUM_TABLE.COL_NUM
				+ "=?", new String[] { number });
		db.close();
	}

	/**
	 * ɾ�����ݿ��е����м�¼
	 */
	public void deleteAll() {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete(DBMsg.BLACKNUM_TABLE.TABLE_NAME, null, null);
		db.close();
	}

	/**
	 * ����������ݿ��ѯȫ����¼
	 * 
	 * @return ȫ����¼�ļ���
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
	 * ��ѯ���ݿ�Ĳ�������
	 * 
	 * @param offset
	 *            ��ʼ�Ĳ�ѯλ��
	 * @param maxnumber
	 *            ����ѯ�ļ�¼��
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
	 * ��ѯ�����Ƿ�������ݿ��еļ�¼��
	 * 
	 * @param number
	 *            Ҫ��ѯ�ĺ���
	 * @return �����Ƿ�������ݿ��еļ�¼��
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
	 * �������غ����ѯ����ģʽ
	 * 
	 * @param number
	 *            ���غ���
	 * @return ����ģʽ
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
	 * �޸ĺ��������������ģʽ
	 * 
	 * @param number
	 *            Ҫ�����޸ĵĺ���������
	 * @param mode
	 *            �µ�����ģʽ
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
