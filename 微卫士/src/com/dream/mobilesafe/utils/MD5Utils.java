package com.dream.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5密码工具类
 * 
 * @author 温坤哲
 * 
 */
public class MD5Utils {

	/**
	 * md5加密方法
	 * 
	 * @param passwd
	 *            未加密的密码
	 * @return 加密后的密码
	 */
	public static String getMD5Passwd(String passwd) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("md5");
			byte[] bytes = digest.digest(passwd.getBytes());
			StringBuffer buffer = new StringBuffer();
			for (byte b : bytes) {
				int number = b & 0xff;// 加盐
				String hex = Integer.toHexString(number);
				if (hex.length() == 1) {
					buffer.append("0");
				}
				buffer.append(hex);
			}
			// md5加密后的值
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

}
