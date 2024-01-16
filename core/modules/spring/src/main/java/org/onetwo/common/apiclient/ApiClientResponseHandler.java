package org.onetwo.common.apiclient;

import java.lang.reflect.Type;

import org.springframework.http.ResponseEntity;

/**
 * @author wayshall
 * <br/>
 */
public interface ApiClientResponseHandler<M extends ApiClientMethod> /*extends ApiErrorHandler*/ {
	
	Type getActualResponseType(M invokeMethod);
	
	Object handleResponse(M invokeMethod, ResponseEntity<?> responseEntity, RequestContextData context);
	
//	Object handleResponse(M invokeMethod, ResponseEntity<?> responseEntity, Class<?> actualResponseType);

}
