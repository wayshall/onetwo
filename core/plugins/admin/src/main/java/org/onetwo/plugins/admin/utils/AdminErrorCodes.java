package org.onetwo.plugins.admin.utils;


public final class AdminErrorCodes {
	/*****
	 * 默认的系统错误代码
	 */
	public static final String SYSTEM_ERROR = "ERR_";
	public static final String UNIQUE_DATA = SYSTEM_ERROR + "UNIQUE_DATA";
	
	public static final class SystemErrors {
		public static final String BASE_CODE = "SYS_";
		public static final String AREA_DATA_MUST_BE_UNIQUE = BASE_CODE + "AREA_DATA_MUST_BE_UNIQUE";
		/****
		 * 用户名已存在
		 */
		public static final String USER_NAME_EXIST = BASE_CODE + "USER_NAME_EXIST";
		public static final String ROOT_ROLE_CANNOT_DELETE = BASE_CODE + "ROOT_ROLE_CANNOT_DELETE";
	}
	

	private AdminErrorCodes(){
	}

}
