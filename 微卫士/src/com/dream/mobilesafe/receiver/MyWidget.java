package com.dream.mobilesafe.receiver;

import com.dream.mobilesafe.service.UpdateWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class MyWidget extends AppWidgetProvider {

	private Intent service;

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		service = new Intent(context, UpdateWidgetService.class);
		context.startService(service);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		service = new Intent(context, UpdateWidgetService.class);
		context.startService(service);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		context.stopService(service);
	}

}
