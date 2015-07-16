package com.dream.mobilesafe.view;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dream.mobilesafe.R;

public class CustomInputDialog extends Dialog implements
		android.view.View.OnClickListener, TextWatcher {

	private EditText view_et_input1;
	private EditText view_et_input2;

	public CustomInputDialog(Context context) {
		super(context, R.style.dialo_style);
		setContentView(R.layout.view_dialog_input);

		Button view_input_negative = (Button) this
				.findViewById(R.id.view_input_negative);
		view_input_negative.setOnClickListener(this);

		view_et_input1 = (EditText) this.findViewById(R.id.view_et_input1);
		view_et_input2 = (EditText) this.findViewById(R.id.view_et_input2);

		((TextView) this.findViewById(R.id.tv_input_title)).setText("��������");

		view_et_input1.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}

	/**
	 * Ϊȷ����ť�󶨼�����
	 * 
	 * @param listener
	 *            ȷ����ť�󶨼�����
	 * @return �Ի�����
	 */
	public CustomInputDialog setOnPositiveListener(
			android.view.View.OnClickListener listener) {
		Button view_input_positive = (Button) this
				.findViewById(R.id.view_input_positive);
		view_input_positive.setOnClickListener(listener);
		return this;
	}

	/**
	 * ��ȡ�����1�е�����
	 * 
	 * @return �����1�е�����
	 */
	public String getEdit1() {
		return view_et_input1.getText().toString().trim();
	}

	/**
	 * ��ȡ�����2�е�����
	 * 
	 * @return �����2�е�����
	 */
	public String getEdit2() {
		return view_et_input2.getText().toString().trim();
	}

	/**
	 * �������2���ò��ɼ�״̬
	 * 
	 * @return ����
	 */
	public CustomInputDialog setEditText2Gone() {
		view_et_input2.setVisibility(View.GONE);
		return this;
	}

	/**
	 * �õ������2��״̬�ǲ��ǲ��ɼ���״̬���Ӷ��ж��Ƿ�Ҫ�ٴ���������
	 * 
	 * @return �����2��״̬�ǲ��ǲ��ɼ���״̬
	 */
	public boolean isEditText2Gone() {
		return view_et_input2.getVisibility() == View.GONE;
	}

	/**
	 * ����һ���������������Ϊ��
	 */
	public void setEdit1Null() {
		view_et_input1.setText("");
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		view_et_input2.setText("");
	}

	@Override
	public void afterTextChanged(Editable s) {

	}
}
