package org.onetwo.boot.core.web.utils;

import org.onetwo.common.exception.ErrorType;

import lombok.AllArgsConstructor;

/**
 * @author weishao zeng
 * <br/>
 */
@AllArgsConstructor
public enum WebErrors implements ErrorType {
	UPLOAD_FILE_SIZE_LIMIT_EXCEEDED("单个文件上传超过了限制"),
	UPLOAD("上传错误");

	private final String message;
	@Override
	public String getErrorCode() {
		return name();
	}

	@Override
	public String getErrorMessage() {
		return message;
	}

	public Integer getStatusCode(){
		return 200;
	};
}

