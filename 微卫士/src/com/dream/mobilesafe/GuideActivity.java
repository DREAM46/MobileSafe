package com.dream.mobilesafe;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity implements OnClickListener,
		OnPageChangeListener, OnGlobalLayoutListener {

	private ViewPager pager_welcome;
	private View iv_guide_selected;

	private static final int NUM = 3;
	private LinearLayout linear_hintImg;

	private ArrayList<ImageView> ivs;

	// ����������֮��Ŀ��
	private int basicWidth;

	/**
	 * ѡ�е�ҳ���λ��
	 */
	private int selectedPos = 0;

	private Button btn_guide_toMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		this.init();
	}

	private void init() {

		pager_welcome = (ViewPager) this.findViewById(R.id.pager_welcome);
		iv_guide_selected = this.findViewById(R.id.iv_guide_selected);
		btn_guide_toMain = (Button) this.findViewById(R.id.btn_guide_toMain);

		int[] imgIds = { R.drawable.welcome1, R.drawable.welcome2,
				R.drawable.welcome3 };
		ivs = new ArrayList<ImageView>();
		ImageView iv = null;
		for (int i = 0; i < NUM; i++) {
			iv = new ImageView(this);
			iv.setBackgroundResource(imgIds[i]);
			ivs.add(iv);
		}

		linear_hintImg = (LinearLayout) this.findViewById(R.id.linear_hintImg);

		setHintImgs(0);

		// view��������: measure -> layout -> draw
		// ����mSelectPointView�ؼ�layout

		// �����ͼ���Ĺ۲���, ����ȫ�����ֵĻص�
		linear_hintImg.getViewTreeObserver().addOnGlobalLayoutListener(this);

		MyPageAdapter adapter = new MyPageAdapter();
		pager_welcome.setAdapter(adapter);
		pager_welcome.setOnPageChangeListener(this);
		btn_guide_toMain.setOnClickListener(this);
	}

	@Override
	public void onGlobalLayout() {
		// ִֻ��һ��, �ѵ�ǰ���¼�����ͼ���Ĺ۲������Ƴ���
		linear_hintImg.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		// ȡ��������֮��Ŀ��
		basicWidth = linear_hintImg.getChildAt(1).getLeft()
				- linear_hintImg.getChildAt(0).getLeft();
	}

	/**
	 * ���õ�����
	 * 
	 * @param position
	 *            ������λ��
	 */
	private void setHintImgs(int position) {
		linear_hintImg.removeAllViews();
		ImageView img = null;

		for (int i = 0; i < NUM; i++) {
			img = new ImageView(this);
			LinearLayout.LayoutParams lp = new LayoutParams(10, 10);
			if (i == position)
				img.setBackgroundResource(R.drawable.guide_dot_selected);
			else
				img.setBackgroundResource(R.drawable.guide_dot_normal);
			if (i != 0) {
				lp.leftMargin = 10;
			}
			img.setLayoutParams(lp);
			linear_hintImg.addView(img);
		}
	}

	public class MyPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return NUM;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView iv = ivs.get(position);
			container.addView(iv);
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	@Override
	public void onClick(View v) {
		boolean isStarted = this.getIntent().getBooleanExtra("isStarted", true);
		if (isStarted)
			this.startActivity(new Intent(this, HomeActivity.class));
		this.finish();
	}

	/**
	 * /**
	 * 
	 * @param state
	 *            ������״̬ :0��1��2��arg0
	 *            ==1��ʱ��Ĭʾ���ڻ�����arg0==2��ʱ��Ĭʾ��������ˣ�arg0==0��ʱ��Ĭʾʲô��û����
	 */
	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == 1)
			linear_hintImg.getChildAt(selectedPos).setBackgroundResource(
					R.drawable.guide_dot_normal);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int state) {
		int leftMargin = (int) (basicWidth * (position + positionOffset));
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) iv_guide_selected
				.getLayoutParams();
		lp.leftMargin = leftMargin;
		iv_guide_selected.setLayoutParams(lp);

		if (position == ivs.size() - 1 && positionOffset > 0.3) {
			linear_hintImg.getChildAt(position).setBackgroundResource(
					R.drawable.guide_dot_normal);
		}
	}

	@Override
	public void onPageSelected(int position) {
		this.setSelectedImg(R.drawable.guide_dot_normal);
		selectedPos = position;
		this.setSelectedImg(R.drawable.guide_dot_selected);

		if (position == NUM - 1) {
			btn_guide_toMain.setVisibility(View.VISIBLE);
		} else {
			btn_guide_toMain.setVisibility(View.INVISIBLE);
		}
	}

	private void setSelectedImg(int drawable) {
		linear_hintImg.getChildAt(selectedPos).setBackgroundResource(drawable);
	}

}
