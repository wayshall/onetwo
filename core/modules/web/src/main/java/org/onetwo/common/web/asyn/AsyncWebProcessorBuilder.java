package org.onetwo.common.web.asyn;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.asyn2.AsyncMessageHolder;
import org.onetwo.common.web.asyn2.ProgressAsyncWebProcessor;
import org.onetwo.common.web.asyn2.StringMessageHolder;
import org.springframework.core.task.AsyncTaskExecutor;

public class AsyncWebProcessorBuilder {

	private static final String DEFAULT_ASYNCALLBACK = "jfishAsynCallback";

	public static AsyncWebProcessorBuilder newBuilder(HttpServletResponse response){
		return new AsyncWebProcessorBuilder(response);
	}
	public static AsyncWebProcessorBuilder newBuilder(HttpServletResponse response, String jscallback){
		return new AsyncWebProcessorBuilder(response, jscallback);
	}
	public static AsyncWebProcessorBuilder newBuilder(HttpServletRequest request, HttpServletResponse response){
		return new AsyncWebProcessorBuilder(request, response);
	}
	
//	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private AsyncMessageTunnel<?> messageTunnel;
	private AsyncTaskExecutor asyncTaskExecutor;
	private String contentType = AsyncUtils.CONTENT_TYPE;
	private int flushInSecond = 1;
	private String asynCallback = DEFAULT_ASYNCALLBACK;
	
	private boolean progressProcessor;
	
	private AsyncWebProcessorBuilder(HttpServletResponse response) {
		super();
		this.response = response;
	}
	
	private AsyncWebProcessorBuilder(HttpServletRequest request, HttpServletResponse response) {
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

	public AsyncWebProcessorBuilder progressProcessor(boolean progress) {
		this.progressProcessor = progress;
		return this;
	}

	public AsyncWebProcessorBuilder messageTunnel(AsyncMessageTunnel<?> messageTunnel) {
		this.messageTunnel = messageTunnel;
		return this;
	}

	public AsyncWebProcessor<?> build(){
		if(asyncTaskExecutor==null){
			asyncTaskExecutor = SpringApplication.getInstance().getBean(AsyncTaskExecutor.class);
		}
		if(StringUtils.isBlank(asynCallback)){
			asynCallback = AsyncUtils.getAsyncCallbackName(DEFAULT_ASYNCALLBACK);
		}
		response.setContentType(contentType);
		DefaultAsyncWebProcessor<?> processor = null;
		try {
			if(progressProcessor){
				if(messageTunnel==null){
					messageTunnel = new StringMessageHolder();
				}
				processor = new ProgressAsyncWebProcessor(response.getWriter(), (AsyncMessageHolder)messageTunnel, asyncTaskExecutor);
			}else{
				if(messageTunnel==null){
					messageTunnel = new StringMessageTunnel();
				}
				processor = new DefaultAsyncWebProcessor(response.getWriter(), messageTunnel, asyncTaskExecutor);
			}
			processor.setSleepTime(flushInSecond);
			processor.setAsynCallback(asynCallback);
		} catch (IOException e) {
			throw new BaseException("build processor error: " + e.getMessage());
		}
		return processor;
	}
	

}
