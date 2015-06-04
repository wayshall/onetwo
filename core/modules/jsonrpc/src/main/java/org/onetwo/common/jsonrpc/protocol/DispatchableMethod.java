package org.onetwo.common.jsonrpc.protocol;

import org.onetwo.common.jsonrpc.exception.JsonRpcException;

public class DispatchableMethod {
	
//	final private JsonRpcParamsRequest rpcRequest;
	final private String serviceClassName;
	final private String serviceMethodName;

	public DispatchableMethod(String method) {
		super();
//		this.rpcRequest = rpcRequest;
		int lastDot = method.lastIndexOf('.');
		if(lastDot!=-1){
			serviceClassName = method.substring(0, lastDot);
			serviceMethodName = method.substring(lastDot+1);
		}else{
			throw new JsonRpcException("error method name: " + method);
		}
	}

	public String getServiceClassName() {
		return serviceClassName;
	}

	public String getServiceMethodName() {
		return serviceMethodName;
	}

	@Override
	public String toString() {
		return "DispatchableMethod [serviceClassName=" + serviceClassName
				+ ", serviceMethodName=" + serviceMethodName + "]";
	}

}
