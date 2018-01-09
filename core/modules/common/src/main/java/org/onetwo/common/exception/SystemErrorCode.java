package org.onetwo.common.exception;

import java.util.Collections;
import java.util.Map;

public interface SystemErrorCode {
	
	default Map<String, Object> getErrorContext(){
		return Collections.emptyMap();
	}
	
//	public String APP_ERROR_MESSAGE = "appErrorMessage";
	public String DEFAULT_SYSTEM_ERROR_CODE = "SYSTEM_ERROR";
	public String UNKNOWN = "UNKNOWN";
	public String ERR_PARAMETER_VALIDATE = "PARAMETER_VALIDATE_ERROR";
	public String ERR_PARAMETER_CONVERT = "PARAMETER_CONVERT_ERROR";
	
	
	interface UplaodErrorCode {
		String BASE_CODE = "UPLOAD_";//前缀
		String NOT_ALLOW_FILE = BASE_CODE+"NOT_ALLOW_FILE";//不允许的上传此类文件
	}
	
	interface CommandLineErrorCode {
		String BASE_CODE = "COMMAND_";//前缀
		String COMMAND_NOT_FOUND = BASE_CODE+"COMMAND_NOT_FOUND";//错误的指令
		String COMMAND_STOP = BASE_CODE+"COMMAND_STOP";//错误的指令
	}
	
}
