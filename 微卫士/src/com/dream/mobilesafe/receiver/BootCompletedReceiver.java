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
 * �������������Ľ�������
 * 
 * @author Administrator
 * 
 */
public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// ��ȡSharedPreferences�ļ��еİ�SIM�������к�
		Resources resources = context.getResources();
		SharedPreferences sp = context.getSharedPreferences(
				resources.getString(R.string.pre_name), Context.MODE_PRIVATE);
		String boundNum = sp.getString(
				resources.getString(R.string.pre_boundNum), "");

		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		// ���SharedPreferences�ļ��еİ�SIM�������кŲ�Ϊ�����뵱ǰ�豸SIM�������кŲ�ͬ�Ļ���˵���豸���ܱ���
		if (!TextUtils.isEmpty(boundNum)
				&& !boundNum.equals(manager.getSimSerialNumber())) {
			
		}
	}

}
