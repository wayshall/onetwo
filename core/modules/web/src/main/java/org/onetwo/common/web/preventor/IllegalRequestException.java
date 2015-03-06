package org.onetwo.common.web.preventor;

import org.onetwo.common.exception.ServiceException;

@SuppressWarnings("serial")
public class IllegalRequestException extends ServiceException{

	public static final String ERROR_CODE = "ILLEGAL_REQUEST";

	public IllegalRequestException() {
		super("非法请求，或令牌已过时！请尝试重新进入页面。");
	}
	public IllegalRequestException(String msg) {
		super(msg);
	}

	public String getCode() {
		return super.getDefaultCode() + ERROR_CODE;
	}
}
