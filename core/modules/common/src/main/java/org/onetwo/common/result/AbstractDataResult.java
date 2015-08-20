package org.onetwo.common.result;





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
		
		public SimpleDataResult() {
			super();
		}

		public SimpleDataResult(T data) {
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
