package org.onetwo.common.jsonrpc.protocol;

import org.onetwo.common.jsonrpc.exception.JsonRpcException;

public class DispatchableRequest {
	
	final private JsonRpcParamsRequest rpcRequest;
	final private String serviceClass;
	final private String serviceMethod;

	public DispatchableRequest(JsonRpcParamsRequest rpcRequest) {
		super();
		this.rpcRequest = rpcRequest;
		int lastDot = rpcRequest.getMethod().lastIndexOf('.');
		if(lastDot!=-1){
			serviceClass = rpcRequest.getMethod().substring(0, lastDot);
			serviceMethod = rpcRequest.getMethod().substring(lastDot+1);
		}else{
			throw new JsonRpcException("error method name: " + rpcRequest.getMethod());
		}
	}

	public JsonRpcParamsRequest getRpcRequest() {
		return rpcRequest;
	}

	public String getServiceClass() {
		return serviceClass;
	}

	public String getServiceMethod() {
		return serviceMethod;
	}
	

}
