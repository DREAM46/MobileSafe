package com.dream.mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ��������ز�ѯ������
 * 
 * @author ������
 * 
 */
public class NumberAddressQueryUtils {
	/**
	 * ���ݵ绰�����ѯ������
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param number
	 *            �绰����
	 * @return ������
	 */
	public static String queryAddressByNumber(Context context, String number) {
		String address = null;
		if (number.startsWith("0")) {
			address = NumberAddressQueryUtils.queryAddressByGuHuaNumber(
					context.getFilesDir() + "/address.db", number);
		} else if (number.startsWith("1")) {
			address = NumberAddressQueryUtils.queryAddressByMobileNumber(
					context.getFilesDir() + "/address.db", number);
		}
		return address;
	}

	/**
	 * �����ֻ��绰�����ѯ������
	 * 
	 * @param path
	 *            ��ѯ�����ݿ�·��
	 * @param number
	 *            �ֻ��绰����
	 * @return ��������Ϣ
	 */
	public static String queryAddressByMobileNumber(String path, String number) {

		String address = "û�н���������ֻ�����";

		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		// �ֻ����� 13 14 15 16 18
		// �ֻ������������ʽ

		Cursor cursor = db
				.rawQuery(
						"select location from data2 where id = (select outkey from data1 where id = ?)",
						new String[] { number.substring(0, 7) });

		address = getResultFormCursor(cursor);

		db.close();

		return address;

	}

	/**
	 * ���ݹ̻��绰�����ѯ������
	 * 
	 * @param path
	 *            ��ѯ�����ݿ�·��
	 * @param number
	 *            �̻��绰����
	 * @return ��������Ϣ
	 */
	public static String queryAddressByGuHuaNumber(String path, String number) {

		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		String address = "û�н��������̻�����";
		String number1 = "";
		if (number.length() == 3 || number.length() == 4) {
			number = number.substring(1);
			address = queryAddress(db, path, number);

		} else {
			number1 = number.substring(1, 3);
			address = queryAddress(db, path, number1);
			if (address.equals("û�н��")) {
				number1 = number.substring(1, 4);
				address = queryAddress(db, path, number1);
			}
		}
		if (!address.equals("û�н��"))
			address = address.substring(0, address.length() - 2);
		db.close();

		return address;
	}

	private static String queryAddress(SQLiteDatabase db, String path,
			String number) {
		String address;

		Cursor cursor = db.query("data2", new String[] { "location" },
				"area=?", new String[] { number }, null, null, null);
		address = getResultFormCursor(cursor);
		return address;
	}

	/**
	 * ���α��л�ȡ���
	 * 
	 * @param cursor
	 *            �α�
	 * @return �����ؽ��
	 */
	private static String getResultFormCursor(Cursor cursor) {
		String address = "û�н��";
		while (cursor.moveToNext()) {
			address = cursor.getString(0);
		}

		cursor.close();
		return address;
	}
}
