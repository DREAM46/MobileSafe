package com.dream.mobilesafe.domin;

public class BlackNumInfo {

	/**
	 * 黑名单号码
	 */
	private String number;
	/**
	 * 黑名单拦截模式
	 */
	private int mode;

	public BlackNumInfo() {
	}

	public BlackNumInfo(String number, int mode) {
		super();
		this.number = number;
		this.mode = mode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	@Override
	public String toString() {
		return "BlackNum [number=" + number + ", mode=" + mode + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		boolean flag = false;

		if (obj instanceof BlackNumInfo) {
			BlackNumInfo info = (BlackNumInfo) obj;
			if (info.getNumber().equals(this.number))
				flag = true;
		}

		return flag;
	}

}
