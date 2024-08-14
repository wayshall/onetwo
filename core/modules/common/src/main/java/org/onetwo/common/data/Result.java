package org.onetwo.common.data;

import java.io.Serializable;


public interface Result extends Serializable {
	String SUCCESS = "SUCCESS";
	
	public String getCode();
	public String getMessage();
	
	/****
	 * 本次操作请求是否成功(没有发生任何异常)
	 * @return
	 */
	default public boolean isSuccess(){
		String code = getCode();
		return SUCCESS.equalsIgnoreCase(code);
	}
}
