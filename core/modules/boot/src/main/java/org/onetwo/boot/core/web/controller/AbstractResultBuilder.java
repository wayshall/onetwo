package org.onetwo.boot.core.web.controller;

import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.result.AbstractDataResult;
import org.springframework.web.servlet.ModelAndView;

abstract public class AbstractResultBuilder<T, B extends AbstractResultBuilder<T, B>> {
	
	protected int code;
	protected String message;
//	final protected Class<?> builderClass;
//	private T data;
	
	public AbstractResultBuilder() {
		super();
//		this.builderClass = builderClass;
	}

	public B succeed(){
		return succeed(null);
	}
	
	public B succeed(String message){
		return code(AbstractDataResult.SUCCEED, message);
	}
	

	public B failed(){
		return failed(null);
	}
	public B failed(String message){
		return code(AbstractDataResult.FAILED, message);
	}
	
	@SuppressWarnings("unchecked")
	public B code(int code, String message){
		this.code = code;
		this.message = message;
//		return builderClass.cast(this);
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B message(String message){
		this.message = message;
//		return builderClass.cast(this);
		return (B) this;
	}

	abstract public T buildResult();

	public ModelAndView buildModelAndView(){
		T rs = buildResult();
		return BootWebUtils.createModelAndView("", rs);
	}
	
	/*public SimpleDataResult<?> buildResult(){
		return SimpleDataResult.create(code, message, null);
	}*/
	
	/*public SimpleDataResult<?> buildSimpleResult(Object data){
		return SimpleDataResult.create(code, message, data);
	}
	
	public MapResult buildMapResult(Object...datas){
		return MapResult.create(code, message, datas);
	}
	
	public LazyResult buildLazyResult(LazyValue lazyValue){
		return LazyResult.create(code, message, lazyValue);
	}*/

}
