package org.onetwo.common.spring.rest;

import org.springframework.http.ResponseEntity;

/**
 * @author wayshall
 * <br/>
 */
public interface ExtRestErrorHandler<T> {
	
	void onError(T errorData);

}
