package com.dream.mobilesafe.domin;

public class ScanInfo {
	private String packname;
	private boolean isvirus;
	private String desc;
	private String appname;

	public ScanInfo() {

	}

	public ScanInfo(String packname, boolean isvirus, String desc,
			String appname) {
		this.packname = packname;
		this.isvirus = isvirus;
		this.desc = desc;
		this.appname = appname;
	}

	public String getPackname() {
		return packname;
	}

	public void setPackname(String packname) {
		this.packname = packname;
	}

	public boolean isIsvirus() {
		return isvirus;
	}

	public void setIsvirus(boolean isvirus) {
		this.isvirus = isvirus;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

}
