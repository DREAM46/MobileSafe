package com.dream.mobilesafe.view;

import android.content.Context;

/**
 * �����Զ���Ի�����
 * 
 * @author ������
 * 
 */
public class CreateCustomProgressDialog {

	/**
	 * �����Զ���ĵȴ��Ի���
	 * 
	 * @param context
	 *            �����Ķ���
	 * @return �Ի������
	 */
	public static CustomProgressDialog createCustomProgressDialog(
			Context context) {
		CustomProgressDialog dialog = new CustomProgressDialog(context);
		dialog.setMsg("������.......");
		return dialog;
	}
}
