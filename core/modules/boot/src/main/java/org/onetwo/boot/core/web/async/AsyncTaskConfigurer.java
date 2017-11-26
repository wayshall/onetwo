package org.onetwo.boot.core.web.async;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.AsyncConfigurer;

/**
 * @author wayshall
 * <br/>
 */
public class AsyncTaskConfigurer implements AsyncConfigurer {
	
	private SimpleAsyncUncaughtExceptionHandler defaultUncaughtExceptionHandler = new SimpleAsyncUncaughtExceptionHandler();
	
	@Autowired(required=false)
	private AsyncUncaughtExceptionHandler asyncUncaughtExceptionHandler;
	@Autowired(required=false)
	@Qualifier(AsyncTaskConfiguration.ASYNC_TASK_BEAN_NAME)
	private Executor executor;

	@Override
	public Executor getAsyncExecutor() {
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return asyncUncaughtExceptionHandler==null?defaultUncaughtExceptionHandler:asyncUncaughtExceptionHandler;
	}

	
}
