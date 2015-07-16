package com.dream.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.dream.mobilesafe.R;

/**
 * 监听开机启动的接受者类
 * 
 * @author Administrator
 * 
 */
public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// 读取SharedPreferences文件中的绑定SIM卡的序列号
		Resources resources = context.getResources();
		SharedPreferences sp = context.getSharedPreferences(
				resources.getString(R.string.pre_name), Context.MODE_PRIVATE);
		String boundNum = sp.getString(
				resources.getString(R.string.pre_boundNum), "");

		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		// 如果SharedPreferences文件中的绑定SIM卡的序列号不为空且与当前设备SIM卡的序列号不同的话就说明设备可能被盗
		if (!TextUtils.isEmpty(boundNum)
				&& !boundNum.equals(manager.getSimSerialNumber())) {
			
		}
	}

}
