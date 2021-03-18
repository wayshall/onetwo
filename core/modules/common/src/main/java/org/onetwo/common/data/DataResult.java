package org.onetwo.common.data;

import org.onetwo.common.exception.ServiceException;

public interface DataResult<DATA> extends Result {
	String SUCCESS = "SUCCESS";

	public DATA getData();
//	public DATA dataOrThrows();
	
	default public DATA dataOrThrows(){
		if(isSuccess()){
			return getData();
		}
		throw new ServiceException(getMessage(), getCode());
	}
	
	/****
	 * 本次操作请求是否成功(没有发生任何异常)
	 * @return
	 */
	default public boolean isSuccess(){
		String code = getCode();
		return SUCCESS.equalsIgnoreCase(code);
	}
}
