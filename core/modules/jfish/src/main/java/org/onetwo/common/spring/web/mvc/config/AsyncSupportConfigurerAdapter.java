package org.onetwo.common.spring.web.mvc.config;

import java.util.List;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;

public class AsyncSupportConfigurerAdapter extends AsyncSupportConfigurer {

	@Override
	public AsyncTaskExecutor getTaskExecutor() {
		return super.getTaskExecutor();
	}

	@Override
	public Long getTimeout() {
		return super.getTimeout();
	}

	@Override
	public List<CallableProcessingInterceptor> getCallableInterceptors() {
		return super.getCallableInterceptors();
	}

	@Override
	public List<DeferredResultProcessingInterceptor> getDeferredResultInterceptors() {
		return super.getDeferredResultInterceptors();
	}

}
