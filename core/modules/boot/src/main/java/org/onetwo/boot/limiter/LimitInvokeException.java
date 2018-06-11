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

	private String key;
	private int maxLimit = -1;


	public LimitInvokeException(String key, int maxLimit) {
		super("limiter["+key+"]: exceed max limit.");
		this.maxLimit = maxLimit;
		this.key = key;
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
		return "LimitInvokeException [key=" + key + ", maxLimit=" + maxLimit
				+ "]";
	}

}
