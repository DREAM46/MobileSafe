package com.dream.mobilesafe.db;

/**
 * ���ݿ⼰������Ϣ��
 * 
 * @author ������
 * 
 */
public class DBMsg {

	/**
	 * ���ݿ���Ϣ��
	 * 
	 * @author ������
	 * 
	 */
	public static class BLACKNUM_DB {
		/**
		 * ���������ݿ�����
		 */
		public static final String DB_NAME = "blackNum.db";
		/**
		 * ���������ݿ�汾��
		 */
		public static final int VERSION = 1;
	}

	/**
	 * �����������Ϣ��
	 * 
	 * @author ������
	 * 
	 */
	public static class BLACKNUM_TABLE {
		/**
		 * ��������ı���
		 */
		public static final String TABLE_NAME = "balckNum";
		/**
		 * _id�е�����
		 */
		public static final String COL_ID = "_id";
		/**
		 * �����е�����
		 */
		public static final String COL_NUM = "number";
		/**
		 * ����ģʽ�е�����
		 */
		public static final String COL_MODE = "mode";
	}

	public static class APP_DB {
		/**
		 * ���������ݿ�����
		 */
		public static final String DB_NAME = "appLock.db";
		/**
		 * ���������ݿ�汾
		 */
		public static final int VERSION = 1;
	}

	public static class APP_TABLE {
		/**
		 * ��������ı���
		 */
		public static final String TABLE_NAME = "appLock";
		/**
		 * _id�е�����
		 */
		public static final String COL_ID = "_id";
		/**
		 * �����е�����
		 */
		public static final String COL_PACKAGE = "packageName";
	}

}
