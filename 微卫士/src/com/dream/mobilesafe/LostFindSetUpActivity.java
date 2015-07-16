package com.dream.mobilesafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.dream.mobilesafe.view.HintImageItem;
import com.dream.mobilesafe.view.SettingItem;
import com.dream.mobilesafe.view.ShowCustomToast;

public class LostFindSetUpActivity extends FragmentActivity implements
		OnPageChangeListener, OnClickListener {

	private TextView tv_setup_title;
	private ViewPager pager_setup;
	private static String[] titles;
	private HintImageItem hint_img;

	private static SharedPreferences sp;
	private static Editor editor;
	private static Resources resources;

	private static Context context;

	private static EditText et_setup3_num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lostfind_setup);

		resources = this.getResources();
		sp = this.getSharedPreferences(resources.getString(R.string.pre_name),
				Context.MODE_PRIVATE);
		editor = sp.edit();

		tv_setup_title = (TextView) this.findViewById(R.id.tv_setup_title);

		titles = this.getResources().getStringArray(R.array.setup_titles);
		tv_setup_title.setText(titles[0]);

		hint_img = (HintImageItem) this.findViewById(R.id.hint_img);
		hint_img.setHintImg(this, titles.length, 0);

		pager_setup = (ViewPager) this.findViewById(R.id.pager_setup);
		pager_setup.setAdapter(new SetUpPagerAdapter(this
				.getSupportFragmentManager()));
		pager_setup.setOnPageChangeListener(this);

	}

	@Override
	public void onClick(View v) {

		String preInfo = et_setup3_num.getText().toString().trim();

		String number = preInfo.trim();

		this.startActivityForResult(new Intent(this,
				SelectContactorActivity.class).putExtra("number", number), 101);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 101 && resultCode == 102) {
			String selectedMsg = data.getStringExtra("selectedMsg");
			et_setup3_num.setText(selectedMsg);
		}

	}

	/**
	 * Pager的适配器
	 * 
	 * @author 温坤哲
	 * 
	 */
	private class SetUpPagerAdapter extends FragmentStatePagerAdapter {

		public SetUpPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			Fragment fragment = null;

			switch (position) {
			case 0:
				fragment = SetUp1Fragment.newInstance();
				break;
			/*
			 * case 1: fragment = SetUp2Fragment.newInstance(); break;
			 */
			case 1:
				fragment = SetUp3Fragment.newInstance();
				break;
			case 2:
				fragment = SetUp4Fragment.newInstance();
				break;
			}

			return fragment;
		}

		@Override
		public int getCount() {
			return titles.length;
		}

	}

	/**
	 * 设置第一步的界面
	 * 
	 * @author 温坤哲
	 * 
	 */
	public static class SetUp1Fragment extends Fragment {

		/**
		 * 获取Setup1Fragment的静态方法
		 * 
		 * @return SetupFragment
		 */
		public static SetUp1Fragment newInstance() {
			SetUp1Fragment fragment = new SetUp1Fragment();
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			context = this.getActivity();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = null;

			view = View.inflate(this.getActivity(),
					R.layout.view_setup_config1, null);

			return view;
		}

	}

	/**
	 * 设置第二步的界面
	 * 
	 * @author 温坤哲
	 * 
	 */

	public static class SetUp2Fragment extends Fragment implements
			OnCheckedChangeListener {

		private static SettingItem si_setup2_sim;
		private CheckBox cb_sim;

		/**
		 * 获取Setup1Fragment的静态方法
		 * 
		 * @return SetupFragment
		 */
		public static SetUp2Fragment newInstance() {

			SetUp2Fragment fragment = new SetUp2Fragment();
			return fragment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View view = View
					.inflate(context, R.layout.view_setup_config2, null);
			si_setup2_sim = (SettingItem) view.findViewById(R.id.si_setup2_sim);
			cb_sim = (CheckBox) view.findViewById(R.id.cb_sim);
			boolean isBounded = !TextUtils.isEmpty(sp.getString(
					resources.getString(R.string.pre_boundNum), ""));
			si_setup2_sim.setChecked(isBounded);
			cb_sim.setChecked(isBounded);
			cb_sim.setOnCheckedChangeListener(this);
			return view;
		}

		/**
		 * 获取当前设备的SIM卡的序列号
		 * 
		 * @return 当前设备的SIM卡的序列号
		 */
		private String getSerialNum() {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getSimSerialNumber();
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			si_setup2_sim.setChecked(isChecked);
			System.out.println("sim:" + isChecked);
			if (isChecked) {
				editor.putString(resources.getString(R.string.pre_boundNum),
						null);
			} else {
				editor.putString(resources.getString(R.string.pre_boundNum),
						this.getSerialNum());
			}
			editor.commit();
		}

	}

	/**
	 * 设置第三步的界面
	 * 
	 * @author 温坤哲
	 * 
	 */

	public static class SetUp3Fragment extends Fragment {

		private static Button btn_setup3_selectContactor;

		/**
		 * 获取Setup3Fragment的静态方法
		 * 
		 * @return SetupFragment
		 */
		public static SetUp3Fragment newInstance() {

			SetUp3Fragment fragment = new SetUp3Fragment();
			return fragment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = View
					.inflate(context, R.layout.view_setup_config3, null);
			et_setup3_num = (EditText) view.findViewById(R.id.et_setup3_num);
			et_setup3_num.setText(sp.getString("alarmNum", ""));

			btn_setup3_selectContactor = (Button) view
					.findViewById(R.id.btn_setup3_selectContactor);
			btn_setup3_selectContactor
					.setOnClickListener((OnClickListener) context);
			return view;
		}

	}

	/**
	 * 设置第四步的界面
	 * 
	 * @author 温坤哲
	 * 
	 */
	public static class SetUp4Fragment extends Fragment implements
			OnClickListener, OnCheckedChangeListener {

		private static Button btn_setup4_finish;

		private static CheckBox check_setup4_state;

		private static TextView tv_setup4_state;

		/**
		 * 获取Setup1Fragment的静态方法
		 * 
		 * @return SetupFragment
		 */
		public static SetUp4Fragment newInstance() {

			SetUp4Fragment fragment = new SetUp4Fragment();
			return fragment;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View view = View
					.inflate(context, R.layout.view_setup_config4, null);
			btn_setup4_finish = (Button) view
					.findViewById(R.id.btn_setup4_finish);

			check_setup4_state = (CheckBox) view
					.findViewById(R.id.check_setup4_state);

			tv_setup4_state = (TextView) view
					.findViewById(R.id.tv_setup4_state);

			check_setup4_state.setChecked(sp.getBoolean(
					resources.getString(R.string.pre_isSetUp), false));
			check_setup4_state.setOnCheckedChangeListener(this);

			btn_setup4_finish.setOnClickListener(this);
			return view;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				tv_setup4_state.setText("您已经开启防盗保护");
			} else {
				tv_setup4_state.setText("您没有开启防盗保护");
			}
		}

		@Override
		public void onClick(View v) {
			if (check_setup4_state.isChecked()) {
				editor.putBoolean(resources.getString(R.string.pre_isSetUp),
						check_setup4_state.isChecked());
				editor.putBoolean(resources.getString(R.string.pre_isConfiged),
						true);
				editor.commit();
				context.startActivity(new Intent(context,
						LostFindActivity.class));
				this.getActivity().finish();
			} else {
				ShowCustomToast.show(getActivity(), "您没有开启防盗保护");
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int pos) {

		// if (pos == 2) { if (TextUtils.isEmpty(sp.getString(
		// resources.getString(R.string.pre_boundNum), ""))) {
		// pager_setup.setCurrentItem(1); ShowCustomToast.show(this,
		// "您还没绑定SIM卡"); return; } }

		if (pos == 2) {
			String num = et_setup3_num.getText().toString().trim();
			if (!TextUtils.isEmpty(num)) {
				editor.putString("alarmNum", num);
				editor.commit();
			} else {
				pager_setup.setCurrentItem(1);
				ShowCustomToast.show(this, "请输入正确的手机号码");
				return;
			}
		}

		tv_setup_title.setText(titles[pos]);
		hint_img.setHintImg(this, titles.length, pos);
	}

}
