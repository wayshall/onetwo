package org.onetwo.boot.core.web.controller;

import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.result.AbstractDataResult;
import org.springframework.web.servlet.ModelAndView;

abstract public class AbstractResultBuilder<T, B extends AbstractResultBuilder<T, B>> {
	
	protected String code = AbstractDataResult.SUCCESS;
	protected String message;
//	final protected Class<?> builderClass;
//	private T data;
	
	public AbstractResultBuilder() {
		super();
//		this.builderClass = builderClass;
	}

	public B success(){
		return success(null);
	}
	
	public B success(String message){
		return code(AbstractDataResult.SUCCESS, message);
	}
	

	public B error(){
		return error(null);
	}
	public B error(String message){
		return code(AbstractDataResult.ERROR, message);
	}
	
	/****
	 * 如果表示错误，以 {@linkplain AbstractDataResult#ERROR ERROR} 为前缀
	 * @param code
	 * @param message
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public B code(String code, String message){
		this.code = code;
		this.message = message;
//		return builderClass.cast(this);
		return (B) this;
	}
	public B code(Enum<?> code, String message){
		return code(code.name(), message);
	}
	@SuppressWarnings("unchecked")
	public B code(String code){
		this.code = code;
		return (B) this;
	}
	@SuppressWarnings("unchecked")
	public B code(Enum<?> code){
		this.code = code.name();
		this.message = code.toString();
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
