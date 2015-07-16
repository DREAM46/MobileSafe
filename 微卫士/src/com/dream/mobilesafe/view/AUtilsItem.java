package com.dream.mobilesafe.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.mobilesafe.R;
import com.dream.mobilesafe.utils.ViewMsgs;

/**
 * AUtilsActivity类中的子项类
 * 
 * @author 温坤哲
 * 
 */
public class AUtilsItem extends RelativeLayout {

	private TextView tv_autils_item;

	public AUtilsItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		View view = View.inflate(context, R.layout.view_item_autils, this);
		tv_autils_item = (TextView) view.findViewById(R.id.tv_autils_item);

		Resources resources = this.getResources();
		String title = resources.getString(Integer.parseInt(attrs
				.getAttributeValue(ViewMsgs.ATTRS_SCHEMAS, "title")
				.substring(1)));
		tv_autils_item.setText(title);

		Drawable leftDrawable = resources.getDrawable(Integer.parseInt(attrs
				.getAttributeValue(ViewMsgs.ATTRS_SCHEMAS, "drawableLeft")
				.substring(1)));
		leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
				leftDrawable.getMinimumHeight());
		tv_autils_item.setCompoundDrawables(leftDrawable, null, null, null);
	}

}
