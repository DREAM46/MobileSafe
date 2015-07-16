package com.dream.mobilesafe.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dream.mobilesafe.R;

public class CustomRadioDialog extends Dialog {

	private ListView lv_radio;

	public CustomRadioDialog(Context context) {
		super(context, R.style.dialo_style);
		setContentView(R.layout.view_dialog_radio);

		TextView view_dialog_title = (TextView) this
				.findViewById(R.id.view_dialog_title);
		view_dialog_title.setText("归属地显示风格");

		lv_radio = (ListView) this.findViewById(R.id.lv_radio);
	}

	/**
	 * 设置对话框中的ListView的适配器
	 * 
	 * @param adapter
	 *            对话框中的ListView的适配器
	 * @return 对话框本身
	 */
	public CustomRadioDialog setAdapter(ListAdapter adapter) {
		lv_radio.setAdapter(adapter);
		return this;
	}

	/**
	 * 设置对话框中的ListView的OnItemClickListener
	 * 
	 * @param listener
	 *            对话框中的ListView的OnItemClickListener
	 * @return 对话框本身
	 */
	public CustomRadioDialog setOnItemClickListener(OnItemClickListener listener) {
		lv_radio.setOnItemClickListener(listener);
		return this;
	}

}
