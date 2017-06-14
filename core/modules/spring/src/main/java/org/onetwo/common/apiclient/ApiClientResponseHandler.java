package org.onetwo.common.apiclient;

import org.springframework.http.ResponseEntity;

/**
 * @author wayshall
 * <br/>
 */
public interface ApiClientResponseHandler<M extends ApiClientMethod> {
	
	Class<?> getActualResponseType(M invokeMethod);
	Object handleResponse(M invokeMethod, ResponseEntity<?> responseEntity, Class<?> actualResponseType);

}
