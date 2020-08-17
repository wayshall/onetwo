package org.onetwo.common.data;
/**
 * @author weishao zeng
 * <br/>
 */
@SuppressWarnings("serial")
abstract public class BaseDataResult<DATA> implements DataResult<DATA>{
	
	String code;
	String message;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
