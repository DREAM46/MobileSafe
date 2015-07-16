package com.dream.mobilesafe;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dream.mobilesafe.domin.ContactInfo;
import com.dream.mobilesafe.utils.ContactUtils;
import com.dream.mobilesafe.utils.ContactorObserver;
import com.dream.mobilesafe.view.CustomProgressDialog;

public class SelectContactorActivity extends Activity implements
		OnItemClickListener {

	private ListView lv_select_contactor;

	private CustomProgressDialog dialog;

	private static List<HashMap<String, Object>> data;

	private static SelectContactorAdapter adapter;

	private static int selected = -1;

	private static List<ContactInfo> infos;

	private String preNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contactor);

		preNum = this.getIntent().getStringExtra("number");

		lv_select_contactor = (ListView) this
				.findViewById(R.id.lv_select_contactor);

		new QueryContactorTask().execute();
		lv_select_contactor.setOnItemClickListener(this);
	}

	private class QueryContactorTask extends
			AsyncTask<Void, Void, List<ContactInfo>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new CustomProgressDialog(SelectContactorActivity.this);
			dialog.setMsg("正在查询，请稍后");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected List<ContactInfo> doInBackground(Void... params) {

			if (infos == null || ContactorObserver.isChange) {

				infos = ContactUtils
						.getContactInfo(SelectContactorActivity.this);
				sort(infos);
			}

			if (!TextUtils.isEmpty(preNum)) {
				String name = ContactUtils.getContactNameByPhoneNumber(
						SelectContactorActivity.this, preNum);
				System.out.println("name:" + name);
				ContactInfo info = new ContactInfo(name, preNum);
				selected = infos.indexOf(info);
			}
			System.out.println("selected:" + selected);
			data = new ArrayList<HashMap<String, Object>>();

			for (int i = 0; i < infos.size(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("item", infos.get(i));
				map.put("isChecked", false);
				data.add(map);
			}

			if (selected != -1) {
				data.get(selected).put("isChecked", true);
			}

			ContactorObserver.isChange = false;
			adapter = new SelectContactorAdapter(SelectContactorActivity.this,
					data);

			return null;
		}

		@Override
		protected void onPostExecute(List<ContactInfo> result) {
			super.onPostExecute(result);

			dialog.dismiss();

			lv_select_contactor.setAdapter(adapter);
		}

	}

	/**
	 * 根据姓名为通讯录姓名排序
	 * 
	 * @param infos
	 */
	private void sort(List<ContactInfo> infos) {
		String[] names = new String[infos.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = infos.get(i).getName();
		}

		Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
		Arrays.sort(names, cmp);

		final List list = Arrays.asList(names);

		Collections.sort(infos, new Comparator<ContactInfo>() {

			@Override
			public int compare(ContactInfo o1, ContactInfo o2) {

				int i1 = list.indexOf(o1.getName());
				int i2 = list.indexOf(o2.getName());
				return i1 > i2 ? 1 : -1;
			}

		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (selected != -1) {
			HashMap<String, Object> map = data.get(selected);
			map.put("isChecked", false);
		}

		HashMap<String, Object> map = data.get(position);
		map.put("isChecked", true);
		selected = position;

		adapter.notifyDataSetChanged();

		ContactInfo info = (ContactInfo) data.get(position).get("item");
		Intent intent = new Intent();
		intent.putExtra("selectedMsg", info.getNumber());
		this.setResult(102, intent);
		this.finish();

	}
}
