package org.onetwo.common.spring.rest;


/**
 * @author wayshall
 * <br/>
 */
public interface ExtRestErrorHandler<T> {
	
	void onError(T errorData);

}
