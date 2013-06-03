package org.onetwo.common.web.utils.asyn;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.onetwo.apache.io.IOUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.ResponseUtils;
import org.slf4j.Logger;

@Deprecated
public class AsynWebProcessor {
	private static final Logger logger = MyLoggerFactory.getLogger(AsynWebProcessor.class);
	private static final String DEFAULT_ASYNCALLBACK = "parent.jfishAsynCallback";
	
	private PrintWriter out;
//	private DeamonTask<?> task;
	private AsynMessageHolder<?> asynMessageHolder;
	private int taskInterval = 1000;
	private String asynCallback = DEFAULT_ASYNCALLBACK;
	
	private int sleepTime = 3;//seconds
	private int mainThreadSleepCount;
	

	public AsynWebProcessor(PrintWriter out) {
		super();
		this.out = out;
		this.asynMessageHolder = new StringMessageHolder();
	}
	public AsynWebProcessor(PrintWriter out, int taskInterval) {
		super();
		this.out = out;
		this.taskInterval = taskInterval;
		this.asynMessageHolder = new StringMessageHolder();
	}
	
	public void setAsynMessageHolder(AsynMessageHolder<?> asynMessageHolder) {
		this.asynMessageHolder = asynMessageHolder;
	}
	public <T extends AsynMessageHolder<?>> T getAsynMessageHolder() {
		return (T)asynMessageHolder;
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
		this.mainThreadSleepCount++;
		LangUtils.await(sleepTime);
	}

	public void doAsynWithSingle(boolean notifyFinish, int taskIndex, Object taskDatas, DeamonTaskCreator creator){
		String taskMsg = "";
		DeamonTask<?> task = creator.create(taskIndex, taskDatas, asynMessageHolder);
		task.start();
		float hasProcceed = 0;
		int percent = 0;
		String msglist = "";
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
	}
	
	public void doAsyn(List<?> datas, DeamonTaskCreator creator){
		int total = datas.size();
		int taskCount = total/taskInterval;
		if(total%taskInterval!=0){
			taskCount += 1;
		}

		String taskMsg = "";
		taskMsg = appendCallbackFunctionIfNecessarry(asynMessageHolder.createTaskMessage(ProccessorState.afterSplitTask, taskCount, null));
		renderScript(out, taskMsg);
		for(int i=0; i<taskCount; i++){
//			int taskSleepCount = 0;
			int startIndex = i*taskInterval;
			int endIndex = startIndex+taskInterval;
			if(endIndex>total){
				endIndex = total;
			}
			final List<?> taskDatas = datas.subList(startIndex, endIndex);
			
			this.doAsynWithSingle((i+1)==taskCount, i, taskDatas, creator);
		}

	}
	

	protected void renderScript(PrintWriter out, String content) {
		ResponseUtils.renderScript(out, content);
	}

}
