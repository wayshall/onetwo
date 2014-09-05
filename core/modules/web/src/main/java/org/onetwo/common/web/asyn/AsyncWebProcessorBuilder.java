package org.onetwo.common.web.asyn;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.StringUtils;
import org.springframework.core.task.AsyncTaskExecutor;

public class AsyncWebProcessorBuilder {

	private static final String DEFAULT_ASYNCALLBACK = "parent.jfishAsynCallback";

	public static AsyncWebProcessorBuilder newBuilder(HttpServletResponse response){
		return new AsyncWebProcessorBuilder(response);
	}
	public static AsyncWebProcessorBuilder newBuilder(HttpServletResponse response, String jscallback){
		return new AsyncWebProcessorBuilder(response, jscallback);
	}
	public static AsyncWebProcessorBuilder newBuilder(HttpServletResponse response, HttpServletRequest request){
		return new AsyncWebProcessorBuilder(response);
	}
	
//	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private AsyncMessageTunnel<?> messageTunnel;
	private AsyncTaskExecutor asyncTaskExecutor;
	private String contentType = AsyncUtils.CONTENT_TYPE;
	private int flushInSecond = 1;
	private String asynCallback = DEFAULT_ASYNCALLBACK;
	
	private AsyncWebProcessorBuilder(HttpServletResponse response) {
		super();
		this.response = response;
	}
	
	private AsyncWebProcessorBuilder(HttpServletResponse response, HttpServletRequest request) {
		super();
		this.response = response;
		this.asynCallback = AsyncUtils.getAsyncCallbackName(request);
	}
	
	private AsyncWebProcessorBuilder(HttpServletResponse response, String jscallback) {
		super();
		this.response = response;
		this.asynCallback = jscallback;
	}
	
	public AsyncWebProcessorBuilder asyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor) {
		this.asyncTaskExecutor = asyncTaskExecutor;
		return this;
	}

	public AsyncWebProcessorBuilder contentType(String contentType){
		this.contentType = contentType;
		return this;
	}
	
	
	public AsyncWebProcessorBuilder asynCallback(String asynCallback) {
		this.asynCallback = asynCallback;
		return this;
	}

	public AsyncWebProcessorBuilder flushInSecond(int flushInSecond) {
		this.flushInSecond = flushInSecond;
		return this;
	}

	public AsyncWebProcessor<?> build(){
		if(asyncTaskExecutor==null){
			asyncTaskExecutor = SpringApplication.getInstance().getBean(AsyncTaskExecutor.class);
		}
		if(messageTunnel==null){
			messageTunnel = new StringMessageTunnel();
		}
		if(StringUtils.isBlank(asynCallback)){
			asynCallback = DEFAULT_ASYNCALLBACK;
		}
		response.setContentType(contentType);
		AsyncWebProcessor<?> processor = null;
		try {
			processor = new AsyncWebProcessor(response.getWriter(), messageTunnel, asyncTaskExecutor);
			processor.setSleepTime(flushInSecond);
			processor.setAsynCallback(asynCallback);
		} catch (IOException e) {
			throw new BaseException("build processor error: " + e.getMessage());
		}
		return processor;
	}
	

}
