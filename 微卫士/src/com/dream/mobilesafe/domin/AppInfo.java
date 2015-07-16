package com.dream.mobilesafe.domin;

import android.graphics.drawable.Drawable;

public class AppInfo {

	/**
	 * 应用程序图标
	 */
	private Drawable icon;

	/**
	 * 应用程序名称
	 */
	private String name;

	/**
	 * 包名
	 */
	private String packageName;

	/**
	 * 是否存在与手机内存中
	 */
	private boolean isInRom;

	/**
	 * 是否是用户应用
	 */
	private boolean isUserApp;

	/**
	 * 操作系统分配给应用程序的编号，一旦安装成功就无法改变了
	 */
	private int uid;

	public AppInfo() {

	}

	public AppInfo(Drawable icon, String name, String packageName,
			boolean isInRom, boolean isUserApp) {
		this.icon = icon;
		this.name = name;
		this.packageName = packageName;
		this.isInRom = isInRom;
		this.isUserApp = isUserApp;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isInRom() {
		return isInRom;
	}

	public void setInRom(boolean isInRom) {
		this.isInRom = isInRom;
	}

	public boolean isUserApp() {
		return isUserApp;
	}

	public void setUserApp(boolean isUserApp) {
		this.isUserApp = isUserApp;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	@Override
	public String toString() {
		return "AppInfo [icon=" + icon + ", name=" + name + ", packageName="
				+ packageName + ", isInRom=" + isInRom + ", isUserApp="
				+ isUserApp + "]";
	}

}
