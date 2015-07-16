package com.dream.mobilesafe.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具类
 * 
 * @author 温坤哲
 * 
 */
public class NetworkUtils {
	/**
	 * 检查网络
	 * 
	 * @return 是否有网络
	 */
	public static boolean checkNetwork(Context context) {

		boolean flag = true;
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null) {
			flag = false;
		}
		return flag;
	}

}
