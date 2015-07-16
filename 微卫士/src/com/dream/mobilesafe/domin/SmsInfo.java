package com.dream.mobilesafe.domin;

public class SmsInfo {

	public static final String TYPE_SEND = "1";
	public static final String TYPE_RECEIVE = "2";

	private String address;
	private String body;
	private String date;
	private String type;

	public SmsInfo() {

	}

	public SmsInfo(String address, String body, String date, String type) {
		this.address = address;
		this.body = body;
		this.date = date;
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
