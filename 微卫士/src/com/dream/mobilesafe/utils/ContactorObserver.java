package com.dream.mobilesafe.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

/**
 * ͨѶ¼�۲���
 * 
 * @author ������
 * 
 */
public class ContactorObserver extends ContentObserver {

	/**
	 * ͨѶ¼�Ƿ����ı�ı�־���������仯������onChange( )��������Ϊtrue
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
