package com.dream.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5���빤����
 * 
 * @author ������
 * 
 */
public class MD5Utils {

	/**
	 * md5���ܷ���
	 * 
	 * @param passwd
	 *            δ���ܵ�����
	 * @return ���ܺ������
	 */
	public static String getMD5Passwd(String passwd) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("md5");
			byte[] bytes = digest.digest(passwd.getBytes());
			StringBuffer buffer = new StringBuffer();
			for (byte b : bytes) {
				int number = b & 0xff;// ����
				String hex = Integer.toHexString(number);
				if (hex.length() == 1) {
					buffer.append("0");
				}
				buffer.append(hex);
			}
			// md5���ܺ��ֵ
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

}
