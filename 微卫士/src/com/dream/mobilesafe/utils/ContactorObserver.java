package com.dream.mobilesafe.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

/**
 * 通讯录观察类
 * 
 * @author 温坤哲
 * 
 */
public class ContactorObserver extends ContentObserver {

	/**
	 * 通讯录是否发生改变的标志，若发生变化，则在onChange( )方法中设为true
	 */
	public static boolean isChange = false;

	private Context context;

	public ContactorObserver(Handler handler, Context context) {
		super(handler);
		this.context = context;
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		isChange = true;
	}
}
