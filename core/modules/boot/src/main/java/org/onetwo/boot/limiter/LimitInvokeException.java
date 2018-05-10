package org.onetwo.boot.limiter;

import org.onetwo.common.exception.BaseException;

/*********
 * 
 * @author wayshall
 *
 */
@SuppressWarnings("serial")
public class LimitInvokeException extends BaseException {
	public static final String BASE_CODE = "[LimitInvoke]";//前缀

	protected int maxLimit = -1;


	public LimitInvokeException(int maxLimit) {
		super("exceed max limit invoke: " + maxLimit);
		this.maxLimit = 3;
	}
	
	public LimitInvokeException(int maxLimit, String message) {
		super(message);
		this.maxLimit = maxLimit;
	}


	public LimitInvokeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	@Override
	public String toString() {
		return "LimitInvokeException [" + maxLimit + ", " + getMessage() +  "]";
	}

}
