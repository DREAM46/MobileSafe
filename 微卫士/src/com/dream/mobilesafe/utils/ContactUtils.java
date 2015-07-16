package com.dream.mobilesafe.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.dream.mobilesafe.domin.ContactInfo;

/**
 * 通讯录工具类
 * 
 * @author 温坤哲
 * 
 */
public class ContactUtils {

	/*
	 * 根据电话号码取得联系人姓名
	 */
	public static String getContactNameByPhoneNumber(Context context,
			String address) {
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER };

		// 将自己添加到 msPeers 中
		Cursor cursor = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				projection,
				ContactsContract.CommonDataKinds.Phone.NUMBER + " = '"
						+ address + "'", null, null);

		if (cursor == null) {
			return "";
		}
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);

			// 取得联系人名字
			int nameFieldColumnIndex = cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
			String name = cursor.getString(nameFieldColumnIndex);
			return name;
		}
		return "";
	}

	/**
	 * 得到全部的联系人信息
	 * 
	 * @param context
	 *            上下文对象
	 * @return 全部联系人信息
	 */
	public static List<ContactInfo> getContactInfo(Context context) {

		List<ContactInfo> infos = new ArrayList<ContactInfo>();

		// 获得通讯录信息 ，URI是ContactsContract.Contacts.CONTENT_URI
		Cursor cursor = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while (cursor.moveToNext()) {

			// Log.v(TAG, "…name…" + name);
			// 查看给联系人是否有电话，返回结果是String类型，1表示有，0表是没有
			String hasPhone = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if (hasPhone.equalsIgnoreCase("1")) {
				hasPhone = "true";

				// 获得通讯录中每个联系人的ID
				String contactId = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				// 获得通讯录中联系人的名字
				String contactName = cursor
						.getString(cursor
								.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

				// 当联系人中有电话这一项时才将其加入到集合中

				Cursor phonesCursor = context.getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phonesCursor.moveToNext()) {
					String phoneNumber = phonesCursor
							.getString(phonesCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

					// Log.v(TAG, "…phoneNumber…  " + phoneNumber);
					// numbers.add(phoneNumber);

					ContactInfo info = new ContactInfo();
					info.setId(contactId);
					info.setName(contactName);
					info.setNumber(phoneNumber);
					infos.add(info);
				}

				phonesCursor.close();
			}
		}
		cursor.close();
		return infos;
	}

	/**
	 * 读取手机里面的联系人
	 * 
	 * @return
	 */
	public static List<ContactInfo> getContactInfo1(Context context) {

		// 把所有的联系人
		List<ContactInfo> infos = new ArrayList<ContactInfo>();

		// 得到一个内容解析器
		ContentResolver resolver = context.getContentResolver();
		// raw_contacts uri
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri uriData = Uri.parse("content://com.android.contacts/data");

		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);

		while (cursor.moveToNext()) {
			String contact_id = cursor.getString(0);

			if (contact_id != null) {
				// 具体的某一个联系人
				ContactInfo info = new ContactInfo();

				Cursor dataCursor = resolver.query(uriData, new String[] {
						"data1", "mimetype" }, "contact_id=?",
						new String[] { contact_id }, null);

				while (dataCursor.moveToNext()) {

					String data1 = dataCursor.getString(0);
					String mimetype = dataCursor.getString(1);

					if ("vnd.android.cursor.item/name".equals(mimetype)) {
						// 联系人的姓名
						info.setName(data1);
					} else if ("vnd.android.cursor.item/phone_v2"
							.equals(mimetype)) {

						info.setNumber(data1);
					}
				}
				infos.add(info);
				dataCursor.close();
			}
		}
		cursor.close();
		return infos;
	}

	/**
	 * 判断是否是一个合法的手机号码
	 * 
	 * @param phoneNumber
	 *            要进行判断的手机号码
	 * @return 布尔值表示是否是一个合法的手机号码
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		/*
		 * 可接受的电话格式有：
		 */
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
		/*
		 * 可接受的电话格式有：
		 */
		String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);

		Pattern pattern2 = Pattern.compile(expression2);
		Matcher matcher2 = pattern2.matcher(inputStr);
		if (matcher.matches() || matcher2.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 根据电话号码删除通话记录
	 * 
	 * @param context
	 *            上下文对象，用于获取内容提供者
	 * @param number
	 *            电话号码
	 */
	public static void deleteCallLog(Context context, String number) {
		ContentResolver resolver = context.getContentResolver();
		// 呼叫记录uri的路径
		Uri uri = Uri.parse("content://call_log/calls");
		resolver.delete(uri, "number=?", new String[] { number });
	}
}