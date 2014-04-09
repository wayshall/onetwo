package org.onetwo.common.utils.commandline;


public class CommandStopException extends CommandLineException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 317687896561703415L;
	public static final String DEFAULT_MESSAGE = "退出命令";
	
	public CommandStopException() {
		super(DEFAULT_MESSAGE);
	}

	public CommandStopException(String message) {
		super(message);
	}

	public CommandStopException(String message, String code) {
		super(message, code);
	}

	public CommandStopException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

	protected String getDefaultCode(){
		return CommandLineErrorCode.COMMAND_STOP;
	}
}
