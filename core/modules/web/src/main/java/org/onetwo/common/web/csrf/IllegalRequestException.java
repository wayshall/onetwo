package org.onetwo.common.web.csrf;

import org.onetwo.common.exception.ServiceException;

@SuppressWarnings("serial")
public class IllegalRequestException extends ServiceException{

	public IllegalRequestException() {
		super("非法请求！");
	}
}
