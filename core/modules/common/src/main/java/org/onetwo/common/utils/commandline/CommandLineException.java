package org.onetwo.common.utils.commandline;

import org.onetwo.common.exception.ServiceException;

public class CommandLineException extends ServiceException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1033620239530683772L;
	public static final String DEFAULT_MESSAGE = "错误的指令";
	
	public CommandLineException() {
		super(DEFAULT_MESSAGE);
	}

	public CommandLineException(String message) {
		super(message);
	}

	public CommandLineException(String message, String code) {
		super(message, code);
	}

	public CommandLineException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}
	
	public CommandLineException(String msg, Throwable cause) {
		super(msg, cause);
	}

	protected String getDefaultCode(){
		return CommandLineErrorCode.BASE_CODE;
	}
}
