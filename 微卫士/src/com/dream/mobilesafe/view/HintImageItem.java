package com.dream.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dream.mobilesafe.R;

public class HintImageItem extends RelativeLayout {

	private LinearLayout layout;

	public HintImageItem(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);
	}

	private void init(Context context) {
		View view = View.inflate(context, R.layout.view_hintimage, this);

		layout = (LinearLayout) view.findViewById(R.id.linear_setup_hint);

	}

	/**
	 * ���ø������������
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param count
	 *            ��������
	 * @param pos
	 *            ������λ��
	 */
	public void setHintImg(Context context, int count, int pos) {
		layout.removeAllViews();
		for (int i = 0; i < count; i++) {
			ImageView imageView = new ImageView(context);
			if (i == pos) {
				imageView.setImageResource(android.R.drawable.presence_online);
			} else {
				imageView
						.setImageResource(android.R.drawable.presence_invisible);
			}
			layout.addView(imageView);
		}
	}

}
