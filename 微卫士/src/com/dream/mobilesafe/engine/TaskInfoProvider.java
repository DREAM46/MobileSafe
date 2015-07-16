package com.dream.mobilesafe.engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Debug.MemoryInfo;
import android.text.format.Formatter;

import com.dream.mobilesafe.R;
import com.dream.mobilesafe.domin.TaskInfo;

public class TaskInfoProvider {

	/**
	 * 获取所有任务进程
	 * 
	 * @param context
	 *            上下文对象
	 * @return 所有任务进程对象
	 * @throws Exception
	 */
	public static List<TaskInfo> getTaskInfos(Context context) {
		List<TaskInfo> infos = new ArrayList<TaskInfo>();

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		List<RunningAppProcessInfo> rInfos = am.getRunningAppProcesses();

		for (Iterator<RunningAppProcessInfo> iterator = rInfos.iterator(); iterator
				.hasNext();) {
			RunningAppProcessInfo ri = iterator.next();
			TaskInfo info = new TaskInfo();
			String processName = ri.processName;
			MemoryInfo[] mInfo = am.getProcessMemoryInfo(new int[] { ri.pid });

			info.setPackName(processName);
			info.setMemsize(Formatter.formatFileSize(context,
					mInfo[0].getTotalPrivateDirty() * 1024l));

			ApplicationInfo aInfo;
			try {
				aInfo = pm.getApplicationInfo(processName, 0);
				info.setName(aInfo.loadLabel(pm).toString());
				info.setIcon(aInfo.loadIcon(pm));
				int flags = aInfo.flags;
				// 应用程序信息的标记 相当于用户提交的答卷
				if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					// 用户程序
					info.setUserTask(true);
				} else {
					// 系统程序
					info.setUserTask(false);
				}
				infos.add(info);

			} catch (NameNotFoundException e) {
				e.printStackTrace();
				info.setIcon(context.getResources().getDrawable(
						R.drawable.ic_launcher));
				info.setName(processName);
				infos.add(info);
			}

		}
		return infos;
	}
}
