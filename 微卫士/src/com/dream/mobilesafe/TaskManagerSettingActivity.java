package com.dream.mobilesafe;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.dream.mobilesafe.view.SettingItem;
import com.dream.mobilesafe.view.SlidingToggleButton;
import com.dream.mobilesafe.view.SlidingToggleButton.OnSlidingBtnClickListener;

public class TaskManagerSettingActivity extends Activity implements
		OnSlidingBtnClickListener {

	private SettingItem si_task_showSys;
	private SlidingToggleButton stb_task_showSys;
	private Editor editor;

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_taskmanager);

		SharedPreferences sp = this.getSharedPreferences(this.getResources()
				.getString(R.string.pre_name), Context.MODE_PRIVATE);
		editor = sp.edit();

		si_task_showSys = (SettingItem) this.findViewById(R.id.si_task_showSys);
		stb_task_showSys = (SlidingToggleButton) this
				.findViewById(R.id.stb_task_showSys);

		boolean isShow = sp.getBoolean("pre_isShowSys", true);
		si_task_showSys.setChecked(isShow);
		stb_task_showSys.initChecked(isShow);
		stb_task_showSys.setOnSlidingBtnClickListener(this);
	}

	@Override
	public void onSlidingBtnClick(SlidingToggleButton stb) {
		switch (stb.getId()) {
		case R.id.stb_task_showSys:
			boolean isShow = stb.isChecked();
			si_task_showSys.setChecked(isShow);
			editor.putBoolean("pre_isShowSys", isShow);
			System.out.println("show:" + si_task_showSys.isChecked());
			break;
		}
		editor.commit();
	}
}
