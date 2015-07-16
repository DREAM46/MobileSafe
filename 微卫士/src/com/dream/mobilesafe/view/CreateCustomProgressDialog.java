package com.dream.mobilesafe.view;

import android.content.Context;

/**
 * 创建自定义对话框类
 * 
 * @author 温坤哲
 * 
 */
public class CreateCustomProgressDialog {

	/**
	 * 创建自定义的等待对话框
	 * 
	 * @param context
	 *            上下文对象
	 * @return 对话框对象
	 */
	public static CustomProgressDialog createCustomProgressDialog(
			Context context) {
		CustomProgressDialog dialog = new CustomProgressDialog(context);
		dialog.setMsg("处理中.......");
		return dialog;
	}
}
