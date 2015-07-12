package org.onetwo.boot.core.web.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.onetwo.common.exception.BaseException;
import org.springframework.util.Assert;


public class ResponseFlow<T> {

	public interface ResponseAction<T> {
		T execute();
	}

	private List<ResponseType> responseTypes;
	private ResponseAction<T> action;
	
	public ResponseFlow(ResponseType... responseTypes) {
		super();
		Assert.notEmpty(responseTypes);
		this.responseTypes = Arrays.asList(responseTypes);
	}

	/*protected ModelAndView responseData(Object value){
		return BootWebUtils.mv("", DataWrapper.wrap(value));
	}*/

	public ResponseFlow<T> ifPage(ResponseAction<T> action){
		return ifResponse(action, ResponseType.PAGE);
	}

	/***
	 * 如果需要返回的是json等数据形式
	 * @param action
	 * @return
	 */
	public ResponseFlow<T> ifData(ResponseAction<T> action){
		return ifResponse(action, ResponseType.JSON);
	}

	public ResponseFlow<T> ifResponse(ResponseAction<T> action, ResponseType...anyOfTypes){
		Stream.of(anyOfTypes).filter(type->responseTypes.contains(type))
							.findAny().ifPresent(type->ResponseFlow.this.action = action);
		return this;
	}
	
	public T execute(){
		if(action==null){
			throw new BaseException("no response action!");
		}
		return action.execute();
	}
	
}
