package org.onetwo.dbm.exception;


@SuppressWarnings("serial")
public class NotImplementedDbmOperationException extends DbmException {

	private static final String DefaultMsg = "not implemented yet!";
	
	public NotImplementedDbmOperationException(){
		super(DefaultMsg);
	}
	
	public NotImplementedDbmOperationException(String msg){
		super(msg);
	}
	
	public NotImplementedDbmOperationException(Throwable cause){
		super(DefaultMsg, cause);
	}
	
	public NotImplementedDbmOperationException(String msg, Throwable cause){
		super(msg, cause);
	}
}
