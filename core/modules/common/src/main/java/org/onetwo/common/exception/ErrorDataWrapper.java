package org.onetwo.common.exception;

import java.io.Serializable;

/**
 * 用于包装抛出异常时，同时需要把数据返回给客户端的情况
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
public class ErrorDataWrapper implements Serializable {
	
	public static final ErrorDataWrapper errorData(Object data) {
		return new ErrorDataWrapper(data);
	}
	
	final private Object data;

	public ErrorDataWrapper(Object data) {
		super();
		this.data = data;
	}

	public Object getData() {
		return data;
	}

}
