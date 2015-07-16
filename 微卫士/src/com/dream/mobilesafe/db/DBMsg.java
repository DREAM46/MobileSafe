package com.dream.mobilesafe.db;

/**
 * 数据库及其表的信息类
 * 
 * @author 温坤哲
 * 
 */
public class DBMsg {

	/**
	 * 数据库信息类
	 * 
	 * @author 温坤哲
	 * 
	 */
	public static class BLACKNUM_DB {
		/**
		 * 黑名单数据库名称
		 */
		public static final String DB_NAME = "blackNum.db";
		/**
		 * 黑名单数据库版本号
		 */
		public static final int VERSION = 1;
	}

	/**
	 * 黑名单表的信息类
	 * 
	 * @author 温坤哲
	 * 
	 */
	public static class BLACKNUM_TABLE {
		/**
		 * 黑名单表的表名
		 */
		public static final String TABLE_NAME = "balckNum";
		/**
		 * _id列的列名
		 */
		public static final String COL_ID = "_id";
		/**
		 * 号码列的列名
		 */
		public static final String COL_NUM = "number";
		/**
		 * 拦截模式列的列名
		 */
		public static final String COL_MODE = "mode";
	}

	public static class APP_DB {
		/**
		 * 程序锁数据库名称
		 */
		public static final String DB_NAME = "appLock.db";
		/**
		 * 程序锁数据库版本
		 */
		public static final int VERSION = 1;
	}

	public static class APP_TABLE {
		/**
		 * 黑名单表的表名
		 */
		public static final String TABLE_NAME = "appLock";
		/**
		 * _id列的列名
		 */
		public static final String COL_ID = "_id";
		/**
		 * 号码列的列名
		 */
		public static final String COL_PACKAGE = "packageName";
	}

}
