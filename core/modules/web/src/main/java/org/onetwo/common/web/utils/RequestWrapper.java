package org.onetwo.common.web.utils;

import javax.servlet.ServletRequest;

public interface RequestWrapper {
	
	public static Object unwrapRequest(Object request){
		if(request instanceof RequestWrapper){
			return ((RequestWrapper)request).getNativeRequest();
		}
		return request;
	}
	
	ServletRequest getNativeRequest();

}
