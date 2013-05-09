package org.onetwo.plugins.richmodel;

import org.onetwo.common.exception.BaseException;

@SuppressWarnings("serial")
public class NotImplementedYetException extends BaseException {

	private static final String DefaultMsg = "not implemented yet!";
	
	public NotImplementedYetException(){
		super(DefaultMsg);
	}
	
	public NotImplementedYetException(String msg){
		super(msg);
	}
	
	public NotImplementedYetException(Throwable cause){
		super(DefaultMsg, cause);
	}
	
	public NotImplementedYetException(String msg, Throwable cause){
		super(msg, cause);
	}
}
