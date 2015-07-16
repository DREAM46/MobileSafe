package com.dream.mobilesafe.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * ���繤����
 * 
 * @author ������
 * 
 */
public class NetworkUtils {
	/**
	 * �������
	 * 
	 * @return �Ƿ�������
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
