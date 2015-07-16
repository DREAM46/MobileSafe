package com.dream.mobilesafe.service;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class AutoCleanService extends Service {

	private LockScreenReceiver receiver;
	private ActivityManager am;

	@Override
	public void onCreate() {
		super.onCreate();
		receiver = new LockScreenReceiver();
		am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		this.registerReceiver(receiver, new IntentFilter(
				Intent.ACTION_SCREEN_OFF));
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(receiver);
		receiver = null;
		am = null;
	}

	private class LockScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
			for (Iterator<RunningAppProcessInfo> iterator = infos.iterator(); iterator
					.hasNext();) {
				am.killBackgroundProcesses(iterator.next().processName);
			}
		}
	}

}
