package org.onetwo.common.web.utils.asyn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.utils.LangUtils;

@Deprecated
abstract public class AsynMessageHolder<T> implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7009021457311801551L;
	private List<SimpleMessage> messages = new ArrayList<SimpleMessage>();
	private Object lock = new Object();

	/*******
	 * 
	 * @param state
	 * @param taskCount 当state是ProccessorState.afterSplitTas时，是被分割成的任务数量；
	 * 					当state是ProccessorState.executingTask时，是百分比
	 * 					其他情况为-1，没用。
	 * @param task
	 * @return
	 */
	public String createTaskMessage(ProccessorState state, int taskCount, DeamonTask<?> task){
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
			msg = "进度 ：导入完成！ ";
		}else{
			throw new UnsupportedOperationException();
		}
		return msg;
	}

	/********
	 * 执行过程中添加信息
	 * @param data
	 * @param state  自定义状态
	 * @return
	 */
	abstract protected SimpleMessage createMessage(T data, Integer state);


	public void addMessage(T data, Integer state){
		SimpleMessage msg = this.createMessage(data, state);
		addMessage(msg);
	}

	public void addMessage(SimpleMessage msg){
		if(msg==null)
			return;
		synchronized (lock) {
			this.messages.add(msg);
		}
	}


	public List<SimpleMessage> getMessages() {
		return messages;
	}

	public void clearMessages() {
		messages.clear();
	}

	public String getHtmlMessagesAndClear() {
		SimpleMessage[] simpleMessages = null;
		synchronized (lock) {
			simpleMessages = messages.toArray(new SimpleMessage[messages.size()]);
			this.clearMessages();
		}
		if(LangUtils.isEmpty(simpleMessages))
			return "";
		StringBuilder sb = new StringBuilder();
		for(SimpleMessage msg : simpleMessages){
			sb.append(msg.toString()+"<br/>");
		}
		return sb.toString();
	}
	

	public String toString() {
		SimpleMessage[] simpleMessages = null;
		synchronized (lock) {
			simpleMessages = messages.toArray(new SimpleMessage[messages.size()]);
			this.clearMessages();
		}
		
		StringBuilder sb = new StringBuilder();
		for(SimpleMessage msg : simpleMessages){
			sb.append(msg.toString());
		}
		return sb.toString();
	}
	
}
