package com.dream.mobilesafe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.dream.mobilesafe.domin.SmsInfo;
import com.dream.mobilesafe.utils.SmsUtils;
import com.dream.mobilesafe.view.AUtilsItem;
import com.dream.mobilesafe.view.ShowCustomToast;

public class AUtilsActivity extends Activity implements OnClickListener {

	private AUtilsItem au_queyNum;
	private AUtilsItem au_backupSms;
	private AUtilsItem au_reductSms;

	private static final String PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/MobileSafe/backup";
	private static final String FILE_NAME = "backup.xml";
	private static final String XML_SMSS = "smss";
	private static final String XML_SMS = "sms";
	private static final String XML_ADDRESS = "address";
	private static final String XML_BODY = "body";
	private static final String XML_DATE = "date";
	private static final String XML_TYPE = "type";

	private ProgressBar pb_backup;
	private ProgressBar pb_restore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_autils);

		au_queyNum = (AUtilsItem) this.findViewById(R.id.au_queyNum);
		au_queyNum.setOnClickListener(this);

		au_backupSms = (AUtilsItem) this.findViewById(R.id.au_backupSms);
		au_backupSms.setOnClickListener(this);

		pb_backup = (ProgressBar) this.findViewById(R.id.pb_backup);

		au_reductSms = (AUtilsItem) this.findViewById(R.id.au_reductSms);
		au_reductSms.setOnClickListener(this);

		pb_restore = (ProgressBar) this.findViewById(R.id.pb_restore);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.au_queyNum:
			this.startActivity(new Intent(this,
					NumberAddressQueryActivity.class));
			break;
		case R.id.au_backupSms:
			new BackupSmsTask().execute();
			break;
		case R.id.au_reductSms:
			new RestoreSmsTask().execute();
			break;
		}
	}

	/**
	 * 短信备份异步任务类
	 * 
	 * @author 温坤哲
	 * 
	 */
	private class BackupSmsTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				backupSms(AUtilsActivity.this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			pb_backup.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pb_backup.setProgress(0);
			ShowCustomToast.show(AUtilsActivity.this, "备份成功");
		}

		/**
		 * 备份用户终端中的短信到用户的sd卡上
		 * 
		 * @param context
		 *            上下文对象
		 * @throws Exception
		 */
		private void backupSms(Context context) throws Exception {

			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File file = new File(PATH);
				if (!file.exists())
					file.mkdirs();
				file = new File(file, FILE_NAME);

				FileOutputStream os = new FileOutputStream(file);
				XmlSerializer serializer = Xml.newSerializer();
				serializer.setOutput(os, HTTP.UTF_8);
				serializer.startDocument(HTTP.UTF_8, true);
				serializer.startTag(null, XML_SMSS);

				Cursor cursor = SmsUtils.readSms(context);
				int count = 0;
				while (cursor.moveToNext()) {

					String address = cursor.getString(0);
					String body = cursor.getString(1);
					String date = cursor.getString(2);
					String type = cursor.getString(3);

					if (!TextUtils.isEmpty(address)) {

						serializer.startTag(null, XML_SMS);

						serializer.startTag(null, XML_ADDRESS);
						serializer.text(address);
						serializer.endTag(null, XML_ADDRESS);

						serializer.startTag(null, XML_BODY);
						serializer.text(body);
						serializer.endTag(null, XML_BODY);

						serializer.startTag(null, XML_DATE);
						serializer.text(date);
						serializer.endTag(null, XML_DATE);

						serializer.startTag(null, XML_TYPE);
						serializer.text(type);
						serializer.endTag(null, XML_TYPE);

						serializer.endTag(null, XML_SMS);

						count++;
						publishProgress((int) (100 * count / cursor.getCount()));
					}
				}

				cursor.close();
				serializer.endTag(null, XML_SMSS);
				serializer.endDocument();
			}

		}
	}

	/**
	 * 恢复短信异步任务类
	 * 
	 * @author 温坤哲
	 * 
	 */
	private class RestoreSmsTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			List<SmsInfo> infos = null;
			try {
				infos = this.getSms();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Uri uri = Uri.parse("content://sms/");

			for (int i = 0; i < infos.size(); i++) {

				SmsInfo info = infos.get(i);

				ContentValues values = new ContentValues();
				values.put(XML_BODY, info.getBody());
				values.put(XML_DATE, info.getDate());
				values.put(XML_TYPE, info.getType());
				values.put(XML_ADDRESS, info.getAddress());
				getContentResolver().insert(uri, values);

				this.publishProgress((int) 100 * i / infos.size());
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			pb_restore.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pb_backup.setProgress(0);
			ShowCustomToast.show(AUtilsActivity.this, "恢复成功");
		}

		/**
		 * 解析SD卡中的xml文件，获取将要进行短信回复的短信
		 * 
		 * @return
		 * @throws Exception
		 */
		private List<SmsInfo> getSms() throws Exception {
			List<SmsInfo> infos = new ArrayList<SmsInfo>();

			XmlPullParser parser = Xml.newPullParser();

			File file = new File(PATH + "/" + FILE_NAME);
			if (file.exists()) {

				FileInputStream fis = new FileInputStream(file);
				parser.setInput(fis, HTTP.UTF_8);

				int eventType = parser.START_DOCUMENT;
				SmsInfo info = null;

				while ((eventType = parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						String name = parser.getName();
						if (name.equals(XML_SMS)) {
							info = new SmsInfo();
						} else if (name.equals(XML_ADDRESS)) {
							info.setAddress(parser.nextText());
						} else if (name.equals(XML_BODY)) {
							info.setBody(parser.nextText());
						} else if (name.equals(XML_DATE)) {
							info.setDate(parser.nextText());
						} else if (name.equals(XML_TYPE)) {
							info.setType(parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						if (parser.getName().equals(XML_SMS)) {
							infos.add(info);
						}
						break;
					}
					parser.next();
				}
			}
			return infos;
		}
	}

}
