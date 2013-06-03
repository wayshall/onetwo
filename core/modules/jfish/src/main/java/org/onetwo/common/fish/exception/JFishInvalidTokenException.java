package org.onetwo.common.fish.exception;

import org.onetwo.common.exception.BusinessException;

@SuppressWarnings("serial")
public class JFishInvalidTokenException extends BusinessException{

	public JFishInvalidTokenException() {
		super("请不要重复提交！");
	}

	public JFishInvalidTokenException(String msg, String code) {
		super(msg, code);
	}

	public JFishInvalidTokenException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}

	public JFishInvalidTokenException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public JFishInvalidTokenException(String msg) {
		super(msg);
	}

	public JFishInvalidTokenException(Throwable cause, String code) {
		super(cause, code);
	}

	public JFishInvalidTokenException(Throwable cause) {
		super(cause);
	}
	
	protected String getBaseCode(){
		return JFishErrorCode.INVALID_TOKEN_ERROR;
	}

}
