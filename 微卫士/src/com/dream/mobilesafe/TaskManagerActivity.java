package com.dream.mobilesafe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.mobilesafe.domin.TaskInfo;
import com.dream.mobilesafe.engine.TaskInfoProvider;
import com.dream.mobilesafe.utils.SystemInfoUtils;
import com.dream.mobilesafe.view.CustomProgressDialog;
import com.dream.mobilesafe.view.ShowCustomToast;

public class TaskManagerActivity extends Activity implements OnScrollListener,
		OnItemClickListener, OnClickListener {

	private TextView tv_process_count;
	private TextView tv_meo_info;

	private ListView lv_task;
	private TextView tv_task_lable;

	private Button btn_task_all;
	private Button btn_task_noAll;
	private Button btn_task_clean;

	private ImageButton ib_setting_task;

	private LinearLayout ll_task_btn;

	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> sysTaskInfos;

	private TaskListAdapter adapter;

	private ActivityManager am;

	private SharedPreferences sp;

	/**
	 * 选中个数
	 */
	private int checkedCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);

		am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

		sp = this.getSharedPreferences(
				this.getResources().getString(R.string.pre_name),
				Context.MODE_PRIVATE);

		tv_process_count = (TextView) this.findViewById(R.id.tv_process_count);
		tv_meo_info = (TextView) this.findViewById(R.id.tv_meo_info);
		tv_task_lable = (TextView) this.findViewById(R.id.tv_task_lable);
		lv_task = (ListView) this.findViewById(R.id.lv_task);

		btn_task_all = (Button) this.findViewById(R.id.btn_task_all);
		btn_task_noAll = (Button) this.findViewById(R.id.btn_task_noAll);
		btn_task_clean = (Button) this.findViewById(R.id.btn_task_clean);

		ib_setting_task = (ImageButton) this.findViewById(R.id.ib_setting_task);

		ll_task_btn = (LinearLayout) this.findViewById(R.id.ll_task_btn);
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.setSystemInfo();
		new LoadTask().execute();
	}

	private class LoadTask extends AsyncTask<Void, Void, Void> {

		private CustomProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new CustomProgressDialog(TaskManagerActivity.this);
			dialog.setMsg("正在加载,请稍后...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				userTaskInfos = TaskInfoProvider
						.getTaskInfos(TaskManagerActivity.this);
				sysTaskInfos = new ArrayList<TaskInfo>();
				for (int i = 0; i < userTaskInfos.size(); i++) {
					TaskInfo info = userTaskInfos.get(i);
					if (!info.isUserTask()) {
						sysTaskInfos.add(info);
						userTaskInfos.remove(i);
					}
				}

				if (!sp.getBoolean("pre_isShowSys", true))
					sysTaskInfos.clear();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dialog.dismiss();
			tv_task_lable.setVisibility(View.VISIBLE);
			if (adapter == null) {
				adapter = new TaskListAdapter();
				lv_task.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
			tv_task_lable.setText("用户程序(" + userTaskInfos.size() + ")");
			lv_task.setOnScrollListener(TaskManagerActivity.this);
			lv_task.setOnItemClickListener(TaskManagerActivity.this);

			ll_task_btn.setVisibility(View.VISIBLE);
			btn_task_all.setOnClickListener(TaskManagerActivity.this);
			btn_task_noAll.setOnClickListener(TaskManagerActivity.this);
			btn_task_clean.setOnClickListener(TaskManagerActivity.this);
			ib_setting_task.setOnClickListener(TaskManagerActivity.this);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_task_all:
			this.setAllCheckedOrNot(true);
			adapter.notifyDataSetChanged();
			this.setSystemInfo();
			checkedCount = sysTaskInfos.size() + userTaskInfos.size();
			break;
		case R.id.btn_task_noAll:
			// this.setAllCheckedOrNot(false);
			// 将选中进程个数归0
			checkedCount = 0;
			this.setCheckedNo();
			adapter.notifyDataSetChanged();
			this.setSystemInfo();
			break;
		case R.id.btn_task_clean:
			int count = this.killProcess();
			if (count != -1) {
				new LoadTask().execute();
				adapter.notifyDataSetChanged();
				this.setSystemInfo();
			}
			break;
		case R.id.ib_setting_task:
			this.startActivity(new Intent(this,
					TaskManagerSettingActivity.class));
			break;
		}

	}

	/**
	 * 反选
	 */
	private void setCheckedNo() {
		this.setListNo(sysTaskInfos);
		this.setListNo(userTaskInfos);
	}

	/**
	 * 反选
	 * 
	 * @param infos
	 *            反选集合
	 */
	private void setListNo(List<TaskInfo> infos) {
		for (int i = 0; i < infos.size(); i++) {
			TaskInfo info = infos.get(i);
			boolean checked = !info.isChecked();
			info.setChecked(checked);
			if (checked == true)
				checkedCount += 1;
		}
	}

	/**
	 * 重新设置内存信息文字
	 */
	private void setSystemInfo() {
		int TaskCount = SystemInfoUtils.getRunningProcessCount(this);
		tv_process_count.setText("运行中的进程:" + TaskCount + "  ");
		tv_meo_info.setText(SystemInfoUtils.getAvailTotalMemory(this));
	}

	/**
	 * 设置所有的任务进程被选中与否
	 * 
	 * @param isChecked
	 *            所有的任务进程被选中与否
	 */
	private void setAllCheckedOrNot(boolean isChecked) {
		this.CheckedOrNot(userTaskInfos, isChecked);
		this.CheckedOrNot(sysTaskInfos, isChecked);
	}

	/**
	 * 杀死任务进程集合中被选中要杀死的进程
	 * 
	 * @param infos
	 *            任务进程集合
	 */
	private int killProcess() {
		if (checkedCount == 0) {
			ShowCustomToast.show(this, "你没有选中进程");
			return -1;
		}
		int count = 0;
		for (int i = 0; i < userTaskInfos.size(); i++) {
			TaskInfo info = userTaskInfos.get(i);
			if (info.isChecked()) {
				am.killBackgroundProcesses(info.getPackName());
				userTaskInfos.remove(info);
				count++;
			}
		}
		for (int i = 0; i < sysTaskInfos.size(); i++) {
			TaskInfo info = sysTaskInfos.get(i);
			if (info.isChecked()) {
				am.killBackgroundProcesses(info.getPackName());
				sysTaskInfos.remove(info);
				count++;
			}
		}
		checkedCount = 0;
		return count;
	}

	/**
	 * 设置任务集合中的任务被选中与否
	 * 
	 * @param infos
	 *            任务集合
	 * @param isChecked
	 *            任务集合中的任务被选中与否
	 */
	private void CheckedOrNot(List<TaskInfo> infos, boolean isChecked) {
		for (Iterator<TaskInfo> iterator = infos.iterator(); iterator.hasNext();) {
			iterator.next().setChecked(isChecked);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (firstVisibleItem > userTaskInfos.size())
			tv_task_lable.setText("系统进程(" + sysTaskInfos.size() + ")");
		else
			tv_task_lable.setText("用户进程(" + userTaskInfos.size() + ")");
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	private class TaskListAdapter extends BaseAdapter {

		private class ViewHolder {
			ImageView iv_task;
			TextView tv_task_name;
			TextView tv_task_size;
			CheckBox cb_task;
		}

		ViewHolder holder;

		@Override
		public int getCount() {
			return userTaskInfos.size() + 1 + sysTaskInfos.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;

			if (position == 0 || position == userTaskInfos.size() + 1) {
				TextView tv = new TextView(TaskManagerActivity.this);
				if (position == 0)
					tv.setText("用户进程(" + userTaskInfos.size() + ")");
				else if (sp.getBoolean("pre_isShowSys", true))
					tv.setText("系统进程(" + sysTaskInfos.size() + ")");
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				view = tv;
			} else {

				if (convertView != null
						&& convertView instanceof RelativeLayout) {

					view = convertView;
					holder = (ViewHolder) view.getTag();

				} else {

					view = View.inflate(TaskManagerActivity.this,
							R.layout.view_listcell_taskmanager, null);
					holder = new ViewHolder();
					holder.iv_task = (ImageView) view
							.findViewById(R.id.iv_task);
					holder.tv_task_name = (TextView) view
							.findViewById(R.id.tv_task_name);
					holder.tv_task_size = (TextView) view
							.findViewById(R.id.tv_task_size);
					holder.cb_task = (CheckBox) view.findViewById(R.id.cb_task);
					view.setTag(holder);
				}

				TaskInfo info = null;

				if (position <= userTaskInfos.size()) {
					info = userTaskInfos.get(position - 1);
				} else {
					info = sysTaskInfos.get(position - 1 - userTaskInfos.size()
							- 1);
				}

				holder.iv_task.setImageDrawable(info.getIcon());
				holder.tv_task_name.setText(info.getPackName());
				holder.tv_task_size.setText(info.getMemsize());
				holder.cb_task.setChecked(info.isChecked());
			}
			return view;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CheckBox cb_task = ((TaskListAdapter.ViewHolder) view.getTag()).cb_task;
		if (position == 0 || position == userTaskInfos.size() + 1)
			return;
		TaskInfo info = null;
		if (position <= userTaskInfos.size()) {
			info = userTaskInfos.get(position - 1);
		} else {
			info = sysTaskInfos.get(position - 1 - userTaskInfos.size() - 1);
		}
		info.setChecked(!info.isChecked());
		cb_task.setChecked(info.isChecked());
		if (info.isChecked())
			checkedCount++;
		else
			checkedCount--;
	}

}
