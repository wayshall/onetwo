package org.onetwo.boot.mq;

import java.util.Collections;
import java.util.List;

public interface SendMessageInterceptor {
	
	Object intercept(SendMessageInterceptorChain chain);

	public interface InterceptorPredicate {
		boolean isApply(SendMessageInterceptor inter);
		/***
		 * 将要添加到拦截器链的拦截器
		 * @author wayshall
		 * @return
		 */
		default List<SendMessageInterceptor> getIncreasingInterceptors(){
			return Collections.emptyList();
		}
	}
}
