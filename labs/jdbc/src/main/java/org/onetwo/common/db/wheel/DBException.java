package org.onetwo.common.db.wheel;

import org.onetwo.common.exception.BaseException;

@SuppressWarnings("serial")
public class DBException extends BaseException {

	private static final String DefaultMsg = "db error";
	
	public DBException(){
		super(DefaultMsg);
	}
	
	public DBException(String msg){
		super(msg);
	}
	
	public DBException(Throwable cause){
		super(DefaultMsg, cause);
	}
	
	public DBException(String msg, Throwable cause){
		super(msg, cause);
	}
}
