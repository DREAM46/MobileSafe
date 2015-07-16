package com.dream.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.dream.mobilesafe.R;
import com.dream.mobilesafe.receiver.MyWidget;
import com.dream.mobilesafe.utils.SystemInfoUtils;

public class UpdateWidgetService extends Service {

	private Timer timer;
	private TimerTask task;

	private AppWidgetManager awm;

	private OnReceiver onReceiver;
	private OffReceiver offReceiver;

	/**
	 * Ϊ�˽�ʡ�û��ĵ��������û����豸����ʱӦ��ֹͣ���������ʱ��������������ʱӦ������ ��ˣ�д�������㲥�����߶������ͽ����������м���
	 */

	/**
	 * ��Ļ�����Ĺ㲥�����ߣ����ڸ��������ʱ����
	 * 
	 * @author ������
	 * 
	 */
	private class OnReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("on");
			startTimer();
		}

	}

	/**
	 * ��Ļ����ʱ�Ĺ㲥�����ߣ�����ֹͣ���������ʱ����
	 * 
	 * @author ������
	 * 
	 */
	private class OffReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("off");
			stopTimer();
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		awm = AppWidgetManager.getInstance(this);
		onReceiver = new OnReceiver();
		offReceiver = new OffReceiver();

		this.registerReceiver(onReceiver, new IntentFilter(
				Intent.ACTION_SCREEN_ON));
		this.registerReceiver(offReceiver, new IntentFilter(
				Intent.ACTION_SCREEN_OFF));

		startTimer();
	}

	private void startTimer() {
		if (timer == null && task == null) {
			timer = new Timer();
			task = new TimerTask() {
				// ����Widget�ؼ�
				@Override
				public void run() {
					ComponentName provider = new ComponentName(
							UpdateWidgetService.this, MyWidget.class);
					RemoteViews views = new RemoteViews(getPackageName(),
							R.layout.process_widget);
					views.setTextViewText(
							R.id.process_count,
							"�������е������"
									+ SystemInfoUtils
											.getRunningProcessCount(getApplicationContext()));
					views.setTextViewText(
							R.id.process_memory,
							"�����ڴ棺"
									+ SystemInfoUtils
											.getAvailMemory(getApplicationContext()));
					Intent intent = new Intent("com.dream.mobilesafe.kill");

					PendingIntent pi = PendingIntent.getBroadcast(
							getApplicationContext(), 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);
					views.setOnClickPendingIntent(R.id.btn_clear, pi);
					awm.updateAppWidget(provider, views);
				}
			};
			timer.schedule(task, 0, 1000 * 2);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(onReceiver);
		this.unregisterReceiver(offReceiver);
		onReceiver = null;
		offReceiver = null;
		stopTimer();
	}

	private void stopTimer() {
		if (timer != null && task != null) {
			timer.cancel();
			task.cancel();
			timer = null;
			task = null;
		}
	}

}
