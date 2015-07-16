package com.dream.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.dream.mobilesafe.db.dao.BlackNumDAO;
import com.dream.mobilesafe.domin.BlackNumInfo;
import com.dream.mobilesafe.view.AddBlackNumDialog;
import com.dream.mobilesafe.view.CustomAlertDialog;
import com.dream.mobilesafe.view.CustomAlertDialog.OnCustomAlertDialogClickListener;
import com.dream.mobilesafe.view.CustomProgressDialog;
import com.dream.mobilesafe.view.ShowCustomToast;

public class CallSmsSafeActivity extends Activity implements OnClickListener,
		OnScrollListener {

	private SwipeMenuListView lv_callsafe;
	private ImageButton btn_callsafe_add;

	private List<BlackNumInfo> infos;

	private BlackNumListAdapter adapter;

	/**
	 * ����ģʽ���ַ�������
	 */
	private String[] modes = { "�绰����", "��������", "ȫ������" };

	private BlackNumDAO dao;

	/**
	 * ��ѯ���ݵĿ�ʼ���Ϊ
	 */
	private int offset = 0;
	/**
	 * ��ѯ���ݵ������Ŀ��
	 */
	private int maxnumber = 20;
	/**
	 * �Ƿ�����б������û����б�����ĩβ�Ž�����Ϊtrue
	 */
	private boolean isUpdateList = false;
	/**
	 * ���ݿ��е������Ƿ��������ӣ����ǣ���������ݸ���
	 */
	private boolean isDataChanged = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callsmssafe);

		infos = new ArrayList<BlackNumInfo>();

		lv_callsafe = (SwipeMenuListView) this.findViewById(R.id.lv_callsafe);
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// ����һ���˵���
				SwipeMenuItem item = new SwipeMenuItem(getApplicationContext());
				// set item background
				item.setBackground(new ColorDrawable(Color
						.rgb(0xF9, 0x3F, 0x25)));
				// set item width
				item.setWidth(dp2px(90));
				// set a icon
				item.setIcon(R.drawable.delete_selector);
				// add to menu
				menu.addMenuItem(item);
			}

		};
		// set creator
		lv_callsafe.setMenuCreator(creator);

		// step 2. Ϊ�˵����ü����¼�
		lv_callsafe.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(final int position, SwipeMenu menu,
					int index) {
				if (position == 0) {
					CustomAlertDialog dialog = new CustomAlertDialog(
							CallSmsSafeActivity.this).setMsg("��ȷ��Ҫɾ��������¼��")
							.setPositiveText("ȷ��").setNeutralGone();
					dialog.setOnCustomAlertDialogClickListener(new OnCustomAlertDialogClickListener() {

						@Override
						public void onPositive() {
							dao.delete((String) adapter.getItem(position));
							dataChanged(false);
							new QueryTask().execute();
						}

						@Override
						public void onNeutral() {}

						@Override
						public void onNegative() {}
					});
					dialog.show();
				}
				return false;
			}
		});

		lv_callsafe.setOnScrollListener(this);

		btn_callsafe_add = (ImageButton) this
				.findViewById(R.id.btn_callsafe_add);
		btn_callsafe_add.setOnClickListener(this);

		dao = new BlackNumDAO(this);
		new QueryTask().execute();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && isUpdateList) {
			offset += 20;
			new QueryTask().execute();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		isUpdateList = (firstVisibleItem + visibleItemCount == totalItemCount);
	}

	/**
	 * �첽�����ѯ���ݿ���
	 * 
	 * @author ������
	 * 
	 */
	private class QueryTask extends AsyncTask<Void, Void, Void> {
		private CustomProgressDialog dialog;

		public QueryTask() {
			dialog = new CustomProgressDialog(CallSmsSafeActivity.this);
			dialog.setMsg("���ڲ�ѯ�����Ժ�...");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			/*
			 * ���ݲ�ѯ˼·�� 1���û�����ListView������ĩ�˲���ͣ�����������в�
			 * ѯ����2�����Ͻ��в�ѯ��infosΪ�ܼ��ϣ���������ݣ���cacheInfosΪ
			 * ���漯�ϣ���Ų�ѯ�������ݣ��������漯��cacheInfos�е�������ӵ�
			 * �ܼ���infos�У�����������Ч�ؼ����û���ѯ��ʱ�䣬��ǿ�û������
			 * 
			 * 2�������û����ӻ���ɾ���˼�¼����Ӧ�ôӵ�һ����¼���𣬵õ�ԭ���� infos �����е����ж�������¼������Ӧ��Ӧ�ò�ѯ��������¼
			 */

			if (isDataChanged) {
				infos.clear();
				isDataChanged = false;
			}

			List<BlackNumInfo> cacheInfos = dao.queryPart(offset, maxnumber);
			maxnumber = 20;

			infos.addAll(cacheInfos);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dialog.dismiss();
			// �������������ڣ�����д����������������ڣ���������������ݸ���
			if (adapter == null) {
				adapter = new BlackNumListAdapter();
				lv_callsafe.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}

		}
	}

	/**
	 * ListView��������
	 * 
	 * @author ������
	 * 
	 */
	private class BlackNumListAdapter extends BaseAdapter {

		class ViewHolder {
			TextView tv_blackNum_num;
			TextView tv_black_mode;
		}

		ViewHolder holder = null;

		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public Object getItem(int position) {
			return infos.get(position).getNumber();
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View view = null;

			if (convertView == null) {
				view = View.inflate(CallSmsSafeActivity.this,
						R.layout.view_listcell_blacknum, null);
				holder = new ViewHolder();
				holder.tv_black_mode = (TextView) view
						.findViewById(R.id.tv_black_mode);
				holder.tv_blackNum_num = (TextView) view
						.findViewById(R.id.tv_blackNum_num);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			BlackNumInfo info = infos.get(position);

			holder.tv_blackNum_num.setText(info.getNumber());
			holder.tv_black_mode.setText(modes[info.getMode()]);

			return view;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_callsafe_add:
			final AddBlackNumDialog dialog = new AddBlackNumDialog(this);
			dialog.setOnPositiveClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String number = dialog.getEditMsg();
					if (TextUtils.isEmpty(number)) {
						ShowCustomToast
								.show(CallSmsSafeActivity.this, "���벻��Ϊ��");
						return;
					}
					int mode = -1;
					if (dialog.isCallChecked()) {
						mode += 1;
					}
					if (dialog.isSmsChecked()) {
						mode += 2;
						SharedPreferences sp = CallSmsSafeActivity.this
								.getSharedPreferences("sp_mf",
										Context.MODE_PRIVATE);
						String smsBlackNum = sp.getString("smsBlackNum", "");
						smsBlackNum += number + ",";
						Editor editor = sp.edit();
						editor.putString("smsBlackNum", smsBlackNum);
						editor.commit();
					}
					if (mode < 0) {
						ShowCustomToast.show(CallSmsSafeActivity.this,
								"����ѡ��һ������ģʽ");
						return;
					}

					if (dao.insert(new BlackNumInfo(number, mode)) != -1) {
						ShowCustomToast.show(CallSmsSafeActivity.this, "����ɹ�");
						dataChanged(true);
						new QueryTask().execute();
					}
					dialog.dismiss();
				}

			}).show();
			break;
		}
	}

	private void dataChanged(boolean isAdd) {
		isDataChanged = true;
		offset = 0;
		maxnumber = infos.size();
		if (isAdd)
			maxnumber++;
	}

}
