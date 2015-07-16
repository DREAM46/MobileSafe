package com.dream.mobilesafe.domin;

import android.graphics.drawable.Drawable;

/**
 * 任务进程信息类
 * 
 * @author 温坤哲
 * 
 */
public class TaskInfo {
	private Drawable icon;
	private String name;
	private String packName;
	private boolean isUserTask;
	private String memsize;
	private boolean isChecked;

	public TaskInfo() {

	}

	public TaskInfo(Drawable icon, String name, String packName,
			boolean isUserTask, String memsize, boolean isChecked) {
		this.icon = icon;
		this.name = name;
		this.packName = packName;
		this.isUserTask = isUserTask;
		this.memsize = memsize;
		this.isChecked = isChecked;
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

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public boolean isUserTask() {
		return isUserTask;
	}

	public void setUserTask(boolean isUserTask) {
		this.isUserTask = isUserTask;
	}

	public String getMemsize() {
		return memsize;
	}

	public void setMemsize(String memsize) {
		this.memsize = memsize;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public String toString() {
		return "TaskInfo [icon=" + icon + ", name=" + name + ", packName="
				+ packName + ", isUserTask=" + isUserTask + ", memsize="
				+ memsize + "]";
	}

}
