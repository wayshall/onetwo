package org.example.utils;

import org.onetwo.common.exception.SystemErrorCode;

public final class ErrorCodes {
	/*****
	 * 默认的系统错误代码
	 */
	public static final String SYSTEM_ERROR = SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
	
	/****
	 * 客服模块错误代码
	 * @author weishao
	 *
	 */
	public static final class CustomerServiceErrors {
		/****
		 * 模块代码前缀
		 */
		public static final String BASE_CODE = "CS_";
		
		/****
		 * 自定义的错误代码
		 */
		public static final String ALREADY_REPORT_LOSS = BASE_CODE + "ALREADY_REPORT_LOSS";
	}

	private ErrorCodes(){
	}

}
