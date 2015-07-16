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
 * 开始界面 主要功能有：1、软件更新的检查与实施
 * 
 * @author Administrator
 * 
 */

public class SplashActivity extends Activity implements AnimationListener {

	/**
	 * 要进行更新要装的路径
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
		tv_splash_version.setText("微卫士     "
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
	 * 启动动画进入界面
	 * 
	 * @param ll_splash
	 *            启动动画的View控件
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
	 * 将assert中的数据库文件拷贝到项目的根目录中
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
	 * 进入主界面
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
				// 由于此界面不需要回退，所以跳转完立刻将其finish
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
	 * 软件更新的异步任务
	 * 
	 * @author 温坤哲
	 * 
	 */
	private class UpdateTask extends AsyncTask<String, Void, String> {

		/**
		 * 更新软件的提示
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

				// 校验软件版本，若不是最新版本，则进行更新
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
						SplashActivity.this).setTitleText("您需要更新吗")
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
		 * 测试所要下载的apk包存在与否，若不存在，则下载，若存在，则直接安装
		 * 
		 * @param result
		 */
		private void apkIsExist(final String result) {
			// 由json解析得到的下载路径再进行字符创解析，得到apk名字
			String fileName = result.substring(result.lastIndexOf('/') + 1);

			File file = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/MobileSafe/download");

			// 看看下载文件夹存在与否，若不存在，则创建
			if (!file.exists()) {
				file.mkdirs();
			}
			path = file.getAbsolutePath() + "/" + fileName;

			file = new File(path);

			// 看看将要下载的apk存在与否，若不存在，则下载，若存在，则直接安装
			if (!file.exists()) {
				// new DownLoadTask().execute(result);

				download(result);

			} else {
				installApk(file);
			}
		}

		/**
		 * 借用github上的afinal_0.5_bin.jar快捷开发下载功能
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
					Toast.makeText(SplashActivity.this, "下载失败了",
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
					mProgressDialog.setMessage("下载中");
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
	 * 普通下载类
	 * 
	 * @author 温坤哲
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
					// 进行下载
					InputStream is = response.getEntity().getContent();

					// 要下载的文件的长度
					long dataLen = response.getEntity().getContentLength();

					// 已经下载的文件的总长度
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
				System.out.println("下载完成");

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
	 * 安装软件
	 * 
	 * @param path
	 *            要安装的apk包的路径
	 */
	private void installApk(File apkfile) {
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
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
	 * 创建快捷方式
	 */
	public void createDeskShortCut() {
		// 创建快捷方式的Intent
		Intent shortcutIntent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重复创建 ，如果重复的话就会有多个快捷方式了
		shortcutIntent.putExtra("duplicate", false);
		// 这个就是应用程序图标下面的名称
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));
		// 快捷图片
		Parcelable icon = Intent.ShortcutIconResource.fromContext(
				getApplicationContext(), R.drawable.logo);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		// 这个MainActivity是调用此方法的Activity
		Intent intent = new Intent(getApplicationContext(),
				SplashActivity.class);
		// 下面两个属性是为了当应用程序卸载时桌面 上的快捷方式会删除
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		// 点击快捷图片，运行的程序主入口
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// 最后一步就是发送广播
		sendBroadcast(shortcutIntent);
	}

}
