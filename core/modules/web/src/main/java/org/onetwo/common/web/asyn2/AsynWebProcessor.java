package org.onetwo.common.web.asyn2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletResponse;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/****
 * web异步处理，如果容器不支持异步，web页面提交时可以提交到一个iframe上
 * @author way
 *
 */
public class AsynWebProcessor {

	public static AsynWebProcessor create(HttpServletResponse response) {
		return create(response, null);
	}
	public static AsynWebProcessor create(HttpServletResponse response, String asynCallback) {
		try {
			return new AsynWebProcessor(response.getWriter(), asynCallback);
		} catch (IOException e) {
			throw new BaseException("create AsynWebProcessor error: " + e.getMessage(), e);
		}
	}
	
	private static final Logger logger = MyLoggerFactory.getLogger(AsynWebProcessor.class);
	private static final String DEFAULT_ASYNCALLBACK = "parent.jfishAsynCallback";
	
	private PrintWriter out;
//	private DeamonTask<?> task;
	private AsynMessageHolder<?> asynMessageHolder;
	private int dataCountPerTask = Integer.MIN_VALUE;
	private String asynCallback = DEFAULT_ASYNCALLBACK;
	
	private int sleepTime = 1;//seconds
//	private int mainThreadSleepCount;
	
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	

	public AsynWebProcessor(PrintWriter out, String asynCallback) {
		super();
		this.out = out;
		if(StringUtils.isNotBlank(asynCallback))
			this.asynCallback = asynCallback;
		this.asynMessageHolder = new StringMessageHolder();
	}

	public AsynWebProcessor(PrintWriter out, AsynMessageHolder<?> holder) {
		super();
		this.out = out;
		this.asynMessageHolder = holder;
	}
	public AsynWebProcessor(PrintWriter out, int taskInterval, AsynMessageHolder<?> holder) {
		super();
		this.out = out;
		this.dataCountPerTask = taskInterval;
		this.asynMessageHolder = holder;
	}
	
	public AsynMessageHolder<?> getAsynMessageHolder() {
		return asynMessageHolder;
	}
	public AsynWebProcessor(HttpServletResponse response){
		try {
			this.out = response.getWriter();
		} catch (IOException e) {
			throw new BaseException("create asyn processor error: " + e.getMessage());
		}
	}
	
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
	private boolean isIncludeJsFunction(String msg){
		return (msg.indexOf('(')!=-1 && msg.indexOf(')')!=-1);
	}
	
	protected String appendCallbackFunctionIfNecessarry(String msg){
		if(StringUtils.isBlank(msg))
			return "";
		if(isIncludeJsFunction(msg))
			return msg;
		return asynCallback + "('"+msg+"');";
	}
	
	public void sleep(){
//		this.mainThreadSleepCount++;
		LangUtils.await(sleepTime);
	}

	public void setAsynCallback(String asynCallback) {
		this.asynCallback = asynCallback;
	}
	/*public void doAsynWithSingle(boolean notifyFinish, int taskIndex, Object taskDatas, DeamonTaskCreator creator){
		doAsynTask(notifyFinish, creator.create(taskIndex, taskDatas, asynMessageHolder));
	}*/
	

	public void handleTask(AsyncTask task){
		handleTask(true, task);
	}
	
	private void handleTask(boolean notifyFinish, AsyncTask task){
		String taskMsg = "";
		
		Future<?> future = this.threadPoolTaskExecutor.submit(task);
		
		float hasProcceed = 0;
		int percent = 0;
		String msglist = "";
		int taskInterval = getDataCountPerTask(100);
		while(!future.isDone()){
			sleep();
			
			SimpleMessage<?>[] meesages = asynMessageHolder.getAndClearMessages(); 
			hasProcceed += meesages.length;
			percent = (int)(hasProcceed*100/taskInterval);
			msglist = formatMessages(meesages);
			
			renderScript(out, appendCallbackFunctionIfNecessarry(msglist));
			taskMsg = appendCallbackFunctionIfNecessarry(asynMessageHolder.createTaskMessage(ProccessorState.EXECUTING, percent, task));
			renderScript(out, taskMsg);
		}
		
		msglist = formatMessages(asynMessageHolder.getAndClearMessages());
		renderScript(out, appendCallbackFunctionIfNecessarry(msglist));
		
		if(task.isError()){
			logger.error("导入出错,任务终止", task.getException());
			taskMsg = appendCallbackFunctionIfNecessarry(asynMessageHolder.createTaskMessage(ProccessorState.FAILED, -1, task));
			renderScript(out, taskMsg);
			if(notifyFinish){
				IOUtils.closeQuietly(out);
			}
			return ;
		}else{
			taskMsg = appendCallbackFunctionIfNecessarry(asynMessageHolder.createTaskMessage(ProccessorState.SUCCEED, -1, task));
			renderScript(out, taskMsg);
		}
		asynMessageHolder.clearMessages();
		task = null;

		if(notifyFinish){
			taskMsg = appendCallbackFunctionIfNecessarry(asynMessageHolder.createTaskMessage(ProccessorState.FINISHED, -1, null));
			renderScript(out, taskMsg);
			IOUtils.closeQuietly(out);
		}
	}
	

	protected String formatMessages(SimpleMessage<?>[] simpleMessages) {
		if(LangUtils.isEmpty(simpleMessages))
			return "";
		StringBuilder sb = new StringBuilder();
		for(SimpleMessage<?> msg : simpleMessages){
			sb.append(msg.toString()).append("<br/>");
		}
		return sb.toString();
	}
	/*
	public void doAsynTask(boolean notifyFinish, DeamonTask task){
		String taskMsg = "";
//		DeamonTask<?> task = creator.create(taskIndex, taskDatas, asynMessageHolder);
		task.start();
		float hasProcceed = 0;
		int percent = 0;
		String msglist = "";
		int taskInterval = getDataCountPerTask(100);
		while(!task.isFinished()){
			this.sleep();
			hasProcceed += asynMessageHolder.getMessages().size();
			percent = (int)(hasProcceed*100/taskInterval);
			msglist = asynMessageHolder.getHtmlMessagesAndClear();
			
			renderScript(out, appendCallbackFunctionIfNecessarry(msglist));
			taskMsg = appendCallbackFunctionIfNecessarry(asynMessageHolder.createTaskMessage(ProccessorState.executingTask, percent, task));
			renderScript(out, taskMsg);
		}

		msglist = asynMessageHolder.getHtmlMessagesAndClear();
		renderScript(out, appendCallbackFunctionIfNecessarry(msglist));
		
		if(task.isError()){
			logger.error("导入出错,任务终止", task.getException());
			taskMsg = appendCallbackFunctionIfNecessarry(asynMessageHolder.createTaskMessage(ProccessorState.errorTask, -1, task));
			renderScript(out, taskMsg);
			if(notifyFinish){
				IOUtils.closeQuietly(out);
			}
			return ;
		}else{
			taskMsg = appendCallbackFunctionIfNecessarry(asynMessageHolder.createTaskMessage(ProccessorState.finishedTask, -1, task));
			renderScript(out, taskMsg);
		}
		asynMessageHolder.clearMessages();
		task = null;

		if(notifyFinish){
			taskMsg = appendCallbackFunctionIfNecessarry(asynMessageHolder.createTaskMessage(ProccessorState.finished, -1, null));
			renderScript(out, taskMsg);
			IOUtils.closeQuietly(out);
		}
	}*/
	
	protected int getDataCountPerTask(int def){
		int taskInterval = this.dataCountPerTask;
		if(taskInterval==Integer.MIN_VALUE){//if no set
			taskInterval = def;
		}
		return taskInterval;
	}
	
	public void handleList(List<?> datas, DeamonTaskCreator<List<?>> creator){
		int total = datas.size();
		int taskInterval = getDataCountPerTask(total);
		int taskCount = total%taskInterval==0?(total/taskInterval):(total/taskInterval+1);

		String taskMsg = "";
		taskMsg = appendCallbackFunctionIfNecessarry(asynMessageHolder.createTaskMessage(ProccessorState.SPLITED, taskCount, null));
		renderScript(out, taskMsg);
		for(int i=0; i<taskCount; i++){
//			int taskSleepCount = 0;
			int startIndex = i*taskInterval;
			int endIndex = startIndex+taskInterval;
			if(endIndex>total){
				endIndex = total;
			}
			final List<?> taskDatas = datas.subList(startIndex, endIndex);
			
//			this.doAsynWithSingle((i+1)==taskCount, i, taskDatas, creator);
			handleTask((i+1)==taskCount, creator.create(i, taskDatas, asynMessageHolder));
		}

	}

	protected void renderMessage(String msg) {
		renderScript(out, appendCallbackFunctionIfNecessarry(msg));
	}

	protected void renderScript(PrintWriter out, String content) {
		if (StringUtils.isBlank(content))
			return;
		out.println("<script>");
		out.println(content);
		out.println("</script>");
		out.flush();
	}

}
