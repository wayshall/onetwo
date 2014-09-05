package org.onetwo.common.web.asyn2;

import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Future;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.Assert;

/****
 * web异步处理，如果容器不支持异步，web页面提交时可以提交到一个iframe上
 * @author way
 *
 */
public class AsyncWebProgressProcessor {
	
	private static final Logger logger = MyLoggerFactory.getLogger(AsyncWebProgressProcessor.class);
	private static final String DEFAULT_ASYNCALLBACK = "parent.jfishAsynCallback";
	
	private PrintWriter out;
//	private DeamonTask<?> task;
	private AsyncMessageHolder asynMessageHolder;
//	private int dataCountPerTask = Integer.MIN_VALUE;
	private String asynCallback = DEFAULT_ASYNCALLBACK;
	
	private int sleepTime = 1;//seconds
//	private int mainThreadSleepCount;
	
	private final AsyncTaskExecutor asyncTaskExecutor;
//	private float procceed = 0;
//	private int percent = 0;
//	private int taskCount = 1;
	

	AsyncWebProgressProcessor(PrintWriter out, String asynCallback) {
		this(out, new StringMessageHolder());
		if(StringUtils.isNotBlank(asynCallback))
			this.asynCallback = asynCallback;
	}

	AsyncWebProgressProcessor(PrintWriter out, AsyncMessageHolder holder) {
		this(out, holder, SpringApplication.getInstance().getBean(AsyncTaskExecutor.class));
	}
	AsyncWebProgressProcessor(PrintWriter out, AsyncMessageHolder holder, AsyncTaskExecutor asyncTaskExecutor) {
		super();
		this.out = out;
//		this.dataCountPerTask = taskInterval;
		this.asynMessageHolder = holder;
		Assert.notNull(asyncTaskExecutor, "no asyncTaskExecutor found, please add a asyncTaskExecutor to spring context!");
		this.asyncTaskExecutor = asyncTaskExecutor;
	}
	
	public AsyncMessageHolder getAsynMessageHolder() {
		return asynMessageHolder;
	}
	
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
	private boolean isIncludeJsFunction(String msg){
		return (msg.indexOf('(')!=-1 && msg.indexOf(')')!=-1);
	}
	
	public void renderMessage(ProcessMessageType state, int percent, String msg){
		if(StringUtils.isBlank(msg))
			return ;
		if(isIncludeJsFunction(msg)){
			renderScript(msg);
			return;
		}
//		String jsmsg = asynCallback + "('"+msg+"');";
		StringBuilder jsmsg = new StringBuilder(asynCallback)
												.append("('").append(msg).append("'")
												.append(", ").append(percent)
												.append(", '").append(state.toString()).append("'")
												.append(");");
		renderScript(jsmsg.toString());
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
	
	
	public void flushInfoMessage(){
		SimpleMessage[] meesages = asynMessageHolder.getAndClearMessages(); 
		/*procceed = procceed + meesages.length;
		int newPercent = (int)(procceed*100/dataTotalCount);
		percent = newPercent==0?1:newPercent;*/
		String msglist = formatMessages(meesages);
//		renderScript(out, renderMessage(ProccessorState.EXECUTING, percent, msglist));
		renderMessage(ProcessMessageType.INFO, 0, msglist);
		
	}
	
	public void flushProgressingMessage(int percent, AsyncTask task){
//		SimpleMessage[] meesages = asynMessageHolder.getAndClearMessages(); 
		/*procceed = procceed + meesages.length;
		int newPercent = (int)(procceed*100/dataTotalCount);
		percent = newPercent==0?1:newPercent;*/
//		String msglist = formatMessages(meesages);
//		renderScript(out, renderMessage(ProccessorState.EXECUTING, percent, msglist));
		renderMessage(ProcessMessageType.PROGRESSING, percent, asynMessageHolder.createTaskMessage(ProcessMessageType.PROGRESSING, percent, task));
		
	}
	
	/*private void registerTaskState(AsyncTask task){
		if(asynMessageHolder.getTaskStates().isEmpty()){
			TaskState[] states = task.getTaskStates();
			if(LangUtils.isEmpty(states))
				throw new BaseException("no taskstate register!");
			for(TaskState state : states){
				asynMessageHolder.registeState(state);
			}
		}
	}*/


	public void handleTask(int dataTotalCount, AsyncTask task){
		handleTask(1, 0, dataTotalCount, task);
	}
	
	private void handleTask(int taskCount, int currentTaskIndex, int dataTotalCountOfCurrentTask, AsyncTask task){
//		String taskMsg = "";
//		this.registerTaskState(task);
		Future<?> future = this.asyncTaskExecutor.submit(task);
		
		while(!future.isDone()){
			sleep();
			
			flushInfoMessage();
			
//			renderMessage(ProcessMessageType.PROGRESSING, percent, asynMessageHolder.createTaskMessage(ProcessMessageType.PROGRESSING, percent, task));
		}
		
		Object sync = ReflectUtils.getFieldValue(future, "sync");
		if(sync!=null){
			Throwable exp = (Throwable)ReflectUtils.getFieldValue(sync, "exception");
			if(exp!=null)
				logger.error("error: " + exp.getMessage(), exp);
		}
		
		flushInfoMessage();
		

		flushProgressingMessage(100, task);
		boolean notifyFinish = taskCount==(currentTaskIndex+1);
		if(task.isError()){
			logger.error("导入出错,任务终止", task.getException());
			renderMessage(ProcessMessageType.FAILED, 100, asynMessageHolder.createTaskMessage(ProcessMessageType.FAILED, 100, task));
//			renderScript(out, taskMsg);
			if(notifyFinish){
				IOUtils.closeQuietly(out);
			}
			return ;
		}else{
			renderMessage(ProcessMessageType.SUCCEED, 100, asynMessageHolder.createTaskMessage(ProcessMessageType.SUCCEED, 100, task));
//			renderScript(out, taskMsg);
		}
		asynMessageHolder.clearMessages();
		task = null;

		if(notifyFinish){
			renderMessage(ProcessMessageType.FINISHED, 100, asynMessageHolder.createTaskMessage(ProcessMessageType.FINISHED, taskCount, null));
//			renderScript(out, taskMsg);
			IOUtils.closeQuietly(out);
		}
	}
	

	protected String formatMessages(SimpleMessage[] simpleMessages) {
		if(LangUtils.isEmpty(simpleMessages))
			return "";
		StringBuilder sb = new StringBuilder();
		for(SimpleMessage msg : simpleMessages){
			sb.append(msg.toString()).append("<br/>");
		}
//		System.out.println("format: " + sb);
		return sb.toString();
	}
	
	public void handleList(List<?> datas, int dataCountPerTask, DeamonTaskCreator<List<?>> creator){
		int total = datas.size();
		int taskCount = total%dataCountPerTask==0?(total/dataCountPerTask):(total/dataCountPerTask+1);

		renderMessage(ProcessMessageType.SPLITED, taskCount, asynMessageHolder.createTaskMessage(ProcessMessageType.SPLITED, taskCount, null));
//		renderScript(out, taskMsg);
		for(int i=0; i<taskCount; i++){
//			int taskSleepCount = 0;
			int startIndex = i*dataCountPerTask;
			int endIndex = startIndex+dataCountPerTask;
			if(endIndex>total){
				endIndex = total;
			}
			final List<?> taskDatas = datas.subList(startIndex, endIndex);
			
//			this.doAsynWithSingle((i+1)==taskCount, i, taskDatas, creator);
			AsyncTask task = creator.create(i, taskDatas, asynMessageHolder);
			int percent = (int)((i+1)*100/taskCount);
			this.flushProgressingMessage(percent, task);
			
			handleTask(taskCount, i, dataCountPerTask, task);
			
		}

	}

	protected void renderScript(String content) {
		if (StringUtils.isBlank(content))
			return;
		out.println("<script>");
		out.println(content);
		out.println("</script>");
		out.flush();
//		System.out.println("flush out: " + content);
	}

}
