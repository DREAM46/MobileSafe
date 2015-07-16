package com.dream.mobilesafe.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dream.mobilesafe.R;
import com.dream.mobilesafe.utils.SlidingBtnDensityUtil;

/**
 * 自定义滑动按钮
 * 
 * @author 温坤哲
 * 
 */
public class SlidingToggleButton extends RelativeLayout implements
		OnClickListener, AnimationListener {

	private ImageButton ib_slding;
	private ImageView iv_bg;

	private boolean isChecked;

	private OnSlidingBtnClickListener onClickListener;

	private int sildingWidth = 0;

	private Drawable iv_bg_on;
	private Drawable iv_bg_off;

	/**
	 * 单击滑动按钮的事件的响应接口
	 * 
	 * @author 温坤哲
	 * 
	 */
	public interface OnSlidingBtnClickListener {
		/**
		 * 当被滑动按钮被单击时执行的方法
		 * 
		 * @param stb
		 *            被单击的View本身
		 */
		void onSlidingBtnClick(SlidingToggleButton stb);
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;

	}

	/**
	 * 返回滑动按钮的选中状态
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return isChecked;
	}

	public void setOnSlidingBtnClickListener(
			OnSlidingBtnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public SlidingToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initView(context, attrs);
		this.setOnClickListener(this);
	}

	private void initView(Context context, AttributeSet attrs) {
		View view = View.inflate(getContext(), R.layout.view_toggle, this);

		iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
		ib_slding = (ImageButton) view.findViewById(R.id.ib_slding);
		ib_slding.setClickable(false);
		// AttributeSet 对XML文件解析后的结果，封装为 AttributeSet 对象。 存储的都是原始数据。仅对数据进行简单加工。
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.SlidingSettinItem);
		for (int i = 0; i < ta.getIndexCount(); i++) {
			int index = ta.getIndex(i);
			switch (index) {
			case R.styleable.SlidingSettinItem_toggle_bg_on:
				iv_bg_on = ta.getDrawable(index);
				break;
			case R.styleable.SlidingSettinItem_toggle_bg_off:
				iv_bg_off = ta.getDrawable(index);
				break;
			case R.styleable.SlidingSettinItem_isCheck:
				isChecked = ta.getBoolean(index, false);
				break;
			case R.styleable.SlidingSettinItem_sliding_drawable:
				ib_slding.setImageDrawable(ta.getDrawable(index));
				break;
			}
		}
		System.out.println("default switch:" + isChecked);
	}

	/**
	 * 初始化滑动按钮的选中状态
	 * 
	 * @param isChecked1
	 *            滑动按钮的选中状态，即选中与否
	 */
	public void initChecked(boolean isChecked1) {
		// 根据初始状态是否是选中选择不同的背景
		this.isChecked = isChecked1;
		if (isChecked)
			iv_bg.setImageDrawable(iv_bg_on);
		else
			iv_bg.setImageDrawable(iv_bg_off);

		if (!isChecked) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, 0, 0);
			ib_slding.setLayoutParams(lp);
		}
	}

	@Override
	public void onClick(View v) {
		// 将成员变量isChecked置为其相反值
		isChecked = !isChecked;
		TranslateAnimation ta = null;
		// 测量sildingWidth的宽度
		sildingWidth = iv_bg.getWidth() - ib_slding.getWidth();
		if (isChecked) {
			ta = new TranslateAnimation(0, sildingWidth, 0, 0);
			System.out.println("to true");
		} else {
			sildingWidth = iv_bg.getWidth() - ib_slding.getWidth();
			ta = new TranslateAnimation(0, -sildingWidth, 0, 0);
			System.out.println("to false");
		}
		ta.setDuration(500);
		ta.setAnimationListener(this);
		ib_slding.startAnimation(ta);
		Log.d("M", isChecked + "click");
		// onClickCallBack.click();
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		animation.setFillAfter(true);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		if (isChecked) {
			lp.setMargins(
					SlidingBtnDensityUtil.dip2px(getContext(), sildingWidth),
					0, 0, 0);
			iv_bg.setImageDrawable(iv_bg_on);
		} else {
			lp.setMargins(0, 0, 0, 0);
			iv_bg.setImageDrawable(iv_bg_off);
		}
		ib_slding.clearAnimation();
		ib_slding.setLayoutParams(lp);
		if (onClickListener != null)
			onClickListener.onSlidingBtnClick(this);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

}
