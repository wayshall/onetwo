package org.onetwo.common.web.asyn;

import org.onetwo.common.utils.LangUtils;



public class StringMessageHolder extends BlockQueueMessageHolder{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2307057128037819039L;

	
	public StringMessageHolder(){
//		registeState(TaskState.SUCCEED).registeState(TaskState.FAILED);
	}
	
	/*public StringMessageHolder registerDefaultStates(){
		registeState(SUCCEED).registeState(FAILED);
		return this;
	}*/

	public void addSucceedMessage(String msg){
		addMessage(null, msg, TaskState.SUCCEED);
	}

	public void addFailedMessage(String msg){
		addMessage(null, msg, TaskState.FAILED);
	}
	public void addCommonMessage(String msg){
		addMessage(null, msg, null);
	}

	public String createTaskMessage(CreateTaskMessageContext ctx){
		ProcessMessageType state = ctx.getState();
		int taskCount = ctx.getTaskCount();
		AsyncTask task = ctx.getTask();
		String msg = "";
		if(state==ProcessMessageType.SPLITED){
			msg = "进度: 共分成"+taskCount+"个导入任务……";
		}else if(state==ProcessMessageType.PROGRESSING){
			int done = ctx.getTaskDonePercent();
			msg = "进度: "+done+"% "+LangUtils.repeatString(done/10, "- ")+LangUtils.repeatString((100-done)/10, "| ");
			if(task!=null){
				msg += "正在执行["+task.getName()+"]";
			}
		}else if(state==ProcessMessageType.FAILED){
			msg = "进度: 任务["+task.getName()+"]出错,已中止"+task.getException().getMessage();
		}else if(state==ProcessMessageType.SUCCEED){
			msg = "进度: ["+task.getName()+"]完成！";
		}else if(state==ProcessMessageType.FINISHED){
//			msg = "结果 ：一共导入"+getTotalCount()+". " + getStatesAsString();
			msg = "结果: 一共执行了"+taskCount+"个任务. " + countStatesAsString();
		}else{
			throw new UnsupportedOperationException();
		}
		return msg;
	}
	
}
