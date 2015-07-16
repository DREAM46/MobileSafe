package com.dream.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dream.mobilesafe.db.dao.VirusDAO;
import com.dream.mobilesafe.domin.ScanInfo;
import com.dream.mobilesafe.utils.MD5Utils;
import com.dream.mobilesafe.view.CustomAlertDialog;
import com.dream.mobilesafe.view.CustomAlertDialog.OnCustomAlertDialogClickListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_anti_virus)
public class AntiVirusActivity extends Activity implements
		OnCustomAlertDialogClickListener {

	@ViewInject(R.id.iv_scan)
	private ImageView iv_scan;
	@ViewInject(R.id.progressBar1)
	private ProgressBar progressBar1;
	@ViewInject(R.id.tv_scan_status)
	private TextView tv_scan_status;
	@ViewInject(R.id.ll_container)
	private LinearLayout ll_container;

	private PackageManager pm;

	/**
	 * ��Ϣ��־�����ڽ���һ��ɨ��
	 */
	private static final int MSG_SCANNING = 1;
	/**
	 * ��Ϣ��־�����һ��ɨ��
	 */
	private static final int MSG_FINISHED_ONE = 2;
	/**
	 * ��Ϣ��־�����ȫ��ɨ��
	 */
	private static final int MSG_FINISHED_ALL = 3;

	private List<ScanInfo> virInfos = new ArrayList<ScanInfo>();

	private Handler scanHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case MSG_SCANNING:
				ScanInfo info = (ScanInfo) msg.obj;
				tv_scan_status.setText("����ɨ��:" + info.getAppname());
				progressBar1.setProgress(msg.arg1);
				break;
			case MSG_FINISHED_ONE:
				ScanInfo info1 = (ScanInfo) msg.obj;
				TextView tv = new TextView(AntiVirusActivity.this);
				tv.setText(info1.getAppname() + "ɨ�����");
				if (info1.isIsvirus()) {
					tv.setBackgroundColor(Color.RED);
					tv.append(",���ֲ���");
				} else {
					tv.append(",�����ȫ");
				}
				ll_container.addView(tv, 0);
				break;
			case MSG_FINISHED_ALL:
				tv_scan_status.setText("ɨ�����,����" + virInfos.size() + "������");
				iv_scan.clearAnimation();
				progressBar1.setProgress(0);

				// ɨ���������в�����Ҫ����ɱ��
				if (virInfos.size() > 0) {
					CustomAlertDialog dialog = new CustomAlertDialog(
							AntiVirusActivity.this)
							.setTitleText("����!!!")
							.setMsg("�������ֻ����淢���ˣ�" + virInfos.size()
									+ "������,�ֻ��Ѿ�ʮ��Σ�գ��÷�0�֣��Ͻ���ɱ������")
							.setPositiveText("�����鶾").setNegativeText("�´���˵")
							.setNeutralGone();
					dialog.setOnCustomAlertDialogClickListener(AntiVirusActivity.this);
					dialog.show();
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		this.scanVirus();
	}

	private void scanVirus() {
		pm = this.getPackageManager();
		tv_scan_status.setText("���ڳ�ʼ��ɱ������...");
		final List<PackageInfo> infos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
						+ PackageManager.GET_SIGNATURES);
		progressBar1.setMax(infos.size());
		RotateAnimation ra = new RotateAnimation(0, 360,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		ra.setRepeatCount(Animation.INFINITE);
		ra.setDuration(1000);
		ra.setInterpolator(new LinearInterpolator());
		iv_scan.startAnimation(ra);

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				for (int i = 0; i < infos.size(); i++) {

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					PackageInfo pi = infos.get(i);
					ApplicationInfo info = pi.applicationInfo;

					ScanInfo scanInfo = new ScanInfo();
					scanInfo.setPackname(info.packageName);
					scanInfo.setAppname(info.loadLabel(pm).toString());

					// ��������ɨ����Ϣ
					Message msg = scanHandler.obtainMessage();
					msg.obj = scanInfo;
					msg.what = MSG_SCANNING;
					msg.arg1 = i;
					scanHandler.sendMessage(msg);

					String md5 = MD5Utils.getMD5Passwd(pi.signatures[0]
							.toCharsString());
					String result = VirusDAO.isVirus(md5);
					if (result != null) {
						scanInfo.setIsvirus(true);
						scanInfo.setDesc(result);
						virInfos.add(scanInfo);
					} else {
						scanInfo.setIsvirus(false);
						scanInfo.setDesc(null);
					}

					// ����ɨ�������Ϣ
					Message msg1 = scanHandler.obtainMessage();
					msg1.obj = scanInfo;
					msg1.what = MSG_FINISHED_ONE;
					scanHandler.sendMessage(msg1);

				}
				Message msg2 = scanHandler.obtainMessage();
				msg2.what = MSG_FINISHED_ALL;
				scanHandler.sendMessage(msg2);
			}
		}).start();

	}

	@Override
	public void onPositive() {
		// ж��մȾ���������
		for (ScanInfo info : virInfos) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:" + info.getPackname()));
			startActivity(intent);
		}
	}

	@Override
	public void onNegative() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNeutral() {
		// TODO Auto-generated method stub

	}
}
