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
 * ϵͳ��Ϣ������
 * 
 * @author ������
 * 
 */
public class SystemInfoUtils {

	/**
	 * ��ȡ�������еĽ�������
	 * 
	 * @param context
	 *            �����Ķ���
	 * @return �������еĽ�������
	 */
	public static int getRunningProcessCount(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		return infos.size();
	}

	/**
	 * ��ȡ�ڴ���Ϣ(�����ڴ�/���ڴ�)
	 * 
	 * @param context
	 *            �����Ķ���
	 * @return �ֻ�ʣ���ڴ�
	 */
	public static String getAvailTotalMemory(Context context) {
		String text = "";
		// ����API16����ʹ��
		/*
		 * ActivityManager am = (ActivityManager) context
		 * .getSystemService(Context.ACTIVITY_SERVICE); MemoryInfo info = new
		 * MemoryInfo(); am.getMemoryInfo(info); text = "ʣ��/���ڴ�:" +
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

			text = "ʣ��/���ڴ�:" + Formatter.formatFileSize(context, info.availMem)
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
