package org.onetwo.ext.security.exception;

import java.util.stream.Stream;

import org.onetwo.common.exception.ErrorType;

/**
 * @author wayshall
 * <br/>
 */
public enum SecurityErrors implements ErrorType {
	AUTH_FAILED("认证失败"),
//	NOT_AUTHED("未认证的用户"),
	ACCESS_DENIED("未授权，访问拒绝"),
	CM_NOT_LOGIN("请先登录！");//包括匿名和rememberMe的用户
	;

	final private String message;

	private SecurityErrors(String message) {
		this.message = message;
	}

	@Override
	public String getErrorCode() {
		return name();
	}

	public String getErrorMessage() {
		return message;
	}
	public static SecurityErrors of(String status){
		return Stream.of(values()).filter(s->s.name().equalsIgnoreCase(status))
									.findAny()
									.orElseThrow(()->new IllegalArgumentException("error status: " + status));
	}
	
}
