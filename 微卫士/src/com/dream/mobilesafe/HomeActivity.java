package com.dream.mobilesafe;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dream.mobilesafe.utils.ContactorObserver;
import com.dream.mobilesafe.utils.MD5Utils;
import com.dream.mobilesafe.view.CustomInputDialog;
import com.dream.mobilesafe.view.MenuView;
import com.dream.mobilesafe.view.ShowCustomToast;
import com.slidingmenu.lib.SlidingMenu;

public class HomeActivity extends Activity implements OnItemClickListener,
		OnClickListener {

	private GridView gv_home;
	private HomeAdapter adapter;
	/**
	 * 主界面各功能项的文字说明
	 */
	private String[] strs_functions;
	/**
	 * 主界面各功能项的图片
	 */
	private int[] imgs_functions = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, /* R.drawable.sysoptimize, */
			R.drawable.atools, R.drawable.settings };

	private static final int POS_SAFE = 0;
	private static final int POS_CALLMSGSAFE = 1;
	private static final int POS_APP = 2;
	private static final int POS_TASKMANAGER = 3;
	// private static final int POS_TROJAN = 4;
	// private static final int POS_SYSOTIMIZE = 5;
	private static final int POS_ATTOLS = 4;
	private static final int POS_SETTING = 5;

	private SharedPreferences sp;

	private CustomInputDialog dialog;

	private SlidingMenu sm;

	private ImageButton img_logo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		gv_home = (GridView) this.findViewById(R.id.gv_home);
		adapter = new HomeAdapter();

		Resources resources = this.getResources();
		strs_functions = resources.getStringArray(R.array.strs_functions);

		gv_home.setAdapter(adapter);

		img_logo = (ImageButton) this.findViewById(R.id.img_logo);
		img_logo.setOnClickListener(this);
		gv_home.setOnItemClickListener(this);

		sp = this.getSharedPreferences(resources.getString(R.string.pre_name),
				Context.MODE_PRIVATE);

		this.getContentResolver().registerContentObserver(
				Uri.parse("content://com.android.contacts/raw_contacts"), true,
				new ContactorObserver(new Handler(), this));
		this.initSlidingMenu();
	}

	private void initSlidingMenu() {
		sm = new SlidingMenu(this);
		// 设置左滑菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动的屏幕范围，该设置为全屏区域都可以滑动
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// 设置阴影图片
		sm.setShadowDrawable(R.drawable.shadow);
		// 设置阴影图片的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// SlidingMenu划出时主页面显示的剩余宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置SlidingMenu菜单的宽度
		// menu.setBehindWidth(400);
		// SlidingMenu滑动时的渐变程度
		sm.setFadeDegree(0.35f);
		// 使SlidingMenu附加在Activity上
		sm.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

		MenuView mv = new MenuView(this);
		// 设置menu的布局文件
		sm.setMenu(mv);
	}

	public SlidingMenu getMenu() {
		return sm;
	}

	private class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return strs_functions.length;
		}

		@Override
		public Object getItem(int position) {
			return strs_functions[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if (convertView == null) {
				view = View.inflate(HomeActivity.this, R.layout.view_item_home,
						null);
			} else {
				view = convertView;
			}

			ImageView iv_home_item = (ImageView) view
					.findViewById(R.id.iv_home_item);
			iv_home_item.setImageResource(imgs_functions[position]);
			TextView tv_home_item = (TextView) view
					.findViewById(R.id.tv_home_item);
			tv_home_item.setText(strs_functions[position]);

			return view;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case POS_SAFE:
			if (this.isSetUpPwd()) {
				dialog = new CustomInputDialog(this).setEditText2Gone();
			} else {
				dialog = new CustomInputDialog(this);
			}
			dialog.setOnPositiveListener(this).show();
			break;
		case POS_CALLMSGSAFE:
			this.startActivity(new Intent(this, CallSmsSafeActivity.class));
			break;
		case POS_APP:
			this.startActivity(new Intent(this, AppManagerActivity.class));
			break;
		case POS_TASKMANAGER:
			this.startActivity(new Intent(this, TaskManagerActivity.class));
			break;
		/*
		 * case POS_SYSOTIMIZE: this.startActivity(new Intent(this,
		 * CleanCacheActivity.class)); break;
		 */
		case POS_ATTOLS:
			this.startActivity(new Intent(this, AUtilsActivity.class));
			break;

		case POS_SETTING:
			this.startActivity(new Intent(this, SettingActivity.class));
			break;

		}
	}

	/**
	 * 判断是否在“手机防盗”功能中设置了密码
	 * 
	 * @return “手机防盗”功能中 是否设置了密码
	 */
	private boolean isSetUpPwd() {
		return !TextUtils.isEmpty(sp.getString("passwd", null));
	}

	@Override
	public void onClick(View v) {

		if (v == img_logo) {
			this.startActivity(new Intent(this, AntiVirusActivity.class));
			return;
		}

		if (dialog.isEditText2Gone()) {

			String inputPasswd = dialog.getEdit1();

			if (TextUtils.isEmpty(inputPasswd)) {
				ShowCustomToast.show(this, "请输入密码");
				return;
			}

			// 取出加密后的密码
			String savePasswd = sp.getString("passwd", "");

			if (savePasswd.equals(MD5Utils.getMD5Passwd(inputPasswd))) {

				if (sp.getBoolean(
						this.getResources().getString(R.string.pre_isConfiged),
						false)) {
					HomeActivity.this.startActivity(new Intent(
							HomeActivity.this, LostFindActivity.class));
				} else {
					HomeActivity.this.startActivity(new Intent(
							HomeActivity.this, LostFindSetUpActivity.class));
				}

				dialog.dismiss();
			} else {
				ShowCustomToast.show(this, "密码错误，请重新输入");
				dialog.setEdit1Null();
			}

		} else {
			if (this.setPasswd()) {
				HomeActivity.this.startActivity(new Intent(HomeActivity.this,
						LostFindSetUpActivity.class));
				dialog.dismiss();
			}

		}
	}

	/**
	 * 设置手机防盗功能的密码
	 */
	private boolean setPasswd() {
		String passwd = dialog.getEdit1().trim();
		String passwd_confim = dialog.getEdit2().trim();

		// 判断密码是否为空
		if (TextUtils.isEmpty(passwd) || TextUtils.isEmpty(passwd_confim)) {
			ShowCustomToast.show(this, "密码不能为空");
			return false;
		}
		// 判断密码是否一致
		if (!passwd.equals(passwd_confim)) {
			ShowCustomToast.show(this, "两次输入的密码不一致");
			return false;
		}

		Editor editor = sp.edit();
		editor.putString("passwd", MD5Utils.getMD5Passwd(passwd));
		editor.commit();
		ShowCustomToast.show(this, "密码已重置");
		return true;
	}

	/**
	 * 准备退出的标志
	 */
	private boolean isExit = false;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (sm.isMenuShowing()) {
				sm.toggle();
				return false;
			}

			if (!isExit) {
				isExit = true;// 准备退出的标志

				ShowCustomToast.show(this, R.string.exithint);

				/*
				 * 设置会自动执行的TimerTask， 如果在2秒钟之内不再一次按下返回键，即将isExit设为false，即取消掉准备退出
				 * 如果在2秒钟之内再一次按下返回键，此时isExit仍为true，此时执行else中的方法
				 */
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						// 取消准备退出的标志
						isExit = false;
					}

				}, 2000);

			} else {
				Process.killProcess(Process.myPid());
			}
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (sm.isMenuShowing())
				sm.toggle();
			else
				sm.showMenu();
		}
		return true;
	}

}
