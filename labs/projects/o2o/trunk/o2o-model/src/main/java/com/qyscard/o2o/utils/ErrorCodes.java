package com.qyscard.o2o.utils;


public final class ErrorCodes {
	/*****
	 * 默认的系统错误代码
	 */
	public static final String SYSTEM_ERROR = "ERR_";
	public static final String UNIQUE_DATA = SYSTEM_ERROR + "UNIQUE_DATA";
	


	public static final class SystemErrors {
		public static final String BASE_CODE = "SYS_";
		/****
		 * 用户名已存在
		 */
		public static final String USER_NAME_EXIST = BASE_CODE + "USER_NAME_EXIST";
		public static final String ROOT_ROLE_CANNOT_DELETE = BASE_CODE + "ROOT_ROLE_CANNOT_DELETE";
	}
	
	private ErrorCodes(){
	}

}
