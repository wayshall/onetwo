package org.onetwo.common.data;

import java.util.HashSet;
import java.util.Set;

import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;





@SuppressWarnings("serial")
abstract public class AbstractDataResult<T> implements Result<String, T>{
	public static final String SUCCESS = "SUCCESS";
	
	private static final Set<String> SUCESS_VALUES = new HashSet<String>();
	public static void addSucessValues(String value) {
		if(value==null){
			return ;
		}
		SUCESS_VALUES.add(value);
	}
	static {
		addSucessValues(SUCCESS);
		addSucessValues("0");
		addSucessValues("1");
	}
	
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

	/****
	 * 本次操作请求是否成功(没有发生任何异常)
	 * @return
	 */
	public boolean isSuccess(){
		if(code!=null){
			return SUCESS_VALUES.contains(code);
		}else{
			return true;
		}
	}

	public boolean isError(){
		return !isSuccess();
		/*if(code!=null){
			return code.endsWith(EXCEPTION_POSTFIX) || 
					code.toUpperCase().startsWith(ERR) || 
					code.toUpperCase().startsWith(ERROR);
		}else{
			return false;
		}*/
	}
	/****
	 * 
	 * 指示客户端是否只提取result里的data作为返回结果
	 * 客户端用拦截的方式统一处理时需要用到，客户端拦截器可以根据这个提示获取data内容作为下层ui组件数据的输入
	 * @author wayshall
	 * @return
	 */
	public boolean isExtractableData() {
		return extractableData;
	}

	/***
	 * 
	 * 指示客户端是否只提取result里的data作为返回结果
	 * 客户端用拦截的方式统一处理时需要用到，客户端拦截器可以根据这个提示获取data内容作为下层ui组件数据的输入
	 * @author wayshall
	 * @param extractableData
	 */
	public void setExtractableData(boolean extractableData) {
		this.extractableData = extractableData;
	}

	/****
	 * 指示客户端是否只显示message内容即可
	 * 客户端用拦截的方式统一处理时需要用到，客户端拦截器可以根据这个提示只弹出提示信息，无需继续传递结果给下层的ui
	 * @return
	 */
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
