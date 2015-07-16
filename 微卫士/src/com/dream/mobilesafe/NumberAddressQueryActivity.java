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
		 * 查询号码归属地有几种情况: 1、区号为3位数的固话号码，首位区号为0，当用户输入到3位数时即可查询
		 * 2、区号为4位数的固话号码，首位区号为0，之前输入第3位数时，已经进行了查询，
		 * 所以判断结果TextView中的文字是否为“没有结果”，若是，则可能（只可能是可能是，因为有可能根本不存在）为区号为4位数
		 * 的固话号码，若否，则证明已经查到了结果，无须再查
		 * 3、若首位为0，则说明该号码要么是固话号码，要么不存在，若当输到第5位及第5位以后，则不该再进行查询
		 * 4、11位的手机号码，首位为1,当手机号码输入到第7位时，进行查询，若查到结果
		 * 后用户对手机号码前7位进行更改，则不该再显示之前的查询结果，应显示归属地
		 * 5、若首位为1，则说明该号码要么是手机号码，要么不存在，若当输到第8位及第8位以后，则不该再进行查询
		 * 6、若长度为0或长度小于3，则不该进行归属地查询
		 */

		if (TextUtils.isEmpty(number)) {
			tv_result_query.setText("归属地:");
			return;
		}

		if (number.charAt(0) != '0' && number.charAt(0) != '1') {
			tv_result_query.setText("归属地:没有结果，请检查号码");
			return;
		}

		if (number.length() < 3) {
			tv_result_query.setText("归属地:");
			return;
		}

		if (number.startsWith("0")) {
			if (number.length() == 3) {
				new QueryTask().execute(path, number);
			} else if (number.length() == 4
					&& tv_result_query.getText().equals("归属地:没有结果")) {
				new QueryTask().execute(path, number);
			}
		}

		if (number.startsWith("1")) {
			if (number.length() == 5 || number.length() == 6) {
				tv_result_query.setText("归属地:");
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
			tv_result_query.setText("归属地:" + result);
		}
	}
}
