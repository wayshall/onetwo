package org.onetwo.common.web.asyn;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.Assert;

/****
 * web异步处理，如果容器不支持异步，web页面提交时可以提交到一个iframe上
 * @author way
 *
 */
public class DefaultAsyncWebProcessor implements AsyncWebProcessor{
	
	protected final Logger logger = JFishLoggerFactory.getLogger(getClass());
	
	protected PrintWriter out;
	private AsyncMessageHolder asynMessageHolder;
	protected String asynCallback;
	
	private int sleepTime = 1;//seconds
	protected final AsyncTaskExecutor asyncTaskExecutor;
	protected boolean writeEmptyMessage;
	

	public DefaultAsyncWebProcessor(PrintWriter out, AsyncMessageHolder holder, String asynCallback) {
		this(out, holder, Springs.getInstance().getBean(AsyncTaskExecutor.class));
		if(StringUtils.isNotBlank(asynCallback))
			this.asynCallback = asynCallback;
	}
	public DefaultAsyncWebProcessor(PrintWriter out, AsyncMessageHolder holder, AsyncTaskExecutor asyncTaskExecutor) {
		super();
		this.out = out;
//		this.dataCountPerTask = taskInterval;
		this.asynMessageHolder = holder;
		if(asyncTaskExecutor==null){
			this.asyncTaskExecutor = Springs.getInstance().getBean(AsyncTaskExecutor.class);
			Assert.notNull(this.asyncTaskExecutor, "no asyncTaskExecutor found, please add a asyncTaskExecutor to spring context!");
		}else{
			this.asyncTaskExecutor = asyncTaskExecutor;
		}
	}
	
	public void setWriteEmptyMessage(boolean writeEmptyMessage) {
		this.writeEmptyMessage = writeEmptyMessage;
	}
	public AsyncMessageHolder getAsynMessageHolder() {
		return asynMessageHolder;
	}
	
	void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
	
	public void flushMessage(Object message){
		if(message==null){
			return ;
		}
		//没有配置回调函数则忽略
		/*if(StringUtils.isBlank(asynCallback)){
			return ;
		}*/
		String jsObj = JsonMapper.IGNORE_EMPTY.toJson(message);
		StringBuilder jsmsg = new StringBuilder(asynCallback)
												.append("(").append(jsObj).append(");");
		flushMessage(jsmsg.toString());
	}
	
	public void sleep(){
		LangUtils.await(sleepTime);
	}

	void setAsynCallback(String asynCallback) {
		this.asynCallback = asynCallback;
	}
	
	public void flushAndClearTunnelMessage(){
		List<?> meesages = asynMessageHolder.getAndClearMessages();
		if(LangUtils.isNotEmpty(meesages)){
			flushMessage(meesages);
		}else if(writeEmptyMessage){
			meesages = Arrays.asList(new SimpleMessage("", TaskState.PROCESSING, TaskState.PROCESSING.getName()));
			flushMessage(meesages);
		}
	}
	
	@Override
	public void handleTask(AsyncTask task){
		handleTask(true, task);
	}
	
	/***
	 * 默认实现为每个人物都是阻塞执行，可扩展为并行
	 * @author wayshall
	 * @param closeWriter
	 * @param task
	 */
	protected void handleTask(boolean closeWriter, AsyncTask task){
		Assert.notNull(task);
		Future<?> future = this.asyncTaskExecutor.submit(task);
		
		while(!future.isDone()){
			sleep();
			
			flushAndClearTunnelMessage();
		}
		
		flushAndClearTunnelMessage();
		doAfterTaskCompleted(closeWriter, task);
	}
	
	protected void doAfterTaskCompleted(boolean closeWriter, AsyncTask task){
//		asynMessageTunnel.clearMessages();
		logIfThrowable(task);
		if(closeWriter){
			IOUtils.closeQuietly(out);
		}
		task = null;
	}
	
	protected void logIfThrowable(AsyncTask task){
		if(task.isError())
			logger.error("async processor error: " + task.getException().getMessage(), task.getException());
	}

	public void flushMessage(String content) {
		if (StringUtils.isBlank(content))
			return;
		out.println("<script>");
		out.println(content);
		out.println("</script>");
		out.flush();
	}

}
