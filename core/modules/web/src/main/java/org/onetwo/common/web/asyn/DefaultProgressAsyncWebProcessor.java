package org.onetwo.common.web.asyn;

import java.io.PrintWriter;
import java.util.List;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.core.task.AsyncTaskExecutor;

/****
 * web异步处理，如果容器不支持异步，web页面提交时可以提交到一个iframe上
 * @author way
 *
 */
public class DefaultProgressAsyncWebProcessor extends DefaultAsyncWebProcessor<SimpleMessage> implements ProgressAsyncWebProcessor{
	

	public DefaultProgressAsyncWebProcessor(PrintWriter out, String asynCallback) {
		this(out, new StringMessageHolder());
		if(StringUtils.isNotBlank(asynCallback))
			this.asynCallback = asynCallback;
	}

	public DefaultProgressAsyncWebProcessor(PrintWriter out, AsyncMessageHolder holder) {
		this(out, holder, Springs.getInstance().getBean(AsyncTaskExecutor.class));
	}
	public DefaultProgressAsyncWebProcessor(PrintWriter out, AsyncMessageHolder holder, AsyncTaskExecutor asyncTaskExecutor) {
		super(out, holder, asyncTaskExecutor);
	}
	private boolean isIncludeJsFunction(String msg){
		return (msg.indexOf('(')!=-1 && msg.indexOf(')')!=-1);
	}
	
	public AsyncMessageHolder getAsynMessageTunnel() {
		return (AsyncMessageHolder)super.getAsynMessageTunnel();
	}

	public void flushMessage(ProcessMessageType state, int percent, String msg){
		if(StringUtils.isBlank(msg))
			return ;
		if(isIncludeJsFunction(msg)){
			flushMessage(msg);
			return;
		}
//		String jsmsg = asynCallback + "('"+msg+"');";
		StringBuilder jsmsg = new StringBuilder(asynCallback)
												.append("('").append(msg).append("'")
												.append(", ").append(percent)
												.append(", '").append(state.toString()).append("'")
												.append(");");
		flushMessage(jsmsg.toString());
	}
	
	
	/*public void flushInfoMessage(){
		List<SimpleMessage> meesages = getAsynMessageTunnel().getAndClearMessages(); 
		String msglist = formatMessages(meesages);
		flushMessage(ProcessMessageType.INFO, 0, msglist);
	}*/
	
	public void flushProgressingMessage(int percent, AsyncTask task){
		flushMessage(ProcessMessageType.PROGRESSING, percent, getAsynMessageTunnel().createTaskMessage(ProcessMessageType.PROGRESSING, percent, task));
	}

	public void flushAndClearTunnelMessage(){
		List<SimpleMessage> meesages = getAsynMessageTunnel().getAndClearMessages(); 
		String msglist = formatMessages(meesages);
		flushMessage(ProcessMessageType.INFO, 0, msglist);
	}

	@Override
	public void handleTask(AsyncTask task){
		handleTask(true, task);
	}

	protected void doAfterTaskCompleted(boolean notifyFinish, AsyncTask task){
		flushAndClearTunnelMessage();
		if(notifyFinish)
			flushProgressingMessage(100, task);
//		boolean notifyFinish = taskCount==(currentTaskIndex+1);
		if(task.isError()){
			logger.error("导入出错,任务终止", task.getException());
			flushMessage(ProcessMessageType.FAILED, 100, getAsynMessageTunnel().createTaskMessage(ProcessMessageType.FAILED, 100, task));
//			renderScript(out, taskMsg);
			if(notifyFinish){
				IOUtils.closeQuietly(out);
			}
			return ;
		}else{
			flushMessage(ProcessMessageType.SUCCEED, 100, getAsynMessageTunnel().createTaskMessage(ProcessMessageType.SUCCEED, 100, task));
//			renderScript(out, taskMsg);
		}

		if(notifyFinish){
			flushMessage(ProcessMessageType.FINISHED, 100, getAsynMessageTunnel().createTaskMessage(ProcessMessageType.FINISHED, task.getDataCount(), null));
//			renderScript(out, taskMsg);
			IOUtils.closeQuietly(out);
		}
		getAsynMessageTunnel().clearMessages();
		task = null;
	}
	

	protected String formatMessages(List<SimpleMessage> simpleMessages) {
		if(LangUtils.isEmpty(simpleMessages))
			return "";
		StringBuilder sb = new StringBuilder();
		for(SimpleMessage msg : simpleMessages){
			sb.append(msg.toString()).append("<br/>");
		}
//		System.out.println("format: " + sb);
		return sb.toString();
	}
	
	public void handleList(List<?> datas, int dataCountPerTask, ProgressAsyncTaskCreator<List<?>> creator){
		int total = datas.size();
		int taskCount = total%dataCountPerTask==0?(total/dataCountPerTask):(total/dataCountPerTask+1);

		flushMessage(ProcessMessageType.SPLITED, taskCount, getAsynMessageTunnel().createTaskMessage(ProcessMessageType.SPLITED, taskCount, null));
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
			AsyncTask task = creator.create(i, taskDatas);
			int percent = (int)((i+1)*100/taskCount);
			this.flushProgressingMessage(percent, task);
			
			handleTask((i+1)==taskCount, task);
			
		}

	}

}
