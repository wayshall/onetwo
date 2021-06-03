package org.onetwo.common.data;

import org.onetwo.common.exception.ServiceException;

public interface DataResult<DATA> extends Result {

	public DATA getData();
//	public DATA dataOrThrows();
	
	default public DATA dataOrThrows(){
		if(isSuccess()){
			return getData();
		}
		throw new ServiceException(getMessage(), getCode());
	}
	
}
