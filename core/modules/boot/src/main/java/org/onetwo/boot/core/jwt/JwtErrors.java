package org.onetwo.boot.core.jwt;

import java.util.stream.Stream;

import org.onetwo.common.exception.ErrorType;

/**
 * @author wayshall
 * <br/>
 */
public enum JwtErrors implements ErrorType {
	CM_NOT_LOGIN_ANONYMOUS("当前是游客，请先登录"),
	
	CM_ERROR_TOKEN("token无效！"),
	CM_NOT_LOGIN("请先登录！"),
	CM_SESSION_EXPIREATION("登录已超时！"),
	
	//未定义的登录错误
	CM_NOT_LOGIN_UNKNOW("登录出错！");

	final private String message;

	private JwtErrors(String message) {
		this.message = message;
	}

	@Override
	public String getErrorCode() {
		return name();
	}

	public String getErrorMessage() {
		return message;
	}
	public static JwtErrors of(String status){
		return Stream.of(values()).filter(s->s.name().equalsIgnoreCase(status))
									.findAny()
									.orElseThrow(()->new IllegalArgumentException("error status: " + status));
	}
	
}
