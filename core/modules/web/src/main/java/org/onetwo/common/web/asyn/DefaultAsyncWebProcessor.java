package org.onetwo.common.web.asyn;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Future;

import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.asyn2.AsyncTask;
import org.slf4j.Logger;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.Assert;

/****
 * web异步处理，如果容器不支持异步，web页面提交时可以提交到一个iframe上
 * @author way
 *
 */
public class DefaultAsyncWebProcessor<MSG> implements AsyncWebProcessor<MSG>{
	
	protected final Logger logger = MyLoggerFactory.getLogger(getClass());
	
	protected PrintWriter out;
	private AsyncMessageTunnel<MSG> asynMessageTunnel;
	protected String asynCallback;
	
	private int sleepTime = 1;//seconds
	protected final AsyncTaskExecutor asyncTaskExecutor;
	

	public DefaultAsyncWebProcessor(PrintWriter out, AsyncMessageTunnel<MSG> holder, String asynCallback) {
		this(out, holder, SpringApplication.getInstance().getBean(AsyncTaskExecutor.class));
		if(StringUtils.isNotBlank(asynCallback))
			this.asynCallback = asynCallback;
	}
	public DefaultAsyncWebProcessor(PrintWriter out, AsyncMessageTunnel<MSG> holder, AsyncTaskExecutor asyncTaskExecutor) {
		super();
		this.out = out;
//		this.dataCountPerTask = taskInterval;
		this.asynMessageTunnel = holder;
		Assert.notNull(asyncTaskExecutor, "no asyncTaskExecutor found, please add a asyncTaskExecutor to spring context!");
		this.asyncTaskExecutor = asyncTaskExecutor;
	}
	
	public AsyncMessageTunnel<MSG> getAsynMessageTunnel() {
		return asynMessageTunnel;
	}
	
	void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
	
	public void flushMessage(Object message){
		if(message==null)
			return ;
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
		List<?> meesages = asynMessageTunnel.getAndClearMessages(); 
		flushMessage(meesages);
	}
	@Override
	public void handleTask(AsyncTask task){
		Future<?> future = this.asyncTaskExecutor.submit(task);
		
		while(!future.isDone()){
			sleep();
			
			flushAndClearTunnelMessage();
		}
		
		Object sync = ReflectUtils.getFieldValue(future, "sync");
		if(sync!=null){
			Throwable exp = (Throwable)ReflectUtils.getFieldValue(sync, "exception");
			if(exp!=null)
				logger.error("error: " + exp.getMessage(), exp);
		}
		
		flushAndClearTunnelMessage();
		asynMessageTunnel.clearMessages();
		task = null;
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
