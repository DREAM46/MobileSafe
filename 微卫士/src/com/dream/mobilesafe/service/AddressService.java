package com.dream.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.mobilesafe.R;
import com.dream.mobilesafe.db.dao.NumberAddressQueryUtils;
import com.dream.mobilesafe.view.ShowCustomToast;

public class AddressService extends Service {

	private static MyPhoneListener listener;
	private Context context;
	private OutCallReceiver receiver;
	private TelephonyManager tm;
	private WindowManager wm;
	private View view;
	private WindowManager.LayoutParams params;
	private SharedPreferences sp;

	@Override
	public void onCreate() {
		super.onCreate();

		context = this.getApplicationContext();

		if (listener == null)
			listener = new MyPhoneListener(context);

		tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		receiver = new OutCallReceiver();
		context.registerReceiver(receiver, new IntentFilter(
				Intent.ACTION_NEW_OUTGOING_CALL));
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消监听来电
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;

		// 取消取消去电广播接受者
		context.unregisterReceiver(receiver);
		receiver = null;
	}

	private class MyPhoneListener extends PhoneStateListener {

		private Context context;

		public MyPhoneListener(Context context) {
			this.context = context;
		}

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);

			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:

				String address = NumberAddressQueryUtils.queryAddressByNumber(
						context, incomingNumber);
				ShowCustomToast.show(context, address);
			case TelephonyManager.CALL_STATE_IDLE:
				if (view != null) {
					wm.removeView(view);
					view = null;
				}
				break;
			}

		}
	}

	/**
	 * 去电的接受者
	 * 
	 * @author 温坤哲
	 * 
	 */
	private class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 得到去电电话号码

			String number = this.getResultData();

			if (number.startsWith("+86"))
				number = number.substring(3).trim();

			if (number.startsWith("0") || number.startsWith("1"))

				showToast(NumberAddressQueryUtils.queryAddressByNumber(context,
						number));
		}
	}

	/**
	 * 用Toast去显示信息
	 * 
	 * @param address
	 *            显示的归属地信息
	 */
	private void showToast(String address) {

		Context contex = getApplicationContext();
		view = View.inflate(contex, R.layout.view_show_toast, null);
		RelativeLayout layout = (RelativeLayout) view
				.findViewById(R.id.layout_toast);

		Resources resources = this.getResources();
		sp = this.getSharedPreferences(resources.getString(R.string.pre_name),
				Context.MODE_PRIVATE);
		int dlgbg = sp.getInt(resources.getString(R.string.pre_DlgBg), 0);

		switch (dlgbg) {
		case 0:
			layout.setBackgroundResource(R.drawable.call_locate_white);
			break;
		case 1:
			layout.setBackgroundResource(R.drawable.call_locate_orange);
			break;
		case 2:
			layout.setBackgroundResource(R.drawable.call_locate_blue);
			break;
		case 3:
			layout.setBackgroundResource(R.drawable.call_locate_gray);
			break;
		case 4:
			layout.setBackgroundResource(R.drawable.call_locate_green);
			break;
		}

		TextView tv_toast = (TextView) view.findViewById(R.id.tv_toast);
		tv_toast.setText(address);

		view.setOnTouchListener(new OnTouchListener() {
			int startX = 0;
			int startY = 0;

			Editor editor = sp.edit();

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int MoveX = (int) event.getRawX();
					int MoveY = (int) event.getRawY();

					int dx = MoveX - startX;
					int dy = MoveY - startY;

					params.x += dx;
					params.y += dy;

					// 考虑边界问题
					if (params.x < 0)
						params.x = 0;
					if (params.y < 0)
						params.y = 0;

					if (params.x > wm.getDefaultDisplay().getWidth()
							- view.getWidth())
						params.x = wm.getDefaultDisplay().getWidth()
								- view.getWidth();

					if (params.y > wm.getDefaultDisplay().getHeight()
							- view.getHeight())
						params.y = wm.getDefaultDisplay().getHeight()
								- view.getHeight();

					wm.updateViewLayout(view, params);

					startX = (int) event.getRawX();
					startY = (int) event.getRawY();

					break;
				case MotionEvent.ACTION_UP:
					editor.putInt("startX", params.x);
					editor.putInt("startY", params.y);
					editor.commit();
					break;
				}

				return false;
			}
		});

		view.setOnClickListener(new OnClickListener() {

			long mits[] = new long[2];

			@Override
			public void onClick(View v) {
				System.arraycopy(mits, 1, mits, 0, mits.length - 1);
				mits[mits.length - 1] = SystemClock.currentThreadTimeMillis();
				if (mits[0] - mits[mits.length - 1] < 1000) {
					params.x = wm.getDefaultDisplay().getWidth() / 2
							- view.getWidth() / 2;
					wm.updateViewLayout(view, params);
					Editor editor = sp.edit();
					editor.putInt("lastx", params.x);
					editor.commit();
				}
			}
		});

		wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		params = new LayoutParams();
		params.height = LayoutParams.WRAP_CONTENT;
		params.width = LayoutParams.WRAP_CONTENT;
		params.flags = LayoutParams.FLAG_NOT_FOCUSABLE
				| LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSPARENT;
		params.gravity = Gravity.TOP + Gravity.LEFT;

		params.x = sp.getInt("startX", 0);
		params.y = sp.getInt("startY", 0);
		// 具有电话优先级，并且记得加权限
		params.type = LayoutParams.TYPE_PRIORITY_PHONE;

		// params.type = LayoutParams.TYPE_TOAST;

		wm.addView(view, params);
	}
}
