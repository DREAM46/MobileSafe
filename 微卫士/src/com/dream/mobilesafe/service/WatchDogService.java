package com.dream.mobilesafe.service;

import java.util.List;

import com.dream.mobilesafe.db.dao.AppLockDAO;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class WatchDogService extends Service {

	private boolean isWatching = false;
	private AppLockDAO dao;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		final ActivityManager am = (ActivityManager) this
				.getSystemService(Context.ACTIVITY_SERVICE);
		dao = new AppLockDAO(this.getApplicationContext());
		new Thread() {
			@Override
			public void run() {
				super.run();
				while (isWatching) {
					List<RunningTaskInfo> infos = am.getRunningTasks(100);
					String packageName = infos.get(0).topActivity
							.getPackageName();
					boolean isExsit = dao.queryIsExsit(packageName);

					try {
						Thread.sleep(50);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isWatching = false;
		dao = null;

	}

}
