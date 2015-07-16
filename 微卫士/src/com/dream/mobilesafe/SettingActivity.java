package com.dream.mobilesafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.dream.mobilesafe.service.AddressService;
import com.dream.mobilesafe.service.CallSmsSafeService;
import com.dream.mobilesafe.utils.ServiceUtils;
import com.dream.mobilesafe.view.CustomRadioDialog;
import com.dream.mobilesafe.view.SettingItem;
import com.dream.mobilesafe.view.SettingItem1;
import com.dream.mobilesafe.view.SlidingToggleButton;
import com.dream.mobilesafe.view.SlidingToggleButton.OnSlidingBtnClickListener;

public class SettingActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnSlidingBtnClickListener {

	private SettingItem si_setting_autoUpdate;
	private SettingItem si_setting_address;
	private SettingItem si_setting_openBlackNum;
	private SettingItem1 si1_selectDlgBg;

	private SlidingToggleButton stb_autoUpdate;
	private SlidingToggleButton stb_address;
	private SlidingToggleButton stb_openBlackNum;

	private String[] dlgBgs;

	private CustomRadioDialog dialog;

	private Editor editor;

	private Resources resources;

	@Override
	protected void onResume() {
		super.onResume();
		boolean isRunning = ServiceUtils.isServiceRunning(this,
				"com.dream.mobilesafe.service.AddressService");
		boolean isOpenCallSmsService = ServiceUtils.isServiceRunning(this,
				"com.dream.mobilesafe.service.CallSmsSafeService");
		stb_address.initChecked(isRunning);
		si_setting_address.setChecked(isRunning);
		stb_openBlackNum.initChecked(isOpenCallSmsService);
		si_setting_openBlackNum.setChecked(isOpenCallSmsService);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		resources = this.getResources();

		SharedPreferences preferences = this.getSharedPreferences(
				resources.getString(R.string.pre_name), Context.MODE_PRIVATE);
		editor = preferences.edit();

		initView(preferences);

	}

	private void initView(SharedPreferences preferences) {
		si_setting_autoUpdate = (SettingItem) this
				.findViewById(R.id.si_setting_autoupdate);
		si_setting_address = (SettingItem) this
				.findViewById(R.id.si_setting_address);
		si_setting_address.setChecked(false);
		si1_selectDlgBg = (SettingItem1) this
				.findViewById(R.id.si1_selectDlgBg);
		si_setting_openBlackNum = (SettingItem) this
				.findViewById(R.id.si_setting_openBlackNum);

		stb_autoUpdate = (SlidingToggleButton) this
				.findViewById(R.id.stb_autoUpdate);

		stb_address = (SlidingToggleButton) this.findViewById(R.id.stb_address);
		stb_openBlackNum = (SlidingToggleButton) this
				.findViewById(R.id.stb_openBlackNum);

		boolean isAutoUpdate = preferences.getBoolean(
				resources.getString(R.string.pre_isAutoUpdate), false);
		stb_autoUpdate.initChecked(isAutoUpdate);
		System.out.println("isAutoUpdate:" + isAutoUpdate + "\n"
				+ "stb_address:" + stb_autoUpdate.isChecked());
		si_setting_autoUpdate.setChecked(isAutoUpdate);

		dlgBgs = resources.getStringArray(R.array.dlgBgs);
		int dlgBg = preferences.getInt(resources.getString(R.string.pre_DlgBg),
				0);
		si1_selectDlgBg.setDesc(dlgBgs[dlgBg]);
		si1_selectDlgBg.setOnClickListener(this);

		stb_autoUpdate.setOnSlidingBtnClickListener(this);
		stb_address.setOnSlidingBtnClickListener(this);
		stb_openBlackNum.setOnSlidingBtnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.si1_selectDlgBg) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, dlgBgs);
			dialog = new CustomRadioDialog(this).setAdapter(adapter)
					.setOnItemClickListener(this);
			dialog.show();
		}
	}

	@Override
	public void onSlidingBtnClick(SlidingToggleButton stb) {
		boolean isCheckedNow = stb.isChecked();
		switch (stb.getId()) {
		case R.id.stb_autoUpdate:
			si_setting_autoUpdate.setChecked(isCheckedNow);
			editor.putBoolean(resources.getString(R.string.pre_isAutoUpdate),
					isCheckedNow);
			editor.commit();
			break;
		case R.id.stb_address:
			Intent intent = new Intent(this, AddressService.class);
			si_setting_address.setChecked(isCheckedNow);
			if (isCheckedNow) {
				this.startService(intent);
			} else {
				this.stopService(intent);
			}
			break;
		case R.id.stb_openBlackNum:
			Intent intent2 = new Intent(this, CallSmsSafeService.class);
			si_setting_openBlackNum.setChecked(isCheckedNow);
			if (isCheckedNow) {
				this.startService(intent2);
			} else {
				this.stopService(intent2);
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		editor.putInt(resources.getString(R.string.pre_DlgBg), position);
		editor.commit();
		dialog.dismiss();
		si1_selectDlgBg.setDesc(dlgBgs[position]);
	}

}
