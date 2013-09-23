package org.onetwo.common.fish.exception;

import org.onetwo.common.exception.BaseException;

@SuppressWarnings("serial")
public class JFishException extends BaseException{

	public JFishException() {
		super();
	}

	public JFishException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}

	public JFishException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public JFishException(String msg) {
		super(msg);
	}

	public JFishException(Throwable cause) {
		super(cause);
	}
	
	protected String getDefaultCode(){
		return JFishErrorCode.BASE_CODE;
	}

}
