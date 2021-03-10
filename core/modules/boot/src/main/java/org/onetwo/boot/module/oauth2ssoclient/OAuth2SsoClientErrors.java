package org.onetwo.boot.module.oauth2ssoclient;

import org.onetwo.common.exception.ErrorType;

/**
 * @author wayshall
 * <br/>
 */
public enum OAuth2SsoClientErrors implements ErrorType {
	OAUTH2_NOT_AUTHORIZE("请先通过授权！"),
	OAUTH2_REJECTED("用户拒绝授权！"),
	OAUTH2_ERROR_IN_BROWSER("请使用微信！"),
	OAUTH2_STATE_ERROR("state参数错误"),
	OAUTH2_REDIRECT_URL_BLANK("redirect url must not be blank"),
	
	;
	
	private String errorMessage;

	private OAuth2SsoClientErrors(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String getErrorCode() {
		return name();
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
	
}
