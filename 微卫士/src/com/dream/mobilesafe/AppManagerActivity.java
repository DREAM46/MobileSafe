package com.dream.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.dream.mobilesafe.db.dao.AppLockDAO;
import com.dream.mobilesafe.domin.AppInfo;
import com.dream.mobilesafe.engine.AppInfoProvider;
import com.dream.mobilesafe.view.CustomProgressDialog;
import com.dream.mobilesafe.view.ShowCustomToast;

public class AppManagerActivity extends Activity implements OnScrollListener,
		OnMenuItemClickListener {

	private AppLockDAO dao;

	private SwipeMenuListView lv_app;
	private TextView tv_app_rom;
	private TextView tv_app_sd;
	private TextView tv_app_lable;

	private List<AppInfo> systemAppInfos;
	private List<AppInfo> userAppInfos;

	/**
	 * 所有的应用程序包信息
	 */
	private List<AppInfo> appInfos;

	private AppInfoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);

		dao = new AppLockDAO(this);

		tv_app_rom = (TextView) this.findViewById(R.id.tv_app_rom);
		tv_app_sd = (TextView) this.findViewById(R.id.tv_app_sd);
		tv_app_lable = (TextView) this.findViewById(R.id.tv_app_lable);

		lv_app = (SwipeMenuListView) this.findViewById(R.id.lv_app);
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				switch (menu.getViewType()) {
				case 0:
				case 1:
				case 3:
					return;
				case 2:
					addSwipeMenu(menu, R.drawable.ic_delete_btn,
							new ColorDrawable(Color.rgb(0xE5, 0x18, 0x5E)));
					addSwipeMenu(menu, R.drawable.open,
							new ColorDrawable(Color.rgb(0xE5, 0xE0, 0x3F)));
					addSwipeMenu(menu, R.drawable.share, new ColorDrawable(
							Color.rgb(0xF9, 0x3F, 0x25)));
					break;
				}

			}
		};
		lv_app.setMenuCreator(creator);

		// step 2. listener item click event
		lv_app.setOnMenuItemClickListener(this);
	}

	private void addSwipeMenu(SwipeMenu menu, int drawableId,
			ColorDrawable bgDrawable) {
		SwipeMenuItem item = new SwipeMenuItem(getApplicationContext());
		item.setBackground(bgDrawable);
		item.setWidth(dp2px(90));
		item.setIcon(drawableId);
		menu.addMenuItem(item);
	}

	@Override
	public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
		if (position == 0 || position == userAppInfos.size() + 1)
			return false;

		if (position <= userAppInfos.size()) {
			info = userAppInfos.get(position - 1);
		} else {
			info = systemAppInfos.get(position - 1 - userAppInfos.size() - 1);
		}
		switch (index) {
		case 0:
			uninstallApp();
			break;
		case 1:
			startApp();
			break;
		case 2:
			shareApp();
			break;
		}
		return false;
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	boolean isSysNow = false;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (firstVisibleItem > userAppInfos.size())
			tv_app_lable.setText("系统程序(" + systemAppInfos.size() + ")");
		else
			tv_app_lable.setText("用户程序(" + userAppInfos.size() + ")");
	}

	@Override
	protected void onStart() {
		super.onStart();
		tv_app_rom.setText("内存可用:"
				+ Formatter.formatFileSize(this, this
						.getAvilableSpace(Environment.getDataDirectory()
								.getAbsolutePath())));
		tv_app_sd.setText("SD卡可用:"
				+ Formatter.formatFileSize(this, this
						.getAvilableSpace(Environment
								.getExternalStorageDirectory()
								.getAbsolutePath())));
		new LoadAppTask().execute();
	}

	/**
	 * 获取可用空间
	 * 
	 * @param path
	 *            可用空间的路径，sd或内存
	 * @return 可用空间的大小
	 */
	private long getAvilableSpace(String path) {
		StatFs fs = new StatFs(path);
		long count = fs.getAvailableBlocks();
		long size = fs.getBlockSize();
		return count * size;
	}

	private class LoadAppTask extends AsyncTask<Void, Integer, Void> {

		CustomProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new CustomProgressDialog(AppManagerActivity.this);
			dialog.setMsg("正在加载，请稍后...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			appInfos = AppInfoProvider.getAllAppInfos(AppManagerActivity.this);
			userAppInfos = new ArrayList<AppInfo>();
			systemAppInfos = new ArrayList<AppInfo>();
			for (AppInfo info : appInfos) {
				if (info.isUserApp()) {
					userAppInfos.add(info);
				} else {
					systemAppInfos.add(info);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			dialog.dismiss();
			if (adapter == null) {
				adapter = new AppInfoAdapter();
				lv_app.setAdapter(adapter);

			} else {
				adapter.notifyDataSetChanged();
			}
			tv_app_lable.setVisibility(View.VISIBLE);
			tv_app_lable.setText("用户程序(" + userAppInfos.size() + ")");
			lv_app.setOnScrollListener((OnScrollListener) AppManagerActivity.this);
		}

	}

	private class AppInfoAdapter extends BaseAdapter {

		public class ViewHolder {
			ImageView iv_app;
			TextView tv_app_name;
			TextView tv_app_location;
			ImageView iv_lockState;
		}

		ViewHolder holder = null;

		@Override
		public int getCount() {
			return systemAppInfos.size() + 1 + userAppInfos.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getViewTypeCount() {
			// menu type count
			return 4;
		}

		@Override
		public int getItemViewType(int position) {
			if (position == 0)
				return 0;
			if (position == userAppInfos.size() + 1)
				return 1;
			if (position <= userAppInfos.size())
				return 2;
			return 3;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if (position == 0 || position == (userAppInfos.size() + 1)) {
				TextView tv = new TextView(AppManagerActivity.this);
				if (position == 0)
					tv.setText("用户程序(" + userAppInfos.size() + ")");
				else
					tv.setText("系统程序(" + systemAppInfos.size() + ")");

				tv.setBackgroundResource(R.color.grey);
				tv.setTextColor(Color.WHITE);
				return tv;
			}

			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(AppManagerActivity.this,
						R.layout.view_listcell_appmanager, null);
				holder = new ViewHolder();
				holder.iv_app = (ImageView) view.findViewById(R.id.iv_app);
				holder.tv_app_name = (TextView) view
						.findViewById(R.id.tv_app_name);
				holder.tv_app_location = (TextView) view
						.findViewById(R.id.tv_app_location);
				view.setTag(holder);
			}

			AppInfo info = null;

			if (position <= userAppInfos.size()) {
				info = userAppInfos.get(position - 1);
			} else {
				info = systemAppInfos.get(position - 1 - userAppInfos.size()
						- 1);
			}

			holder.iv_app.setImageDrawable(info.getIcon());
			holder.tv_app_name.setText(info.getName());
			holder.tv_app_location.setText(info.isInRom() == true ? "手机内存 uid:"
					+ info.getUid() : "SD卡 uid:" + info.getUid());
			/*
			 * holder.iv_lockState.setImageResource(dao.queryIsExsit(info
			 * .getPackageName()) ? R.drawable.lock2 : R.drawable.unlock2);
			 */

			return view;
		}
	}

	/**
	 * ListView中被点击的条目
	 */
	private AppInfo info = null;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 卸载软件
	 * 
	 */
	private void uninstallApp() {
		// <action android:name="android.intent.action.DELETE" />
		// <category android:name="android.intent.category.DEFAULT" />
		// <data android:scheme="package" />

		if (!info.isUserApp()) {
			ShowCustomToast.show(this, "系统应用只有root权限才可以卸载");
			return;
		}

		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + info.getPackageName()));
		this.startActivity(intent);
	}

	/**
	 * 启动软件应用
	 */
	private void startApp() {
		PackageManager pm = this.getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(info.getPackageName());
		if (intent != null) {
			this.startActivity(intent);
		} else {
			ShowCustomToast.show(this, "不能启动该应用");
		}
	}

	/**
	 * 分享软件应用
	 */
	private void shareApp() {
		// Intent { act=android.intent.action.SEND typ=text/plain flg=0x3000000
		// cmp=com.android.mms/.ui.ComposeMessageActivity (has extras) } from
		// pid 256
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款软件,名称叫：" + info.getName());
		startActivity(intent);
	}

	/**
	 * 获取所有可以具有启动软件能力的Activity
	 */
	private void getAllLauncherAct() {
		PackageManager pm = this.getPackageManager();
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		List<ResolveInfo> rInfos = pm.queryIntentActivities(intent,
				PackageManager.GET_INTENT_FILTERS);
	}

}
