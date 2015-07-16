package com.dream.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.text.format.Formatter;

/**
 * 系统信息工具类
 * 
 * @author 温坤哲
 * 
 */
public class SystemInfoUtils {

	/**
	 * 获取正在运行的进程数量
	 * 
	 * @param context
	 *            上下文对象
	 * @return 正在运行的进程数量
	 */
	public static int getRunningProcessCount(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		return infos.size();
	}

	/**
	 * 获取内存信息(可用内存/总内存)
	 * 
	 * @param context
	 *            上下文对象
	 * @return 手机剩余内存
	 */
	public static String getAvailTotalMemory(Context context) {
		String text = "";
		// 仅在API16以上使用
		/*
		 * ActivityManager am = (ActivityManager) context
		 * .getSystemService(Context.ACTIVITY_SERVICE); MemoryInfo info = new
		 * MemoryInfo(); am.getMemoryInfo(info); text = "剩余/总内存:" +
		 * Formatter.formatFileSize(context, info.availMem) + "/" +
		 * Formatter.formatFileSize(context, info.totalMem);
		 */

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo info = new MemoryInfo();
		am.getMemoryInfo(info);

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream("/proc/meminfo")));
			String total = br.readLine();
			// MemTotal: xxxxkB
			total = total.substring(total.indexOf(':') + 1, total.indexOf('k'))
					.trim();
			Long totalMem = Long.parseLong(total) * 1024;

			text = "剩余/总内存:" + Formatter.formatFileSize(context, info.availMem)
					+ "/" + Formatter.formatFileSize(context, totalMem);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return text;
	}

	public static String getAvailMemory(Context context) {
		String text = "";

		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo info = new MemoryInfo();
		am.getMemoryInfo(info);
		text = Formatter.formatFileSize(context, info.availMem);
		return text;
	}

}
