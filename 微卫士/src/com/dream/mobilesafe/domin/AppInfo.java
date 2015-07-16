package com.dream.mobilesafe.domin;

import android.graphics.drawable.Drawable;

public class AppInfo {

	/**
	 * Ӧ�ó���ͼ��
	 */
	private Drawable icon;

	/**
	 * Ӧ�ó�������
	 */
	private String name;

	/**
	 * ����
	 */
	private String packageName;

	/**
	 * �Ƿ�������ֻ��ڴ���
	 */
	private boolean isInRom;

	/**
	 * �Ƿ����û�Ӧ��
	 */
	private boolean isUserApp;

	/**
	 * ����ϵͳ�����Ӧ�ó���ı�ţ�һ����װ�ɹ����޷��ı���
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
