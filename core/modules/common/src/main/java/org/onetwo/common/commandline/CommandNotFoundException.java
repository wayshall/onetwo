package org.onetwo.common.commandline;


public class CommandNotFoundException extends CommandLineException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6117722707298643778L;
	/**
	 * 
	 */
	public static final String DEFAULT_MESSAGE = "错误的指令";
	
	public CommandNotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	public CommandNotFoundException(String message) {
		super(message);
	}

	public CommandNotFoundException(String message, String code) {
		super(message, code);
	}

	public CommandNotFoundException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

	protected String getDefaultCode(){
		return CommandLineErrorCode.COMMAND_NOT_FOUND;
	}
}
