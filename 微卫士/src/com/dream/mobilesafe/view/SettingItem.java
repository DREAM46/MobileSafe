package com.dream.mobilesafe.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.mobilesafe.R;

/**
 * 自定义的设置项的控件类
 * 
 * @author 温坤哲
 * 
 */
public class SettingItem extends RelativeLayout {

	private TextView tv_setting_item_title;
	private TextView tv_setting_item_content;

	private String desc_on;
	private String desc_off;
	private boolean isChecked;

	private static final String attr_Schema = "http://schemas.android.com/apk/res/com.dream.mobilesafe";

	private void initView(Context context) {

		View view = View.inflate(context, R.layout.view_item_setting, this);

		tv_setting_item_title = (TextView) view
				.findViewById(R.id.tv_setting_item_title);
		tv_setting_item_content = (TextView) view
				.findViewById(R.id.tv_setting_item_content);
		this.setClickable(false);
	}

	/**
	 * 得到设置项中的滑动开关的选中状态
	 * 
	 * @return 设置项中的滑动开关的选中状态
	 */
	public boolean isChecked() {
		return isChecked;
	}

	/**
	 * 设置设置项中的CheckBox的选中状态
	 * 
	 * @param checked
	 */
	public void setChecked(boolean checked) {
		this.isChecked = checked;
		if (checked)
			this.setDesc(desc_on);
		else
			this.setDesc(desc_off);
		
	}

	/**
	 * 设置设置项的内容
	 * 
	 * @param titleStr
	 *            字符串Id
	 * @return 自身
	 */
	public SettingItem setDesc(int descId) {
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
	public SettingItem setDesc(String descStr) {
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
	public SettingItem setTitle(int titleId) {
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
	public SettingItem setTitle(String titleStr) {
		tv_setting_item_title.setText(titleStr);
		return this;
	}

	public SettingItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView(context);

		Resources resources = this.getResources();

		String titleId = resources.getString(Integer.parseInt(attrs
				.getAttributeValue(attr_Schema, "title1").substring(1)));

		desc_on = resources.getString(Integer.parseInt(attrs.getAttributeValue(
				attr_Schema, "desc_on").substring(1)));

		desc_off = resources.getString(Integer.parseInt(attrs
				.getAttributeValue(attr_Schema, "desc_off").substring(1)));

		this.setTitle(titleId);
		this.setDesc(desc_off);
		this.setChecked(isChecked);
	}

	public SettingItem(Context context) {
		super(context);
		this.initView(context);
	}

	public SettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.initView(context);
	}

}
