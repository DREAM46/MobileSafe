package com.dream.mobilesafe.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.dream.mobilesafe.R;

public class AddBlackNumDialog extends Dialog implements
		android.view.View.OnClickListener {

	private EditText et_addblack_num;
	private CheckBox check_addblack_call;
	private CheckBox check_addblack_sms;
	private Button btn_dialog_negative;
	private Button btn_dialog_positive;

	public AddBlackNumDialog(Context context) {
		super(context, R.style.dialo_style);
		this.setContentView(R.layout.view_dialog_addblacknum);

		et_addblack_num = (EditText) this.findViewById(R.id.et_addblack_num);

		check_addblack_call = (CheckBox) this
				.findViewById(R.id.check_addblack_call);
		check_addblack_sms = (CheckBox) this
				.findViewById(R.id.check_addblack_sms);

		btn_dialog_negative = (Button) this
				.findViewById(R.id.btn_dialog_negative);
		btn_dialog_negative.setOnClickListener(this);
		btn_dialog_positive = (Button) this
				.findViewById(R.id.btn_dialog_positive);

	}

	/**
	 * 为确定按钮添加监听器
	 * 
	 * @param listener
	 *            按钮添加监听器
	 * @return 类自身
	 */
	public AddBlackNumDialog setOnPositiveClickListener(
			View.OnClickListener listener) {
		btn_dialog_positive.setOnClickListener(listener);
		return this;
	}

	/**
	 * 拦截电话的checkbox是否勾上
	 * 
	 * @return 拦截电话的checkbox是否勾上
	 */
	public boolean isCallChecked() {
		return check_addblack_call.isChecked();
	}

	/**
	 * 拦截短信的checkbox是否勾上
	 * 
	 * @return 拦截短信的checkbox是否勾上
	 */
	public boolean isSmsChecked() {
		return check_addblack_sms.isChecked();
	}

	/**
	 * 得到号码EditText的输入值
	 * 
	 * @return EditText的输入值
	 */
	public String getEditMsg() {
		return et_addblack_num.getText().toString().trim();
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}

}
