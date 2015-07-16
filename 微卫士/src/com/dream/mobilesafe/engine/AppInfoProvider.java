package com.dream.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.dream.mobilesafe.domin.AppInfo;

public class AppInfoProvider {

	/**
	 * ��ȡ���е�Ӧ�ó�����Ϣ
	 * 
	 * @param context
	 *            �����Ķ���
	 * @return ���е�Ӧ�ó�����Ϣ����
	 */
	public static List<AppInfo> getAllAppInfos(Context context) {
		List<AppInfo> infos = new ArrayList<AppInfo>();

		PackageManager manager = context.getPackageManager();
		List<PackageInfo> pInfos = manager.getInstalledPackages(0);

		for (int i = 0; i < pInfos.size(); i++) {

			PackageInfo pInfo = pInfos.get(i);
			AppInfo info = new AppInfo();

			int flags = pInfo.applicationInfo.flags;// Ӧ�ó�����Ϣ�ı�� �൱���û��ύ�Ĵ��
			if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				// �û�����
				info.setUserApp(true);
			} else {
				// ϵͳ����
				info.setUserApp(false);
			}
			if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
				// �ֻ����ڴ�
				info.setInRom(true);
			} else {
				// �ֻ���洢�豸
				info.setInRom(false);
			}

			info.setIcon(pInfo.applicationInfo.loadIcon(manager));
			info.setName(pInfo.applicationInfo.loadLabel(manager).toString());
			info.setPackageName(pInfo.applicationInfo.packageName);
			info.setUid(pInfo.applicationInfo.uid);
			infos.add(info);
		}
		return infos;
	}
	
	
}
