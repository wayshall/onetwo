package org.onetwo.common.apiclient;

import net.jodah.typetools.TypeResolver;

import org.springframework.http.ResponseEntity;

/**
 * 自定义响应处理器
 * @author wayshall
 * <br/>
 */
public interface CustomResponseHandler<T> {
	
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
