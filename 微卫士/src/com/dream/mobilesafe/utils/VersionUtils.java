package com.dream.mobilesafe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 版本工具类
 * 
 * @author 温坤哲
 * 
 */
public class VersionUtils {
	/**
	 * 动态得到软件的版本号
	 */
	public static String getVersionName(Context context) {
		// PackageManager用来管理手机的package
		PackageManager pm = context.getPackageManager();
		try {
			// 得到package的清单文件
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}
