package org.onetwo.common.jsonrpc.exception;

import org.onetwo.common.exception.BaseException;

@SuppressWarnings("serial")
public class JsonRpcException extends BaseException{
	
	public static final String JSONRPC_ERROR = "JSONRPC_ERROR";
	
	private JsonRpcError jsonRpcError;

	public JsonRpcException(JsonRpcError jsonRpcError) {
		super(jsonRpcError.getMeaning());
		this.jsonRpcError = jsonRpcError;
	}

	public JsonRpcException(JsonRpcError jsonRpcError, Throwable cause) {
		super(jsonRpcError.getMeaning(), cause);
	}
	
	public JsonRpcException(JsonRpcError jsonRpcError, String msg) {
		this(jsonRpcError, msg, null);
	}

	public JsonRpcException(JsonRpcError jsonRpcError, String msg, Throwable cause) {
		super(jsonRpcError.getMeaning()+" "+msg, cause, String.valueOf(jsonRpcError.getCode()));
		this.jsonRpcError = jsonRpcError;
	}
	
	protected String getDefaultCode(){
		return jsonRpcError==null?JSONRPC_ERROR:String.valueOf(jsonRpcError.getCode());
	}

	public JsonRpcError getJsonRpcError() {
		return jsonRpcError;
	}

}
