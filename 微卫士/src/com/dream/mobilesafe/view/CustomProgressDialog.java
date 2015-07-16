package com.dream.mobilesafe.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.dream.mobilesafe.R;

/**
 * 自定义对话框
 * 
 * @author 温坤哲
 * 
 */
public class CustomProgressDialog extends Dialog {


	private ImageView imageView;

	public CustomProgressDialog(Context context) {
		super(context, R.style.CustomProgressDialog);
		this.setContentView(R.layout.view_dialog_progress);
		this.getWindow().getAttributes().gravity = Gravity.CENTER;

	}

	public void onWindowFocusChanged(boolean hasFocus) {
		imageView = (ImageView) this.findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
	}

	/**
	 * 
	 * setTitile 标题
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public CustomProgressDialog setTitile(String strTitle) {

		return this;
	}

	/**
	 * 
	 * setMessage 提示内容
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public CustomProgressDialog setMsg(String strMessage) {
		TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		} else {
			System.out.println("tvMsg is null");
		}

		return this;
	}
}
