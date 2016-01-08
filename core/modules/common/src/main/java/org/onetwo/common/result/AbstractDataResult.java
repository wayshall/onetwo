package org.onetwo.common.result;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;





@SuppressWarnings("serial")
abstract public class AbstractDataResult<T> implements Result<String, T>{
	public static final String SUCCESS = "SUCCESS";
	
	public static final String ERROR = "ERROR";
	public static final String ERR = "ERR";
	public static final String EXCEPTION_POSTFIX = "Exception";
	
	private String code = SUCCESS;//0,1;
	private String message;//
	/***
	 * 
	面向某些领域时，指示客户端是否只提取result里的data作为返回结果
	 */
	private boolean extractableData = false;
	
	public AbstractDataResult(){
		this.code = SUCCESS;
	}

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

	public boolean isSuccess(){
		return !isError();
	}

	public boolean isError(){
		if(code!=null){
			return code.endsWith(EXCEPTION_POSTFIX) || 
					code.toUpperCase().startsWith(ERR) || 
					code.toUpperCase().startsWith(ERROR);
		}else{
			return false;
		}
	}
	
	public boolean isExtractableData() {
		return extractableData;
	}

	/***
	 * 指示客户端是否只提取result里的data作为返回结果
	 * @param extractableData
	 */
	public void setExtractableData(boolean extractableData) {
		this.extractableData = extractableData;
	}

	public boolean isMessageOnly() {
		return StringUtils.isNotBlank(message) && getData()==null;
	}


	public static class LazyResult extends AbstractDataResult<Object> {


		public static LazyResult success(String message, LazyValue obj){
			return create(SUCCESS, message, obj);
		}
		public static LazyResult create(String code, String message, LazyValue obj){
			LazyResult result = new LazyResult(obj);
			result.setCode(code);
			result.setMessage(message);
			return result;
		}
		
		private LazyValue lazyValue;
		private Object data;

		private LazyResult(LazyValue data) {
			super();
			Assert.notNull(data);
			this.lazyValue = data;
		}

		public Object getData() {
			if(data==null){
				data = lazyValue.lazyGet();
			}
			return data;
		}

	}
	
	public static class SimpleDataResult<T> extends AbstractDataResult<T> {
		public static <E> SimpleDataResult<E> error(String message){
			return create(ERROR, message, null);
		}

		public static <E> SimpleDataResult<E> success(String message, E obj){
			return create(SUCCESS, message, obj);
		}

		public static <E> SimpleDataResult<E> create(String code, String message, E obj){
			SimpleDataResult<E> result = new SimpleDataResult<>();
			result.setCode(code);
			result.setMessage(message);
			result.setData(obj);
			return result;
		}
		
		private T data;
		
		private SimpleDataResult() {
			super();
		}

		private SimpleDataResult(T data) {
			super();
			this.data = data;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}
	}
}
