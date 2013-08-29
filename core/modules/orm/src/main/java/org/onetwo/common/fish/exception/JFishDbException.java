package org.onetwo.common.fish.exception;

import org.onetwo.common.exception.BaseException;

@SuppressWarnings("serial")
public class JFishDbException extends BaseException{

	public JFishDbException() {
		super();
	}

	public JFishDbException(String msg, String code) {
		super(msg, code);
	}

	public JFishDbException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}

	public JFishDbException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public JFishDbException(String msg) {
		super(msg);
	}

	public JFishDbException(Throwable cause, String code) {
		super(cause, code);
	}

	public JFishDbException(Throwable cause) {
		super(cause);
	}
	
	protected String getDefaultCode(){
		return JFishErrorCode.DB_ERROR;
	}

}
