package org.onetwo.common.jfishdbm.exception;

import org.onetwo.common.exception.BaseException;

@SuppressWarnings("serial")
public class JFishOrmException extends BaseException{

	public JFishOrmException() {
		super("jfish orm error!");
	}

	public JFishOrmException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}

	public JFishOrmException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public JFishOrmException(String msg) {
		super(msg);
	}

	public JFishOrmException(Throwable cause) {
		super(cause);
	}
	
	protected String getDefaultCode(){
		return JFishErrorCode.ORM_ERROR;
	}

}
