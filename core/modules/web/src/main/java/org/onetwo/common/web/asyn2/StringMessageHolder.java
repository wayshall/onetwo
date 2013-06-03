package org.onetwo.common.web.asyn2;

import org.onetwo.common.utils.LangUtils;



public class StringMessageHolder extends AsynMessageHolder<String>{


	public final AsynState succeed = createState(1, "成功");
	public final AsynState failed = createState(0, "失败");
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2307057128037819039L;
	
	public StringMessageHolder(){
		registeState(succeed).registeState(failed);
	}

	public String createTaskMessage(ProccessorState state, int taskCount, DeamonTask task){
		String msg = "";
		if(state==ProccessorState.afterSplitTask){
			msg = "进度 ：共分成"+taskCount+"个导入任务……";
		}else if(state==ProccessorState.executingTask){
			int done = taskCount/10;
			msg = "进度 ：正在执行["+task.getName()+"]"+taskCount+"% "+LangUtils.repeatString(done, "- ")+LangUtils.repeatString(10-done, "| ");
		}else if(state==ProccessorState.errorTask){
			msg = "进度 ：导入出错,任务终止"+task.getException().getMessage();
		}else if(state==ProccessorState.finishedTask){
			msg = "进度 ：["+task.getName()+"]完成！";
		}else if(state==ProccessorState.finished){
			msg = "结果 ：一共导入"+getTotalCount()+". "+getStatesAsString();
		}else{
			throw new UnsupportedOperationException();
		}
		return msg;
	}
	

	public AsynState getSucceed() {
		return succeed;
	}

	public AsynState getFailed() {
		return failed;
	}

}
