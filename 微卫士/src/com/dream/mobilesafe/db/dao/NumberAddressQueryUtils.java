package com.dream.mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 号码归属地查询工具类
 * 
 * @author 温坤哲
 * 
 */
public class NumberAddressQueryUtils {
	/**
	 * 根据电话号码查询归属地
	 * 
	 * @param context
	 *            上下文对象
	 * @param number
	 *            电话号码
	 * @return 归属地
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
	 * 根据手机电话号码查询归属地
	 * 
	 * @param path
	 *            查询的数据库路径
	 * @param number
	 *            手机电话号码
	 * @return 归属地信息
	 */
	public static String queryAddressByMobileNumber(String path, String number) {

		String address = "没有结果，请检查手机号码";

		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		// 手机号码 13 14 15 16 18
		// 手机号码的正则表达式

		Cursor cursor = db
				.rawQuery(
						"select location from data2 where id = (select outkey from data1 where id = ?)",
						new String[] { number.substring(0, 7) });

		address = getResultFormCursor(cursor);

		db.close();

		return address;

	}

	/**
	 * 根据固话电话号码查询归属地
	 * 
	 * @param path
	 *            查询的数据库路径
	 * @param number
	 *            固话电话号码
	 * @return 归属地信息
	 */
	public static String queryAddressByGuHuaNumber(String path, String number) {

		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		String address = "没有结果，请检查固话号码";
		String number1 = "";
		if (number.length() == 3 || number.length() == 4) {
			number = number.substring(1);
			address = queryAddress(db, path, number);

		} else {
			number1 = number.substring(1, 3);
			address = queryAddress(db, path, number1);
			if (address.equals("没有结果")) {
				number1 = number.substring(1, 4);
				address = queryAddress(db, path, number1);
			}
		}
		if (!address.equals("没有结果"))
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
	 * 从游标中获取结果
	 * 
	 * @param cursor
	 *            游标
	 * @return 归属地结果
	 */
	private static String getResultFormCursor(Cursor cursor) {
		String address = "没有结果";
		while (cursor.moveToNext()) {
			address = cursor.getString(0);
		}

		cursor.close();
		return address;
	}
}
