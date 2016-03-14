package org.onetwo.common.lexer;

import org.onetwo.common.exception.BaseException;

/*********
 * 
 * @author wayshall
 *
 */
public class JSyntaxException extends BaseException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6015512673900176117L;


	public JSyntaxException() {
		super("syntax error");
	} 
	public JSyntaxException(int lineNumber) {
		super("syntax error at line " + lineNumber);
	} 

	public JSyntaxException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}


	public JSyntaxException(String msg, Throwable cause) {
		super(msg, cause);
	}


	public JSyntaxException(String msg) {
		super(msg);
	}

	public JSyntaxException(Throwable cause) {
		super(cause);
	}
	
}
