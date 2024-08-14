package org.onetwo.boot.core.web.utils;

import org.onetwo.common.exception.ErrorType;

import lombok.AllArgsConstructor;

/**
 * @author weishao zeng
 * <br/>
 */
@AllArgsConstructor
public enum WebErrors implements ErrorType {
	/***
	 * 特殊代码，用于后台出错，但前端不需要显示出错信息的场景
	 */
	SOUND_OF_SILENCE("我错了，但是我不会跟你说！"),
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

