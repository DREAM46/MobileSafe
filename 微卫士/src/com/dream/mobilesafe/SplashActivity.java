package com.dream.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.mobilesafe.utils.VersionUtils;
import com.dream.mobilesafe.view.CustomAlertDialog;
import com.dream.mobilesafe.view.CustomAlertDialog.OnCustomAlertDialogClickListener;

/**
 * ��ʼ���� ��Ҫ�����У�1��������µļ����ʵʩ
 * 
 * @author Administrator
 * 
 */

public class SplashActivity extends Activity implements AnimationListener {

	/**
	 * Ҫ���и���Ҫװ��·��
	 */
	private String path;

	private Resources resources;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		View ll_splash = this.findViewById(R.id.ll_splash);

		TextView tv_splash_version = (TextView) this
				.findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("΢��ʿ     "
				+ VersionUtils.getVersionName(this));

		this.startAnimation(ll_splash);

		resources = this.getResources();
		SharedPreferences preferences = this.getSharedPreferences(
				resources.getString(R.string.pre_name), Context.MODE_PRIVATE);
		boolean isStared = preferences.getBoolean("isStarted", false);
		String isAutoUpdate = resources.getString(R.string.pre_isAutoUpdate);

		if (!isStared) {
			Editor editor = preferences.edit();
			editor.putBoolean("isStarted", true);
			editor.putBoolean(isAutoUpdate, true);
			editor.putString("smsBlackNum", "");
			String dlgBg = resources.getString(R.string.pre_DlgBg);
			editor.putInt(dlgBg, 0);
			editor.commit();
			this.copyDB("address.db");
			this.copyDB("antivirus.db");
			this.createDeskShortCut();
			this.enterHomeAct(GuideActivity.class);
		} else {
			this.enterHomeAct(HomeActivity.class);
		}
		/*
		 * if (preferences.getBoolean(isAutoUpdate, false)) {
		 * 
		 * new UpdateTask().execute(this.getResources().getString(
		 * R.string.url_update));
		 * 
		 * } else { System.out.println("enter"); this.enterHomeAct(); }
		 */

	}

	/**
	 * ���������������
	 * 
	 * @param ll_splash
	 *            ����������View�ؼ�
	 */
	private void startAnimation(View ll_splash) {
		AnimationSet as = new AnimationSet(false);

		Animation ra = new RotateAnimation(330, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(1 * 1000);
		ra.setFillAfter(true);

		Animation sa = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa.setDuration(1 * 1000);
		sa.setFillAfter(true);

		Animation aa = new AlphaAnimation(0, 1);
		aa.setDuration(1 * 1000);
		aa.setFillAfter(true);

		as.addAnimation(ra);
		as.addAnimation(sa);
		as.addAnimation(aa);

		ll_splash.setAnimation(as);

	}

	/**
	 * ��assert�е����ݿ��ļ���������Ŀ�ĸ�Ŀ¼��
	 */
	private void copyDB(String dbName) {
		try {
			InputStream ins = this.getAssets().open(dbName);
			File file = new File(this.getFilesDir(), dbName);
			FileOutputStream fos = new FileOutputStream(file);
			byte[] data = new byte[1024];
			int len = -1;
			while ((len = ins.read(data)) != -1) {
				fos.write(data, 0, len);
			}
			ins.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//
	// http://192.168.56.1:8080/AndroidWeb/MobileSafe1.1.2

	/**
	 * ����������
	 */
	public void enterHomeAct(final Class clazz) {
		/*
		 * Animation aa = new AlphaAnimation(1f, 0f); aa.setDuration(1000);
		 * aa.setFillAfter(true); relative_splash.startAnimation(aa);
		 * aa.setAnimationListener(this);
		 */
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent();

				intent.setClass(SplashActivity.this, clazz);

				SplashActivity.this.startActivity(intent);
				SplashActivity.this.overridePendingTransition(
						R.anim.alpha_enter, R.anim.alpha_exit);
				// ���ڴ˽��治��Ҫ���ˣ�������ת�����̽���finish
				SplashActivity.this.finish();

			}
		}, 1500);
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		this.startActivity(new Intent(this, HomeActivity.class));
		this.finish();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	/**
	 * ������µ��첽����
	 * 
	 * @author ������
	 * 
	 */
	private class UpdateTask extends AsyncTask<String, Void, String> {

		/**
		 * �����������ʾ
		 */
		private String updateText = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			System.out.println("pre");
		}

		@Override
		protected String doInBackground(String... params) {
			System.out.println("pre");
			HttpGet get = new HttpGet(params[0]);
			HttpClient client = new DefaultHttpClient();
			String updateUrl = null;
			try {
				String json = null;
				HttpResponse response = client.execute(get);

				json = EntityUtils.toString(response.getEntity(), "utf-8");
				JSONObject obj = new JSONObject(json);
				String version = obj.getString("version");

				// У������汾�����������°汾������и���
				if (!version.equals(VersionUtils
						.getVersionName(SplashActivity.this))) {
					updateUrl = obj.getString("apkurl");
					updateText = obj.getString("description");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return updateUrl;
		}

		@Override
		protected void onPostExecute(final String result) {
			super.onPostExecute(result);
			System.out.println("result");
			if (!TextUtils.isEmpty(result)) {
				final CustomAlertDialog dialog = new CustomAlertDialog(
						SplashActivity.this).setTitleText("����Ҫ������")
						.setMsg(updateText).setNeutralGone();

				dialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						enterHomeAct(HomeActivity.class);
						dialog.dismiss();
					}
				});
				dialog.setOnCustomAlertDialogClickListener(new OnCustomAlertDialogClickListener() {

					@Override
					public void onPositive() {
						apkIsExist(result);
						dialog.dismiss();
					}

					@Override
					public void onNeutral() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onNegative() {
						dialog.dismiss();
						enterHomeAct(HomeActivity.class);
					}
				});
				dialog.show();
			} else {
				enterHomeAct(HomeActivity.class);
			}

		}

		/**
		 * ������Ҫ���ص�apk����������������ڣ������أ������ڣ���ֱ�Ӱ�װ
		 * 
		 * @param result
		 */
		private void apkIsExist(final String result) {
			// ��json�����õ�������·���ٽ����ַ����������õ�apk����
			String fileName = result.substring(result.lastIndexOf('/') + 1);

			File file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/MobileSafe/download");

			// ���������ļ��д�������������ڣ��򴴽�
			if (!file.exists()) {
				file.mkdirs();
			}
			path = file.getAbsolutePath() + "/" + fileName;

			file = new File(path);

			// ������Ҫ���ص�apk��������������ڣ������أ������ڣ���ֱ�Ӱ�װ
			if (!file.exists()) {
				// new DownLoadTask().execute(result);

				download(result);

			} else {
				installApk(file);
			}
		}

		/**
		 * ����github�ϵ�afinal_0.5_bin.jar��ݿ������ع���
		 * 
		 * @param result
		 */
		private void download(final String result) {
			FinalHttp finalHttp = new FinalHttp();
			finalHttp.download(result, path, new AjaxCallBack<File>() {
				ProgressDialog mProgressDialog = null;

				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					// TODO Auto-generated method stub
					super.onFailure(t, errorNo, strMsg);
					Toast.makeText(SplashActivity.this, "����ʧ����",
							Toast.LENGTH_LONG).show();
				}

				@Override
				public void onLoading(long count, long current) {
					// TODO Auto-generated method stub
					super.onLoading(count, current);
					mProgressDialog
							.setProgress((int) (current / (float) count * 100));
				}

				@Override
				public void onSuccess(File t) {
					// TODO Auto-generated method stub
					super.onSuccess(t);
					mProgressDialog.dismiss();
					installApk(t);
				}

				@Override
				public void onStart() {
					super.onStart();
					mProgressDialog = new ProgressDialog(SplashActivity.this);
					mProgressDialog.setMessage("������");
					mProgressDialog.setIndeterminate(false);
					mProgressDialog.setMax(100);
					mProgressDialog
							.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					mProgressDialog.show();
				}

			});
		}

	}

	/**
	 * ��ͨ������
	 * 
	 * @author ������
	 * 
	 */
	private class DownLoadTask extends AsyncTask<String, Integer, String> {
		ProgressDialog mProgressDialog = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			mProgressDialog.setProgress(values[0]);
			super.onProgressUpdate(values);
		}

		@Override
		protected String doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(params[0]);

			try {
				HttpResponse response = client.execute(get);

				if (response.getStatusLine().getStatusCode() == 200) {
					// ��������
					InputStream is = response.getEntity().getContent();

					// Ҫ���ص��ļ��ĳ���
					long dataLen = response.getEntity().getContentLength();

					// �Ѿ����ص��ļ����ܳ���
					long total = 0;

					File file = new File(path);

					OutputStream os = new FileOutputStream(file);

					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						total += len;
						publishProgress((int) (total * 100 / dataLen));
						os.write(buffer, 0, len);
					}

					os.close();
					is.close();
				}
				System.out.println("�������");

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return path;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			mProgressDialog.dismiss();

			installApk(new File(result));
		}

	}

	private static final int REQUEST_OK = 1;

	/**
	 * ��װ���
	 * 
	 * @param path
	 *            Ҫ��װ��apk����·��
	 */
	private void installApk(File apkfile) {
		if (!apkfile.exists()) {
			return;
		}
		// ͨ��Intent��װAPK�ļ�
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(apkfile),
				"application/vnd.android.package-archive");
		SplashActivity.this.startActivityForResult(intent, REQUEST_OK);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == RESULT_OK) {
			this.startActivity(new Intent(this, HomeActivity.class));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * ������ݷ�ʽ
	 */
	public void createDeskShortCut() {
		// ������ݷ�ʽ��Intent
		Intent shortcutIntent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// �������ظ����� ������ظ��Ļ��ͻ��ж����ݷ�ʽ��
		shortcutIntent.putExtra("duplicate", false);
		// �������Ӧ�ó���ͼ�����������
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));
		// ���ͼƬ
		Parcelable icon = Intent.ShortcutIconResource.fromContext(
				getApplicationContext(), R.drawable.logo);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		// ���MainActivity�ǵ��ô˷�����Activity
		Intent intent = new Intent(getApplicationContext(),
				SplashActivity.class);
		// ��������������Ϊ�˵�Ӧ�ó���ж��ʱ���� �ϵĿ�ݷ�ʽ��ɾ��
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		// ������ͼƬ�����еĳ��������
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// ���һ�����Ƿ��͹㲥
		sendBroadcast(shortcutIntent);
	}

}
