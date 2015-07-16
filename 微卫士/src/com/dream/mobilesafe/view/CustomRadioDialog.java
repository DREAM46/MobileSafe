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
		view_dialog_title.setText("��������ʾ���");

		lv_radio = (ListView) this.findViewById(R.id.lv_radio);
	}

	/**
	 * ���öԻ����е�ListView��������
	 * 
	 * @param adapter
	 *            �Ի����е�ListView��������
	 * @return �Ի�����
	 */
	public CustomRadioDialog setAdapter(ListAdapter adapter) {
		lv_radio.setAdapter(adapter);
		return this;
	}

	/**
	 * ���öԻ����е�ListView��OnItemClickListener
	 * 
	 * @param listener
	 *            �Ի����е�ListView��OnItemClickListener
	 * @return �Ի�����
	 */
	public CustomRadioDialog setOnItemClickListener(OnItemClickListener listener) {
		lv_radio.setOnItemClickListener(listener);
		return this;
	}

}
