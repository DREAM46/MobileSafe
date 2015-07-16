package com.dream.mobilesafe.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.mobilesafe.R;

/**
 * 自定义的设置项的控件类
 * 
 * @author 温坤哲
 * 
 */
public class SettingItem1 extends RelativeLayout {

	private TextView tv_setting_item_title;
	private TextView tv_setting_item_content;

	private static final String attr_Schema = "http://schemas.android.com/apk/res/com.dream.mobilesafe";

	private void initView(Context context) {

		View view = View.inflate(context, R.layout.view_item_setting1, this);

		tv_setting_item_title = (TextView) view
				.findViewById(R.id.tv_setting_item_title);
		tv_setting_item_content = (TextView) view
				.findViewById(R.id.tv_setting_item_content);
	}

	/**
	 * 设置设置项的内容
	 * 
	 * @param titleStr
	 *            字符串Id
	 * @return 自身
	 */
	public SettingItem1 setDesc(int descId) {
		tv_setting_item_content.setText(descId);
		return this;
	}

	/**
	 * 设置设置项的内容
	 * 
	 * @param titleStr
	 *            字符串
	 * @return 自身
	 */
	public SettingItem1 setDesc(String descStr) {
		tv_setting_item_content.setText(descStr);
		return this;
	}

	/**
	 * 设置设置项的标题
	 * 
	 * @param titleId
	 *            字符串Id
	 * @return 自身
	 */
	public SettingItem1 setTitle(int titleId) {
		tv_setting_item_title.setText(titleId);
		return this;
	}

	/**
	 * 设置设置项的标题
	 * 
	 * @param titleStr
	 *            字符串
	 * @return 自身
	 */
	public SettingItem1 setTitle(String titleStr) {
		tv_setting_item_title.setText(titleStr);
		return this;
	}

	public SettingItem1(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView(context);

		Resources resources = this.getResources();

		String titleId = resources.getString(Integer.parseInt(attrs
				.getAttributeValue(attr_Schema, "title").substring(1)));

		this.setTitle(titleId);
	}

	public SettingItem1(Context context) {
		super(context);
		this.initView(context);
	}

	public SettingItem1(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.initView(context);
	}

}
