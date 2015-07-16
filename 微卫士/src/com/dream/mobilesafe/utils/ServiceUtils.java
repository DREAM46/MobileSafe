package com.dream.mobilesafe.utils;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 校验某个服务是否还活着工具类
 * 
 * @author 温坤哲
 * 
 */
public class ServiceUtils {

	/**
	 * 校验某个服务是否活着
	 * 
	 * @param context
	 *            上下文
	 * @param serviceName
	 *            服务名称（带包名）
	 * @return 某个服务是否活着boolean值
	 */
	public static boolean isServiceRunning(Context context, String serviceName) {
		boolean isRunning = false;

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = am.getRunningServices(100);

		for (Iterator<RunningServiceInfo> iterator = infos.iterator(); iterator
				.hasNext();) {
			RunningServiceInfo info = iterator.next();
			if (info.service.getClassName().equals(serviceName)) {
				isRunning = true;
				break;
			}
		}

		return isRunning;
	}
}
