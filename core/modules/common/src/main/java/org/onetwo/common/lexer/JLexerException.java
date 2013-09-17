package org.onetwo.common.lexer;

import org.onetwo.common.exception.BaseException;

/*********
 * 
 * @author wayshall
 *
 */
public class JLexerException extends BaseException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3108469861794608267L;


	public JLexerException() {
		super("lexer error");
	} 


	public JLexerException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}


	public JLexerException(String msg, Throwable cause) {
		super(msg, cause);
	}


	public JLexerException(String msg) {
		super(msg);
	}
	

	public JLexerException(Throwable cause) {
		super(cause);
	}
	
}
