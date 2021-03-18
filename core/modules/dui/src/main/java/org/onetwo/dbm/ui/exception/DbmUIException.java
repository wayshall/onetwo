package org.onetwo.dbm.ui.exception;

import org.onetwo.dbm.exception.DbmException;

@SuppressWarnings("serial")
public class DbmUIException extends DbmException {

	public DbmUIException(String msg){
		super(msg);
	}
	
	public DbmUIException(Throwable cause){
		super(DefaultMsg, cause);
	}
	
	public DbmUIException(String msg, Throwable cause){
		super(msg, cause);
	}
}
