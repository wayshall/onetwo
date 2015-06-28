package org.onetwo.common.spring.plugin;

import org.onetwo.common.exception.BaseException;

@SuppressWarnings("serial")
public class ContextPluginException extends BaseException{

	public ContextPluginException() {
		super();
	}

	public ContextPluginException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}

	public ContextPluginException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ContextPluginException(String msg) {
		super(msg);
	}

	public ContextPluginException(Throwable cause) {
		super(cause);
	}
	
	protected String getDefaultCode(){
		return "[ContextPluginException]";
	}

}
