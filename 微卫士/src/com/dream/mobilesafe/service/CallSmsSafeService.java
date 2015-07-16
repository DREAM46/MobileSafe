package com.dream.mobilesafe.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.dream.mobilesafe.db.dao.BlackNumDAO;
import com.dream.mobilesafe.utils.ContactUtils;

public class CallSmsSafeService extends Service {

	private InnerSmsReceiver receiver;
	private TelephonyManager tm;
	private BlackNumDAO dao;
	private InruptPhoneListener listener;

	@Override
	public void onCreate() {
		super.onCreate();
		receiver = new InnerSmsReceiver();

		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		listener = new InruptPhoneListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		dao = new BlackNumDAO(getApplicationContext());

		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		this.registerReceiver(receiver, filter);
	}

	private class InruptPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				int mode = dao.queryMode(incomingNumber);
				if (mode == 1 || mode == 3) {

					Uri uri = Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true,
							new ContentObserver(new Handler()) {
								@Override
								public void onChange(boolean selfChange) {
									super.onChange(selfChange);
									ContactUtils.deleteCallLog(
											getApplicationContext(),
											incomingNumber);
									getApplicationContext()
											.getContentResolver()
											.unregisterContentObserver(this);
								}
							});

					// 挂断电话
					endCall();

				}

				break;
			}
		}

		private void endCall() {
			// IBinder iBinder = ServiceManager.getService(TELEPHONY_SERVICE);
			try {
				// 加载servicemanager的字节码
				Class clazz = CallSmsSafeService.class.getClassLoader()
						.loadClass("android.os.ServiceManager");
				Method method = clazz.getDeclaredMethod("getService",
						String.class);
				IBinder ibinder = (IBinder) method.invoke(null,
						TELEPHONY_SERVICE);
				ITelephony.Stub.asInterface(ibinder).endCall();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(receiver);
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
	}

	private class InnerSmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objs = (Object[]) intent.getExtras().get("pdus");

			for (int i = 0; i < objs.length; i++) {
				// 具体的某一条短信
				SmsMessage message = SmsMessage.createFromPdu((byte[]) objs[i]);
				// 获取发送者的号码
				String address = message.getOriginatingAddress();

				// new QueryModeTask().execute(address);

				int mode = dao.queryMode(address);
				if (mode == 3 || mode == 2) {
					abortBroadcast();
				}
			}
		}

	}

}
