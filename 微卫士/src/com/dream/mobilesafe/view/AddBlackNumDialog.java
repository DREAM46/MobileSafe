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
	 * Ϊȷ����ť��Ӽ�����
	 * 
	 * @param listener
	 *            ��ť��Ӽ�����
	 * @return ������
	 */
	public AddBlackNumDialog setOnPositiveClickListener(
			View.OnClickListener listener) {
		btn_dialog_positive.setOnClickListener(listener);
		return this;
	}

	/**
	 * ���ص绰��checkbox�Ƿ���
	 * 
	 * @return ���ص绰��checkbox�Ƿ���
	 */
	public boolean isCallChecked() {
		return check_addblack_call.isChecked();
	}

	/**
	 * ���ض��ŵ�checkbox�Ƿ���
	 * 
	 * @return ���ض��ŵ�checkbox�Ƿ���
	 */
	public boolean isSmsChecked() {
		return check_addblack_sms.isChecked();
	}

	/**
	 * �õ�����EditText������ֵ
	 * 
	 * @return EditText������ֵ
	 */
	public String getEditMsg() {
		return et_addblack_num.getText().toString().trim();
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}

}
