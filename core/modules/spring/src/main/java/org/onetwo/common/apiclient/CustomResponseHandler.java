package org.onetwo.common.apiclient;

import org.onetwo.common.exception.ApiClientException;
import org.springframework.http.ResponseEntity;

import net.jodah.typetools.TypeResolver;

/**
 * 自定义响应处理器
 * @author wayshall
 * <br/>
 */
public interface CustomResponseHandler<T> /*extends ApiErrorHandler*/ {
	
	final public static class NullHandler implements CustomResponseHandler<Object> {
		public Class<Object> getResponseType() {
			throw new ApiClientException("you should not use this handler", "error");
		}
		@Override
		public Object handleResponse(ApiClientMethod apiMethod, ResponseEntity<Object> responseEntity) {
			throw new ApiClientException("you should not use this handler", "error");
		}
	};
	
	/***
	 * 指定restRemplate 抽取数据时的类型
	 * @author wayshall
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default Class<T> getResponseType() {
		Class<T> type = (Class<T>)TypeResolver.resolveRawArgument(CustomResponseHandler.class, getClass());
		return type;
	}
	
	/***
	 * 二次处理逻辑
	 * @author wayshall
	 * @param apiMethod
	 * @param responseEntity
	 * @return
	 */
	Object handleResponse(ApiClientMethod apiMethod, ResponseEntity<T> responseEntity);
	

}
