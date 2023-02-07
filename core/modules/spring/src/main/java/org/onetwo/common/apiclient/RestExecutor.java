package org.onetwo.common.apiclient;

import org.springframework.http.ResponseEntity;

/**
 * @author wayshall
 * <br/>
 */
public interface RestExecutor {
	
//	String requestId();
	
	<T> ResponseEntity<T> execute(RequestContextData context);

}
