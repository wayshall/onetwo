package org.onetwo.boot.mq.interceptor;

public interface SendMessageInterceptor {
	
	Object intercept(SendMessageInterceptorChain chain);

	/***
	 *  拦截器断言，只有通过拦截器断言测试的拦截器，才会执行
	 * @author way
	 *
	 */
	public interface InterceptorPredicate {
		boolean isApply(SendMessageInterceptor inter);
		/***
		 * 将要添加到拦截器链的拦截器
		 * @author wayshall
		 * @return
		 
		default List<SendMessageInterceptor> getIncreasingInterceptors(){
			return Collections.emptyList();
		}*/
	}
}
