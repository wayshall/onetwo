package org.onetwo.common.result;

import java.util.Arrays;
import java.util.List;

import org.onetwo.common.utils.Assert;





@SuppressWarnings("serial")
abstract public class AbstractDataResult<T> implements Result<Integer, T>{
	

	public static final int SUCCEED = 1;
	public static final int FAILED = 0;
	private int code = 1;//0,1;
	private String message;//
	
	public AbstractDataResult(){
		this.code = SUCCEED;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess(){
		if(code==SUCCEED)
			return true;
		else
			return false;
	}
	

	public static class LazyResult extends AbstractDataResult<Object> {


		public static LazyResult createSucceed(String message, LazyValue obj){
			return create(SUCCEED, message, obj);
		}
		public static LazyResult create(int code, String message, LazyValue obj){
			LazyResult result = new LazyResult(obj);
			result.setCode(code);
			result.setMessage(message);
			result.setData(obj);
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

		public void setData(Object data) {
			this.data = data;
		}
	}
	
	public static class SimpleDataResult<T> extends AbstractDataResult<T> {
		public static <E> SimpleDataResult<E> createFailed(String message){
			return create(FAILED, message, null);
		}

		public static <E> SimpleDataResult<E> createSucceed(String message, E obj){
			return create(SUCCEED, message, obj);
		}

		public static <E> SimpleDataResult<E> create(int code, String message, E obj){
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
	
	public static class ListDataResult<T> extends AbstractDataResult<List<T>>{

		public static <E> ListDataResult<E> createFailed(String message){
			return create(FAILED, message);
		}

		@SafeVarargs
		public static <E> ListDataResult<E> createSucceed(E...objects){
			return create(SUCCEED, "SUCCEED", objects);
		}
		
		@SafeVarargs
		public static <E> ListDataResult<E> create(int code, String message, E...objects){
			ListDataResult<E> result = new ListDataResult<>();
			result.setCode(code);
			result.setMessage(message);
			result.setData(Arrays.asList(objects));
			return result;
		}
		
		private List<T> data;
		

		public List<T> getData() {
			return data;
		}

		public void setData(List<T> data) {
			this.data = data;
		}
		
	}
}
