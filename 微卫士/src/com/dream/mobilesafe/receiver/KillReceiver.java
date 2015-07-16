package com.dream.mobilesafe.receiver;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 杀死进程接受者
 * 
 * @author 温坤哲
 * 
 */
public class KillReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();

		for (Iterator<RunningAppProcessInfo> iterator = infos.iterator(); iterator
				.hasNext();) {
			am.killBackgroundProcesses(iterator.next().processName);
		}

	}

}
