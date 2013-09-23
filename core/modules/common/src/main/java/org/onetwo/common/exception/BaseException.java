package org.onetwo.common.exception;

import java.io.Serializable;


@SuppressWarnings("serial")
public class BaseException extends RuntimeException implements SystemErrorCode, Serializable{

	protected static final String DefaultMsg = "occur error";
	public static final String Prefix = "[ERROR]:";

//	protected List<Throwable> list = null;

	public BaseException() {
		super(DefaultMsg);
	}

	public BaseException(String msg) {
		super(Prefix + msg);
	}

	public BaseException(Throwable cause) {
		super(DefaultMsg, cause);
	}


	public BaseException(String msg, Throwable cause) {
		super("[" + msg + "] : " + (cause==null?"":cause.getMessage()), cause);
	}

	public BaseException(String msg, Throwable cause, String code) {
		this(msg, cause);
	}

}
