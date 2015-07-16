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

		((TextView) this.findViewById(R.id.tv_input_title)).setText("输入密码");

		view_et_input1.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}

	/**
	 * 为确定按钮绑定监听器
	 * 
	 * @param listener
	 *            确定按钮绑定监听器
	 * @return 对话框本身
	 */
	public CustomInputDialog setOnPositiveListener(
			android.view.View.OnClickListener listener) {
		Button view_input_positive = (Button) this
				.findViewById(R.id.view_input_positive);
		view_input_positive.setOnClickListener(listener);
		return this;
	}

	/**
	 * 获取输入框1中的内容
	 * 
	 * @return 输入框1中的内容
	 */
	public String getEdit1() {
		return view_et_input1.getText().toString().trim();
	}

	/**
	 * 获取输入框2中的内容
	 * 
	 * @return 输入框2中的内容
	 */
	public String getEdit2() {
		return view_et_input2.getText().toString().trim();
	}

	/**
	 * 将输入框2设置不可见状态
	 * 
	 * @return 自身
	 */
	public CustomInputDialog setEditText2Gone() {
		view_et_input2.setVisibility(View.GONE);
		return this;
	}

	/**
	 * 得到输入框2的状态是不是不可见的状态，从而判断是否要再次输入密码
	 * 
	 * @return 输入框2的状态是不是不可见的状态
	 */
	public boolean isEditText2Gone() {
		return view_et_input2.getVisibility() == View.GONE;
	}

	/**
	 * 将第一个输入框内容设置为空
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
