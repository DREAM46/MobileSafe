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
 * ͨѶ¼������
 * 
 * @author ������
 * 
 */
public class ContactUtils {

	/*
	 * ���ݵ绰����ȡ����ϵ������
	 */
	public static String getContactNameByPhoneNumber(Context context,
			String address) {
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.NUMBER };

		// ���Լ���ӵ� msPeers ��
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

			// ȡ����ϵ������
			int nameFieldColumnIndex = cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
			String name = cursor.getString(nameFieldColumnIndex);
			return name;
		}
		return "";
	}

	/**
	 * �õ�ȫ������ϵ����Ϣ
	 * 
	 * @param context
	 *            �����Ķ���
	 * @return ȫ����ϵ����Ϣ
	 */
	public static List<ContactInfo> getContactInfo(Context context) {

		List<ContactInfo> infos = new ArrayList<ContactInfo>();

		// ���ͨѶ¼��Ϣ ��URI��ContactsContract.Contacts.CONTENT_URI
		Cursor cursor = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while (cursor.moveToNext()) {

			// Log.v(TAG, "��name��" + name);
			// �鿴����ϵ���Ƿ��е绰�����ؽ����String���ͣ�1��ʾ�У�0����û��
			String hasPhone = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if (hasPhone.equalsIgnoreCase("1")) {
				hasPhone = "true";

				// ���ͨѶ¼��ÿ����ϵ�˵�ID
				String contactId = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				// ���ͨѶ¼����ϵ�˵�����
				String contactName = cursor
						.getString(cursor
								.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

				// ����ϵ�����е绰��һ��ʱ�Ž�����뵽������

				Cursor phonesCursor = context.getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phonesCursor.moveToNext()) {
					String phoneNumber = phonesCursor
							.getString(phonesCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

					// Log.v(TAG, "��phoneNumber��  " + phoneNumber);
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
	 * ��ȡ�ֻ��������ϵ��
	 * 
	 * @return
	 */
	public static List<ContactInfo> getContactInfo1(Context context) {

		// �����е���ϵ��
		List<ContactInfo> infos = new ArrayList<ContactInfo>();

		// �õ�һ�����ݽ�����
		ContentResolver resolver = context.getContentResolver();
		// raw_contacts uri
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri uriData = Uri.parse("content://com.android.contacts/data");

		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);

		while (cursor.moveToNext()) {
			String contact_id = cursor.getString(0);

			if (contact_id != null) {
				// �����ĳһ����ϵ��
				ContactInfo info = new ContactInfo();

				Cursor dataCursor = resolver.query(uriData, new String[] {
						"data1", "mimetype" }, "contact_id=?",
						new String[] { contact_id }, null);

				while (dataCursor.moveToNext()) {

					String data1 = dataCursor.getString(0);
					String mimetype = dataCursor.getString(1);

					if ("vnd.android.cursor.item/name".equals(mimetype)) {
						// ��ϵ�˵�����
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
	 * �ж��Ƿ���һ���Ϸ����ֻ�����
	 * 
	 * @param phoneNumber
	 *            Ҫ�����жϵ��ֻ�����
	 * @return ����ֵ��ʾ�Ƿ���һ���Ϸ����ֻ�����
	 */
	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		/*
		 * �ɽ��ܵĵ绰��ʽ�У�
		 */
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
		/*
		 * �ɽ��ܵĵ绰��ʽ�У�
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
	 * ���ݵ绰����ɾ��ͨ����¼
	 * 
	 * @param context
	 *            �����Ķ������ڻ�ȡ�����ṩ��
	 * @param number
	 *            �绰����
	 */
	public static void deleteCallLog(Context context, String number) {
		ContentResolver resolver = context.getContentResolver();
		// ���м�¼uri��·��
		Uri uri = Uri.parse("content://call_log/calls");
		resolver.delete(uri, "number=?", new String[] { number });
	}
}