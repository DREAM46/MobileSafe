package com.dream.mobilesafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dream.mobilesafe.view.FindLostItem;

public class LostFindActivity extends Activity implements OnClickListener {

	private FindLostItem fi_num;
	private FindLostItem fi_configed;

	private Button btn_lf_setup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lostfind);

		fi_num = (FindLostItem) this.findViewById(R.id.fi_num);
		fi_configed = (FindLostItem) this.findViewById(R.id.fi_configed);

		Resources resources = this.getResources();
		SharedPreferences sp = this.getSharedPreferences(
				resources.getString(R.string.pre_name), Context.MODE_PRIVATE);
		fi_num.setContent(sp.getString("alarmNum", ""));

		if (sp.getBoolean(resources.getString(R.string.pre_isSetUp), false)) {
			fi_configed.setTextBackground(R.drawable.lock);
		} else {
			fi_configed.setTextBackground(R.drawable.unlock);
		}

		btn_lf_setup = (Button) this.findViewById(R.id.btn_lf_setup);
		btn_lf_setup.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		this.startActivity(new Intent(this, LostFindSetUpActivity.class));
		this.finish();
	}

}
