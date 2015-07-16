package com.dream.mobilesafe.utils;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * У��ĳ�������Ƿ񻹻��Ź�����
 * 
 * @author ������
 * 
 */
public class ServiceUtils {

	/**
	 * У��ĳ�������Ƿ����
	 * 
	 * @param context
	 *            ������
	 * @param serviceName
	 *            �������ƣ���������
	 * @return ĳ�������Ƿ����booleanֵ
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
