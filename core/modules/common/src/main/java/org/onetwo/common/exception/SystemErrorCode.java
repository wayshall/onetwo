package org.onetwo.common.exception;

public interface SystemErrorCode {
	
	public String APP_ERROR_MESSAGE = "appErrorMessage";
	public String DEFAULT_SYSTEM_ERROR_CODE = "SYSTEM_ERROR";
	public String UNKNOWN = "unknown";
	
	public static class JFishErrorCode {
		private JFishErrorCode(){}
		public static final String BASE_CODE = "[BASE]";//前缀
		public static final String DB_ERROR = BASE_CODE + "DB_ERROR";
		public static final String ORM_ERROR = BASE_CODE + "ORM_ERROR";
		public static final String REST_INVOKE_ERROR = BASE_CODE + "REST_INVOKE_ERROR";
		public static final String INVALID_TOKEN_ERROR = BASE_CODE + "INVALID_TOKEN_ERROR";
	}
	
	public static class LoginErrorCode {
		private LoginErrorCode(){}
		public static final String BASE_CODE = "[LOGIN]";//前缀
		public static final String USER_NOT_FOUND = BASE_CODE + "USER_NOT_FOUND";//找不到此用户
		public static final String PASSWORD_ERROR = BASE_CODE + "PASSWORD_ERROR";//错误的密码
		public static final String PASSWORD_CONFIRM_ERROR = BASE_CODE + "PASSWORD_CONFIRM_ERROR";//两次密码不一致
		public static final String NO_TOKEN  = BASE_CODE + "NO_TOKEN";//没有找到此令牌的用户，请先登陆
		public static final String USER_STATE_ERROR = BASE_CODE + "USER_STATE_ERROR";//用户状态不正常
	}
	
	public static class AuthenticErrorCode {
		private AuthenticErrorCode(){}
		public static final String BASE_CODE = "[AUTHENTIC]";//前缀
		public static final String PERMISSION_DENY  = BASE_CODE + "PERMISSION_DENY";//验证失败，没有权限
		public static final String NOT_LOGIN_YET  = BASE_CODE + "NOT_LOGIN_YET";//没有登陆
		public static final String SESSION_TIMEOUT = BASE_CODE + "SESSION_TIMEOUT";//登陆超时
		public static final String ERROR_ROLE = BASE_CODE + "ERROR_ROLE";//登陆超时
	}
	
	public static class ServiceErrorCode {
		private ServiceErrorCode(){}
		public static final String BASE_CODE = "[SERVICE]";//前缀
		public static final String RESOURCE_NOT_FOUND = BASE_CODE + "RESOURCE_NOT_FOUND";//前缀
		/**
		 * 验证码错误
		 */
		public static final String CAPTCHA_ERROR = BASE_CODE + "CAPTCHA_ERROR";
		public static final String OBJECT_NOT_FOUND = BASE_CODE+"OBJECT_NOT_FOUND";//找不到对象
		public static final String SEND_SMS_ERROR = BASE_CODE+"SEND_SMS_ERROR";//发送短信错误
	}
	
	public static class UplaodErrorCode {
		private UplaodErrorCode(){}
		public static final String BASE_CODE = "[UPLOAD]";//前缀
		public static final String NOT_ALLOW_FILE = BASE_CODE+"NOT_ALLOW_FILE";//不允许的上传此类文件
	}
	
	public static class CommandLineErrorCode {
		private CommandLineErrorCode(){}
		public static final String BASE_CODE = "[COMMAND]";//前缀
		public static final String COMMAND_NOT_FOUND = BASE_CODE+"COMMAND_NOT_FOUND";//错误的指令
		public static final String COMMAND_STOP = BASE_CODE+"COMMAND_STOP";//错误的指令
	}
	
	public static class OtherErrorCode {
		private OtherErrorCode(){}
		public static final String BASE_CODE = "[OTHER]";//前缀
	}
	

	/*************
	 * all are deprecated
	 */
//	@Deprecated
	/*public String ERROR_SERVICE = "100";
	public String ERROR_BUSINESS = ERROR_SERVICE;
	public String ERROR_WEB = "200";*/
	
	
	/***
	 * 登陆错误
	 */
	/*public String ES_LOGIN_FAILED = "100001";
	public String ES_USER_NOT_FOUND = "100001001";//找不到此用户
	public String ES_ERROR_PASSWORD = "100001005";//错误的密码
	public String ES_LOGIN_TIMEOUT  = "100001010";//登陆超时
	public String ES_NO_TOKEN  = "100001015";//没有找到此令牌的用户，请先登陆
	public String ES_USER_STATE_ERROR ="100001020";//用户状态不正常
*/	/*
	
	*//***
	 * 验证错误
	 *//*
	public String ES_AUTH_ERROR  = "100002";//验证失败
	public String ES_AUTH_NO_LOGIN  = "200002001";//没有登陆
	
	*//**
	 * 权限验证
	 *//*
	public String PERMISSION_DENY = "permission.deny"; //没有操作权限
	
	*//**
	 * 机构名称
	 *//*
	public String UC_ORGAN_NAME="uc_organname_usercode";
	
	*//**
	 * 用户账号
	 *//*
	public String UC_USER_CODE="uc_user_code";
	
	*//**
	 * 验证码错误
	 *//*
	public String UC_CAPTCHA = "uc_captcha";
	
	*//**
	 * 两次输入密码不一致
	 *//*
	public String UC_CONFIRM_PASSWORD="uc_confirm_password";*/
	
}
