package org.onetwo.common.exception;

@Deprecated
public class WebException extends ServiceException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7280411050853219784L;

	public WebException() {
		super();
	}


	public WebException(String msg, String code) {
		super(msg, code);
	}


	public WebException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}


	public WebException(String msg, Throwable cause) {
		super(msg, cause);
	}


	public WebException(String msg) {
		super(msg);
	}


	public WebException(Throwable cause, String code) {
		super(cause, code);
	}


	public WebException(Throwable cause) {
		super(cause);
	}
	

}
