package com.dream.mobilesafe.receiver;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.dream.mobilesafe.R;
import com.dream.mobilesafe.service.GPSService;

/**
 * 短信接收的广播接受者
 * 
 * @author 温坤哲
 * 
 */
public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for (int i = 0; i < objs.length; i++) {
			// 具体的某一条短信
			SmsMessage message = SmsMessage.createFromPdu((byte[]) objs[i]);
			// 获取发送者的号码
			String address = message.getOriginatingAddress();
			SharedPreferences sp = context.getSharedPreferences("sp_mf",
					Context.MODE_PRIVATE);
			String smsBlackNum = sp.getString("smsBlackNum", "");
			if (smsBlackNum.contains(address)) {
				this.abortBroadcast();
				return;
			}
			// 获取短信内容
			String body = message.getMessageBody();
			String alarmNum = sp.getString("alarmNum", "");
			if (!address.contains(alarmNum))
				continue;
			if (body.equals("#*location*#")) {
				Intent intent2 = new Intent(context, GPSService.class);
				context.startService(intent2);
				String add = sp.getString("address", "no found");
				SmsManager.getDefault().sendTextMessage(address, null, add,
						null, null);
				context.stopService(intent2);
				this.abortBroadcast();
			} else if (body.equals("#*alarm*#")) {
				playAlarm(context);
				this.abortBroadcast();
			} else if (body.equals("#*wipedata*#")) {
				this.abortBroadcast();
			} else if (body.equals("#*lockscreen*#")) {
				this.lockScreen(context);
				this.abortBroadcast();
			}

		}

	}

	/**
	 * 播放报警音乐并振动
	 * 
	 * @param context
	 *            上下文对象
	 */
	private void playAlarm(Context context) {
		MediaPlayer player = MediaPlayer.create(context, R.raw.alarm);

		Vibrator vib = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(100 * 365 * 24 * 3600 * 1000);

		player.setVolume(1.0f, 1.0f);
		player.setLooping(true);
		player.start();

	}

	/**
	 * 锁屏
	 * 
	 * @param context
	 *            上下文对象
	 */
	private void lockScreen(Context context) {
		DevicePolicyManager dm = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName who = new ComponentName(context, LockReceiver.class);
		if (!dm.isAdminActive(who)) {
			this.activeAdmin(context, who);
		}
		dm.lockNow();
		// dm.resetPassword("1006", 0);//设置屏蔽密码
		dm.removeActiveAdmin(who);
	}

	/**
	 * 激活设备策略管理员
	 * 
	 * @param context
	 *            上下文对象
	 * @param who
	 *            组件
	 */
	private void activeAdmin(Context context, ComponentName who) {
		// 创建一个Intent
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		// 我要激活谁
		/*
		 * ComponentName mDeviceAdminSample = new ComponentName(context,
		 * LockReceiver.class);
		 */

		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, who);
		// 劝说用户开启
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"哥们开启我可以一键锁屏，你的按钮就不会经常失灵");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
