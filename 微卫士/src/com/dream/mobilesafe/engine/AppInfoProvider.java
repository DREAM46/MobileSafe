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
	 * 获取所有的应用程序信息
	 * 
	 * @param context
	 *            上下文对象
	 * @return 所有的应用程序信息集合
	 */
	public static List<AppInfo> getAllAppInfos(Context context) {
		List<AppInfo> infos = new ArrayList<AppInfo>();

		PackageManager manager = context.getPackageManager();
		List<PackageInfo> pInfos = manager.getInstalledPackages(0);

		for (int i = 0; i < pInfos.size(); i++) {

			PackageInfo pInfo = pInfos.get(i);
			AppInfo info = new AppInfo();

			int flags = pInfo.applicationInfo.flags;// 应用程序信息的标记 相当于用户提交的答卷
			if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				// 用户程序
				info.setUserApp(true);
			} else {
				// 系统程序
				info.setUserApp(false);
			}
			if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
				// 手机的内存
				info.setInRom(true);
			} else {
				// 手机外存储设备
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
