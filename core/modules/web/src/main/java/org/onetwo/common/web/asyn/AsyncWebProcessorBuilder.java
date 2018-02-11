package org.onetwo.common.web.asyn;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.Springs;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.Assert;

/********
 * 
 * @author wayshall
 *
 */
public class AsyncWebProcessorBuilder {

//	private static final String DEFAULT_ASYNCALLBACK = "jfishAsynCallback";

	public static AsyncWebProcessorBuilder newBuilder(HttpServletResponse response){
		return new AsyncWebProcessorBuilder(response);
	}
	public static AsyncWebProcessorBuilder newBuilder(HttpServletRequest request, HttpServletResponse response){
		return new AsyncWebProcessorBuilder(request, response);
	}
	
//	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private AsyncMessageHolder messageTunnel;
	private AsyncTaskExecutor asyncTaskExecutor;
	private String contentType = AsyncUtils.CONTENT_TYPE;
	private int flushInMilliSecond = 1000;
	private String asynCallback = "parent.doAsynCallback";
//	private String progressCallback = "doProgressCallback";
	private boolean writeEmptyMessage;
	private boolean useCompletableFeture;
	
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
	
	public AsyncWebProcessorBuilder writeEmptyMessage(boolean writeEmptyMessage) {
		this.writeEmptyMessage = writeEmptyMessage;
		return this;
	}
	public AsyncWebProcessorBuilder asynCallback(String asynCallback) {
		this.asynCallback = asynCallback;
		return this;
	}

	public AsyncWebProcessorBuilder useCompletableFeture(boolean useCompletableFeture) {
		this.useCompletableFeture = useCompletableFeture;
		return this;
	}
	public AsyncWebProcessorBuilder flushInMilliSecond(int flushInSecond) {
		this.flushInMilliSecond = flushInSecond;
		return this;
	}

	public AsyncWebProcessorBuilder messageTunnel(AsyncMessageHolder messageTunnel) {
		this.messageTunnel = messageTunnel;
		return this;
	}

	/*public AsyncWebProcessorBuilder<MSG> progressCallback(String progressCallback) {
		this.progressCallback = progressCallback;
		return this;
	}*/
	public AsyncWebProcessor buildAsyncWebProcessor(){
		if(asyncTaskExecutor==null){
			asyncTaskExecutor = Springs.getInstance().getBean(AsyncTaskExecutor.class);
		}
		Assert.hasText(asynCallback);
//		Assert.hasText(progressCallback);
		response.setContentType(contentType);
		DefaultAsyncWebProcessor processor = null;
		try {
			processor = new DefaultAsyncWebProcessor(response.getWriter(), messageTunnel, asyncTaskExecutor);
			processor.setSleepTime(flushInMilliSecond);
			processor.setAsynCallback(asynCallback);
			processor.setWriteEmptyMessage(writeEmptyMessage);
		} catch (IOException e) {
			throw new BaseException("build processor error: " + e.getMessage());
		}
		return processor;
	}

	public ProgressAsyncWebProcessor buildProgressAsyncWebProcessor(){
		if(asyncTaskExecutor==null){
			asyncTaskExecutor = Springs.getInstance().getBean(AsyncTaskExecutor.class);
		}
		Assert.hasText(asynCallback);
//		Assert.hasText(progressCallback);
		response.setContentType(contentType);
		DefaultProgressAsyncWebProcessor processor = null;
		try {
			if(useCompletableFeture){
				processor = new CompletableProgressAsyncWebProcessor(response.getWriter(), messageTunnel, asyncTaskExecutor, /*progressCallback, */asynCallback);
			}else{
				processor = new DefaultProgressAsyncWebProcessor(response.getWriter(), messageTunnel, asyncTaskExecutor, /*progressCallback, */asynCallback);
			}
			processor.setSleepTime(flushInMilliSecond);
			processor.setWriteEmptyMessage(writeEmptyMessage);
		} catch (IOException e) {
			throw new BaseException("build ProgressAsyncWebProcessor error: " + e.getMessage());
		}
		return processor;
	}

}
