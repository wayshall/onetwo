package org.onetwo.common.jsonrpc.exception;

import org.onetwo.common.exception.BaseException;

@SuppressWarnings("serial")
public class JsonRpcException extends BaseException{
	
	public static final String JSONRPC_ERROR = "JSONRPC_ERROR";

	public JsonRpcException() {
		super();
	}

	public JsonRpcException(String msg, Throwable cause, String code) {
		super(msg, cause, code);
	}

	public JsonRpcException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public JsonRpcException(String msg) {
		super(msg);
	}

	public JsonRpcException(Throwable cause) {
		super(cause);
	}
	
	protected String getDefaultCode(){
		return JSONRPC_ERROR;
	}

}
