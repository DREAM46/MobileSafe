package com.dream.mobilesafe.domin;

/**
 * 联系人信息类，包含联系人id，联系人姓名以及电话号码
 * 
 * @author 温坤哲
 * 
 */
public class ContactInfo {

	private String id;
	private String name;
	private String number;

	public ContactInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ContactInfo(String name, String number) {
		super();
		this.name = name;
		this.number = number;
	}

	public ContactInfo(String id, String name, String number) {
		this(name, number);
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "ContactInfo [id=" + id + ", name=" + name + ", number="
				+ number + "]";
	}

	@Override
	public boolean equals(Object o) {

		boolean flag = false;

		if (o instanceof ContactInfo) {
			ContactInfo info = (ContactInfo) o;
			if (info.getName().equals(name) && info.getNumber().equals(number)) {
				flag = true;
			}
		}

		return flag;
	}
}
