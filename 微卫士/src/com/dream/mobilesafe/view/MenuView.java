package com.dream.mobilesafe.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dream.mobilesafe.HomeActivity;
import com.dream.mobilesafe.R;
import com.dream.mobilesafe.SettingActivity;
import com.dream.mobilesafe.GuideActivity;
import com.dream.mobilesafe.base.MyBaseAdapter;

/**
 * 菜单视图
 * 
 * @author 温坤哲
 * 
 */
public class MenuView extends RelativeLayout implements OnItemClickListener {

	private ListView lv_menu;
	private List<HashMap<String, Object>> data;

	private static final String KEY_IMG = "img";
	private static final String KEY_TEXT = "text";

	private static final int POS_ABOUT = 0;
	private static final int POS_UPGRADE = 1;
	private static final int POS_FUNCTION = 2;
	private static final int POS_MORE = 3;

	private Context context;

	public MenuView(Context context) {
		super(context);
		this.context = context;
		initView(context);
	}

	private void initView(Context context) {
		View view = View.inflate(context, R.layout.view_menu_sliding, this);
		lv_menu = (ListView) view.findViewById(R.id.lv_menu);

		data = new ArrayList<HashMap<String, Object>>();

		this.addElementToData(R.drawable.about, "关于");
		this.addElementToData(R.drawable.upgrade, "升级");
		this.addElementToData(R.drawable.about, "功能页");
		this.addElementToData(R.drawable.more, "更多");

		KWAdapter adapter = new KWAdapter(context, data);
		lv_menu.setAdapter(adapter);
		lv_menu.setOnItemClickListener(this);
	}

	private void addElementToData(Integer imgResId, String text) {
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put(KEY_IMG, imgResId);
		map1.put(KEY_TEXT, text);
		data.add(map1);
	}

	private class KWAdapter extends MyBaseAdapter<HashMap<String, Object>> {

		private class ViewHolder {
			ImageView img_menuitem;
			TextView tv_menuitem;
		}

		ViewHolder holder;

		public KWAdapter(Context context, List<HashMap<String, Object>> data) {
			super(context, data);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				view = View.inflate(context, R.layout.view_item_menulist, null);
				holder = new ViewHolder();
				holder.img_menuitem = (ImageView) view
						.findViewById(R.id.img_menuitem);
				holder.tv_menuitem = (TextView) view
						.findViewById(R.id.tv_menuitem);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			HashMap<String, Object> map = data.get(position);
			holder.img_menuitem.setImageResource((Integer) map.get(KEY_IMG));
			holder.tv_menuitem.setText((String) map.get(KEY_TEXT));
			return view;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case POS_ABOUT:
			ShowCustomToast.show(context, "关于");
			break;
		case POS_UPGRADE:
			ShowCustomToast.show(context, "升级");
			break;
		case POS_MORE:
			context.startActivity(new Intent(context, SettingActivity.class));
			break;
		case POS_FUNCTION:
			context.startActivity(new Intent(context, GuideActivity.class)
					.putExtra("isStarted", false));
			break;
		}
		// 关闭菜单
		((HomeActivity) context).getMenu().toggle();
	}

}
