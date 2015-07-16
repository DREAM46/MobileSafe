package com.dream.mobilesafe;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.dream.mobilesafe.db.dao.NumberAddressQueryUtils;

public class NumberAddressQueryActivity extends Activity implements TextWatcher {

	private EditText et_numberaddress_query;
	private TextView tv_result_query;

	private static String path = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_numberadress_query);

		path = this.getFilesDir() + "/address.db";

		tv_result_query = (TextView) this.findViewById(R.id.tv_result_query);

		et_numberaddress_query = (EditText) this
				.findViewById(R.id.et_numberaddress_query);
		et_numberaddress_query.addTextChangedListener(this);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		String number = et_numberaddress_query.getText().toString().trim();

		/**
		 * ��ѯ����������м������: 1������Ϊ3λ���Ĺ̻����룬��λ����Ϊ0�����û����뵽3λ��ʱ���ɲ�ѯ
		 * 2������Ϊ4λ���Ĺ̻����룬��λ����Ϊ0��֮ǰ�����3λ��ʱ���Ѿ������˲�ѯ��
		 * �����жϽ��TextView�е������Ƿ�Ϊ��û�н���������ǣ�����ܣ�ֻ�����ǿ����ǣ���Ϊ�п��ܸ��������ڣ�Ϊ����Ϊ4λ��
		 * �Ĺ̻����룬������֤���Ѿ��鵽�˽���������ٲ�
		 * 3������λΪ0����˵���ú���Ҫô�ǹ̻����룬Ҫô�����ڣ������䵽��5λ����5λ�Ժ��򲻸��ٽ��в�ѯ
		 * 4��11λ���ֻ����룬��λΪ1,���ֻ��������뵽��7λʱ�����в�ѯ�����鵽���
		 * ���û����ֻ�����ǰ7λ���и��ģ��򲻸�����ʾ֮ǰ�Ĳ�ѯ�����Ӧ��ʾ������
		 * 5������λΪ1����˵���ú���Ҫô���ֻ����룬Ҫô�����ڣ������䵽��8λ����8λ�Ժ��򲻸��ٽ��в�ѯ
		 * 6��������Ϊ0�򳤶�С��3���򲻸ý��й����ز�ѯ
		 */

		if (TextUtils.isEmpty(number)) {
			tv_result_query.setText("������:");
			return;
		}

		if (number.charAt(0) != '0' && number.charAt(0) != '1') {
			tv_result_query.setText("������:û�н�����������");
			return;
		}

		if (number.length() < 3) {
			tv_result_query.setText("������:");
			return;
		}

		if (number.startsWith("0")) {
			if (number.length() == 3) {
				new QueryTask().execute(path, number);
			} else if (number.length() == 4
					&& tv_result_query.getText().equals("������:û�н��")) {
				new QueryTask().execute(path, number);
			}
		}

		if (number.startsWith("1")) {
			if (number.length() == 5 || number.length() == 6) {
				tv_result_query.setText("������:");
			} else if (number.startsWith("1") && number.length() == 7)
				new QueryTask().execute(path, number);
		}

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	private class QueryTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {

			if (params[1].startsWith("0"))
				return NumberAddressQueryUtils.queryAddressByGuHuaNumber(
						params[0], params[1]);

			return NumberAddressQueryUtils.queryAddressByMobileNumber(
					params[0], params[1]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			tv_result_query.setText("������:" + result);
		}
	}
}
