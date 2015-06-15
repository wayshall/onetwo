package org.onetwo.common.jackson.exception;

import org.onetwo.common.exception.BaseException;

/*********
 * 
 * @author wayshall
 *
 */
public class JsonException extends BaseException {

	

	public JsonException() {
		super();
	} 


	public JsonException(String msg, Throwable cause) {
		super(msg, cause);
	}


	public JsonException(String msg) {
		super(msg);
	}

}
