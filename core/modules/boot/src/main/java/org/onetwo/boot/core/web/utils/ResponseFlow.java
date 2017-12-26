package org.onetwo.boot.core.web.utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.mvc.utils.DataWrapper;
import org.onetwo.common.web.utils.ResponseType;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;


public class ResponseFlow {

	public interface ResponseAction {
		ModelAndView execute();
	}

	private List<ResponseType> responseTypes;
	private ResponseAction action;
	
	public ResponseFlow(ResponseType... responseTypes) {
		super();
		Assert.notEmpty(responseTypes);
		this.responseTypes = Arrays.asList(responseTypes);
	}

	/*protected ModelAndView responseData(Object value){
		return BootWebUtils.mv("", DataWrapper.wrap(value));
	}*/

	public ResponseFlow onPage(ResponseAction action){
		return on(action, ResponseType.PAGE);
	}

	/***
	 * 如果需要返回的是json等数据形式
	 * @param action
	 * @return
	 */
	public ResponseFlow onJson(Supplier<Object> action){
		return on(()->{
			Object result = action.get();
			if(ModelAndView.class.isInstance(result)){
				return (ModelAndView)result;
			}
			return BootWebUtils.createModelAndView("", DataWrapper.wrap(result));
		}, ResponseType.JSON);
	}

	public ResponseFlow on(ResponseAction action, ResponseType...anyOfTypes){
		Stream.of(anyOfTypes).filter(type->responseTypes.contains(type))
							.findAny().ifPresent(type->ResponseFlow.this.action = action);
		return this;
	}
	
	public ModelAndView execute(){
		if(action==null){
			throw new BaseException("no response action!");
		}
		return action.execute();
	}
	
}
