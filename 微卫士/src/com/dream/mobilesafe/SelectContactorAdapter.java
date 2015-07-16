package com.dream.mobilesafe;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dream.mobilesafe.domin.ContactInfo;

public class SelectContactorAdapter extends BaseAdapter {

	private Context context;
	private List<HashMap<String, Object>> data;

	public SelectContactorAdapter(Context context,
			List<HashMap<String, Object>> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position).get("item");
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = null;

		if (convertView == null) {
			view = View.inflate(context, R.layout.view_listcell_contact, null);
		} else {
			view = convertView;
		}

		TextView tv_contactName = (TextView) view
				.findViewById(R.id.tv_contactName);
		TextView tv_contactNum = (TextView) view
				.findViewById(R.id.tv_contactNum);
		ImageView img_isChecked = (ImageView) view
				.findViewById(R.id.img_isChecked);

		HashMap<String, Object> map = data.get(position);
		ContactInfo info = (ContactInfo) map.get("item");
		boolean isChecked = (Boolean) map.get("isChecked");

		tv_contactName.setText(info.getName());
		tv_contactNum.setText(info.getNumber());

		if (isChecked) {
			img_isChecked.setImageResource(R.drawable.checkbox_normal);
		} else {
			img_isChecked.setImageResource(R.drawable.checkbox_null);
		}

		return view;
	}
}
