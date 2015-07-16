package com.dream.mobilesafe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * �汾������
 * 
 * @author ������
 * 
 */
public class VersionUtils {
	/**
	 * ��̬�õ�����İ汾��
	 */
	public static String getVersionName(Context context) {
		// PackageManager���������ֻ���package
		PackageManager pm = context.getPackageManager();
		try {
			// �õ�package���嵥�ļ�
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}
