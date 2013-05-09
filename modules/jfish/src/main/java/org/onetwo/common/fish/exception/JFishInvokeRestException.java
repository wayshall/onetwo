package org.onetwo.common.fish.exception;

import org.onetwo.common.exception.ServiceException;

@SuppressWarnings("serial")
public class JFishInvokeRestException extends ServiceException{

	public JFishInvokeRestException() {
		super("jfish invoke rest error!");
	}

	public JFishInvokeRestException(String msg, String code) {
		super(msg, code);
	}

	public JFishInvokeRestException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}

	public JFishInvokeRestException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public JFishInvokeRestException(String msg) {
		super(msg);
	}

	public JFishInvokeRestException(Throwable cause, String code) {
		super(cause, code);
	}

	public JFishInvokeRestException(Throwable cause) {
		super(cause);
	}
	
	protected String getBaseCode(){
		return JFishErrorCode.REST_INVOKE_ERROR;
	}

}
