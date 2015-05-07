package org.onetwo.common.jfishdbm.exception;

import org.onetwo.common.exception.BaseException;

@SuppressWarnings("serial")
public class JFishDbException extends BaseException{

	public JFishDbException() {
		super();
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

	public JFishDbException(Throwable cause) {
		super(cause);
	}
	
	protected String getDefaultCode(){
		return JFishErrorCode.DB_ERROR;
	}

}
