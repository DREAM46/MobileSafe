package com.dream.mobilesafe.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.protocol.HTTP;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.dream.mobilesafe.view.ShowCustomToast;

import android.R.xml;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Xml;

/**
 * ���Ź�����
 * 
 * @author ������
 * 
 */
public class SmsUtils {

	/**
	 * ��ѯ���ŵĹ�����
	 * 
	 * @param context
	 *            �����Ķ���
	 * @return ���ż�¼��Cursor�α����
	 */
	public static Cursor readSms(Context context) {
		Cursor cursor = null;
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms/");
		cursor = resolver.query(uri, new String[] { "address", "body", "date",
				"type" }, null, null, null);
		return cursor;
	}
}
