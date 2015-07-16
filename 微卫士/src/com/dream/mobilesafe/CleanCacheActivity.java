package com.dream.mobilesafe;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_cleancache)
public class CleanCacheActivity extends Activity {

	@ViewInject(R.id.clean_pb)
	private ProgressBar clean_pb;

	@ViewInject(R.id.clean_tv_scanStatus)
	private TextView clean_tv_scanStatus;

	@ViewInject(R.id.ll_container)
	private LinearLayout ll_container;

	protected static final int SCANING = 1;
	public static final int SHOW_CACHE_INFO = 2;
	protected static final int SCAN_FINISH = 3;

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANING:
				clean_pb.setProgress(msg.arg1);
				clean_tv_scanStatus.setText("’˝‘⁄…®√Ë£∫" + (String) msg.obj);
				break;
			case SHOW_CACHE_INFO:

				final CacheInfo info = (CacheInfo) msg.obj;

				View view = View.inflate(CleanCacheActivity.this,
						R.layout.view_cleancache_item, null);

				ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				iv_icon.setImageDrawable(info.icon);

				TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
				tv_name.setText(info.name);

				TextView tv_cache = (TextView) view.findViewById(R.id.tv_cache);
				tv_cache.setText("ª∫¥Ê¥Û–°:"
						+ Formatter.formatFileSize(CleanCacheActivity.this,
								info.size));

				ImageView iv_delete = (ImageView) view
						.findViewById(R.id.iv_delete);
				iv_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Method[] methods = PackageManager.class.getMethods();
						for (Method method : methods) {
							try {
								if ("deleteApplicationCacheFiles".equals(method
										.getName())) {
									method.invoke(pm, info.packname,
											new IPackageDataObserver.Stub() {
												@Override
												public void onRemoveCompleted(
														String packageName,
														boolean succeeded)
														throws RemoteException {

												}
											});
								}
							} catch (Exception e) {
								Intent intent = new Intent();
								intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
								intent.addCategory(Intent.CATEGORY_DEFAULT);
								intent.setData(Uri.parse("package:"
										+ info.packname));
								startActivity(intent);
								e.printStackTrace();
							}
						}
					}
				});
				ll_container.addView(view, 0);
				break;
			case SCAN_FINISH:
				clean_pb.setProgress(0);
				clean_tv_scanStatus.setText("…®√ËÕÍ≥…");
				break;
			}
		}

	};

	private PackageManager pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		ll_container.removeAllViews();
		this.scanCache();
	}

	/**
	 * …®√Ëª∫¥Ê
	 */
	private void scanCache() {
		pm = this.getPackageManager();
		final List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
		clean_pb.setMax(packageInfos.size());

		new Thread(new Runnable() {

			@Override
			public void run() {
				int total = 0;
				for (PackageInfo packinfo : packageInfos) {
					try {
						String packname = packinfo.packageName;
						Method method = PackageManager.class.getMethod(
								"getPackageSizeInfo", String.class,
								IPackageStatsObserver.class);
						method.invoke(pm, packname, new MyObserver());
						total++;
						Message msg = handler.obtainMessage();
						msg.what = SCANING;
						msg.arg1 = total;
						msg.obj = packinfo.applicationInfo.loadLabel(pm)
								.toString();
						handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Message msg = handler.obtainMessage();
				msg.what = SCAN_FINISH;
				handler.sendMessage(msg);

			}
		}).start();
	}

	private class MyObserver extends IPackageStatsObserver.Stub {
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long cache = pStats.cacheSize;
			if (cache > 0) {
				try {
					Message msg = Message.obtain();
					msg.what = SHOW_CACHE_INFO;
					CacheInfo cacheInfo = new CacheInfo();
					cacheInfo.packname = pStats.packageName;
					cacheInfo.icon = pm.getApplicationInfo(pStats.packageName,
							0).loadIcon(pm);
					cacheInfo.name = pm
							.getApplicationInfo(pStats.packageName, 0)
							.loadLabel(pm).toString();
					cacheInfo.size = cache;
					msg.obj = cacheInfo;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class CacheInfo {
		Drawable icon;
		String name;
		long size;
		String packname;
	}

}
