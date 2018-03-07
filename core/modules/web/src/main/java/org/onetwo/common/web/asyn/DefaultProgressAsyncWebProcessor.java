package org.onetwo.common.web.asyn;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.asyn.AsyncMessageHolder.CreateTaskMessageContext;
import org.onetwo.common.web.asyn.ProgressAsyncTaskCreator.CreateContext;
import org.springframework.core.task.AsyncTaskExecutor;

/****
 * web异步处理，如果容器不支持异步，web页面提交时可以提交到一个iframe上
 * @author way
 *
 */
public class DefaultProgressAsyncWebProcessor extends DefaultAsyncWebProcessor implements ProgressAsyncWebProcessor {
	
	private int taskCount;
//	private String progressCallback;
	
	/*public DefaultProgressAsyncWebProcessor(PrintWriter out, String progressCallback, String asynCallback) {
		this(out, new StringMessageHolder(), null, progressCallback, asynCallback);
	}*/
	public DefaultProgressAsyncWebProcessor(PrintWriter out, AsyncMessageHolder holder, AsyncTaskExecutor asyncTaskExecutor, /*String progressCallback, */String asynCallback) {
//		this(out, new StringMessageHolder());
		super(out, holder, asyncTaskExecutor);
		this.asynCallback = asynCallback;
//		this.progressCallback = progressCallback;
	}

	/*private boolean isIncludeJsFunction(String msg){
		return (msg.indexOf('(')!=-1 && msg.indexOf(')')!=-1);
	}*/
	
	/*public AsyncMessageHolder getAsynMessageHolder() {
		return (AsyncMessageHolder)super.getAsynMessageHolder();
	}*/

	public void flushMessage(ProcessMessageType state, int percent, String msg){
		if(StringUtils.isBlank(msg))
			return ;
		/*if(isIncludeJsFunction(msg)){
			flushMessage(msg);
			return;
		}*/
//		String jsmsg = asynCallback + "('"+msg+"');";
		StringBuilder jsmsg = new StringBuilder(asynCallback)
												.append("({message: '").append(msg).append("'")
												.append(", percent: ").append(percent)
												.append(", state: '").append(state.toString()).append("'")
												.append("});");
		flushMessage(jsmsg.toString());
	}
	
	
	/*public void flushInfoMessage(){
		List<SimpleMessage> meesages = getAsynMessageHolder().getAndClearMessages(); 
		String msglist = formatMessages(meesages);
		flushMessage(ProcessMessageType.INFO, 0, msglist);
	}*/
	
	public void flushProgressingMessage(int percent, AsyncTask task){
		CreateTaskMessageContext ctx = new CreateTaskMessageContext(taskCount, ProcessMessageType.PROGRESSING, percent, task);
//		flushMessage(ProcessMessageType.PROGRESSING, percent, getAsynMessageHolder().createTaskMessage(ProcessMessageType.PROGRESSING, percent, task));
		flushMessage(ProcessMessageType.PROGRESSING, percent, getAsynMessageHolder().createTaskMessage(ctx));
	}

	/***
	 * 刷新用户处理过程中添加的消息
	 * 类型为：ProcessMessageType.INFO
	 */
	public void flushAndClearTunnelMessage(){
		List<?> meesages = getAsynMessageHolder().getAndClearMessages(); 
		if(LangUtils.isNotEmpty(meesages)){
			String msglist = formatMessages(meesages);
			flushMessage(ProcessMessageType.INFO, 0, msglist);
		}else if(writeEmptyMessage){
			List<?> emptyMessages = Arrays.asList(new SimpleMessage("", TaskState.PROCESSING, TaskState.PROCESSING.getName()));
			String msglist = formatMessages(emptyMessages);
			flushMessage(ProcessMessageType.EMPTY, 0, msglist);
		}
	}

	@Override
	public void handleTask(AsyncTask task){
		handleTask(true, task);
	}

	protected void doAfterTaskCompleted(boolean notifyFinish, AsyncTask task){
		flushAndClearTunnelMessage();
		
		if(task!=null){
			if(notifyFinish){
				flushProgressingMessage(100, task);
			}else{
				/*int percent = (int)((task.getTaskCount()+1)*100/taskCount);
				this.flushProgressingMessage(percent, task);*/
			}
//			boolean notifyFinish = taskCount==(currentTaskIndex+1);
			if(task.isError()){
				logger.error("任务["+task.getName()+"]出错,已中止", task.getException());
				CreateTaskMessageContext ctx = new CreateTaskMessageContext(taskCount, ProcessMessageType.FAILED, getTaskProcessPercent(task), task);
//				flushMessage(ProcessMessageType.FAILED, 100, getAsynMessageHolder().createTaskMessage(ProcessMessageType.FAILED, 100, task));
				this.getAsynMessageHolder().countMessage(TaskState.FAILED);
				flushMessage(ProcessMessageType.FAILED, 100, getAsynMessageHolder().createTaskMessage(ctx));
//				renderScript(out, taskMsg);
				if(notifyFinish){
					IOUtils.closeQuietly(out);
				}
				return ;
			}else if(task.isDone()){
				CreateTaskMessageContext ctx = new CreateTaskMessageContext(taskCount, ProcessMessageType.SUCCEED, getTaskProcessPercent(task), task);
//				flushMessage(ProcessMessageType.SUCCEED, 100, getAsynMessageHolder().createTaskMessage(ProcessMessageType.SUCCEED, 100, task));
				this.getAsynMessageHolder().countMessage(TaskState.SUCCEED);
				flushMessage(ProcessMessageType.SUCCEED, 100, getAsynMessageHolder().createTaskMessage(ctx));
//				renderScript(out, taskMsg);
			}
		}

		if(notifyFinish){
			CreateTaskMessageContext ctx = new CreateTaskMessageContext(taskCount, ProcessMessageType.FINISHED, getTaskProcessPercent(task), null);
//			flushMessage(ProcessMessageType.FINISHED, 100, getAsynMessageHolder().createTaskMessage(ProcessMessageType.FINISHED, taskCount, null));
			flushMessage(ProcessMessageType.FINISHED, 100, getAsynMessageHolder().createTaskMessage(ctx));
//			renderScript(out, taskMsg);
			IOUtils.closeQuietly(out);
		}
//		getAsynMessageHolder().clearMessages();
		task = null;
	}
	

	protected String formatMessages(List<?> simpleMessages) {
		if(LangUtils.isEmpty(simpleMessages))
			return "";
		StringBuilder sb = new StringBuilder();
		for(Object msg : simpleMessages){
			sb.append(msg.toString()).append("<br/>");
		}
//		System.out.println("format: " + sb);
		return sb.toString();
	}
	
	public <T> void handleList(List<T> datas, int dataCountPerTask, ProgressAsyncTaskCreator<T> creator){
		int total = datas.size();
		taskCount = total%dataCountPerTask==0?(total/dataCountPerTask):(total/dataCountPerTask+1);

		CreateTaskMessageContext msgCtx = new CreateTaskMessageContext(taskCount, ProcessMessageType.SPLITED, 0, null);
//		flushMessage(ProcessMessageType.SPLITED, taskCount, getAsynMessageHolder().createTaskMessage(ProcessMessageType.SPLITED, taskCount, null));
		flushMessage(ProcessMessageType.SPLITED, taskCount, getAsynMessageHolder().createTaskMessage(msgCtx));
//		renderScript(out, taskMsg);
		
		flushProgressingMessage(0, null);
		for(int i=0; i<taskCount; i++){
//			int taskSleepCount = 0;
			int startIndex = i*dataCountPerTask;
			int endIndex = startIndex+dataCountPerTask;
			if(endIndex>total){
				endIndex = total;
			}
			final List<T> taskDatas = datas.subList(startIndex, endIndex);
			
//			this.doAsynWithSingle((i+1)==taskCount, i, taskDatas, creator);
			CreateContext<T> ctx = new CreateContext<T>(i, taskDatas, getAsynMessageHolder());
			AsyncTask task = creator.create(ctx);
			/*int percent = (int)((i+1)*100/taskCount);
			this.flushProgressingMessage(percent, task);*/
			
			handleTask((i+1)==taskCount, task);
			doAfterAddTask(task);
		}

	}
	
	protected void doAfterAddTask(AsyncTask task){
		int percent = (int)((task.getTaskIndex()+1)*100/taskCount);
		this.flushProgressingMessage(percent, task);
	}
	
	protected int getTaskProcessPercent(AsyncTask task){
		if(task==null){
			return 0;
		}
		return getTaskProcessPercent(task.getTaskIndex());
	}
	
	protected int getTaskProcessPercent(int doneTaskCount){
		int percent = (int)((doneTaskCount+1)*100/taskCount);
		return percent;
	}

}
