package com.dream.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.mobilesafe.R;

/**
 * FileLostActivity���е�������
 * 
 * @author ������
 * 
 */
public class FindLostItem extends RelativeLayout {

	private static final String attr_Schema = "http://schemas.android.com/apk/res/com.dream.mobilesafe";

	private TextView tv_findlost_content;

	public FindLostItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		View view = View.inflate(context, R.layout.view_item_findlost, this);
		String titleId = this.getResources().getString(
				Integer.parseInt(attrs.getAttributeValue(attr_Schema, "title")
						.substring(1)));
		((TextView) view.findViewById(R.id.tv_findlost_title)).setText(titleId);

		tv_findlost_content = (TextView) view
				.findViewById(R.id.tv_findlost_content);
	}

	/**
	 * ���ÿռ�������TextView������
	 * 
	 * @param text
	 */
	public void setContent(String text) {
		tv_findlost_content.setText(text);
	}

	/**
	 * ���ÿռ�������TextView�ı���
	 * 
	 * @param drawableId
	 */
	public void setTextBackground(int drawableId) {
		tv_findlost_content.setBackgroundResource(drawableId);
	}

}
