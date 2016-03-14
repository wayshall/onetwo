package org.onetwo.common.web.asyn2;

import java.util.Collection;
import java.util.Map;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.asyn.AsyncMessageTunnel;
import org.slf4j.Logger;

@SuppressWarnings("serial")
abstract public class AsyncMessageHolder extends AsyncMessageTunnel<SimpleMessage> {
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
//	protected int totalCount = 0;
//	protected Map<Integer, TaskState> asynStates = new LinkedHashMap<Integer, TaskState>();
	protected Map<TaskState, Integer> stateCounter = LangUtils.newHashMap();
	
//	private int succeedCount = 0;
	
	abstract protected Collection<SimpleMessage> getMessages();
	
	/*public final AsynMessageHolder registeState(TaskState state){
		this.asynStates.put(state.getValue(), state);
		return this;
	}

	public final AsynMessageHolder registeState(int value, String name){
		this.registeState(TaskState.createState(value, name));
		return this;
	}*/
	

	/*******
	 * 
	 * @param state
	 * @param taskCount 当state是ProccessorState.SplitTas时，是被分割成的任务数量；
	 * 					当state是ProccessorState.executing时，是百分比
	 * 					其他情况为-1，没用。
	 * @param task
	 * @return
	 */
	abstract public String createTaskMessage(ProcessMessageType state, int taskCount, AsyncTask task);

	public String countStatesAsString(){
		/*StringBuilder str = new StringBuilder();
		int count = 0;
		Collection<TaskState> states = this.getTaskStates();
		for(TaskState state : states){
			if(count!=states.size()){
				str.append(", ");
			}
			str.append(state);
			count++;
		}
		return str.toString();*/
		return stateCounter.isEmpty()?"":stateCounter.toString();
	}

	/***
	 * un-thread-safe
	 * @return
	 */
	public void addMessage(SimpleMessage msg){
		if(msg==null)
			return;
		try {
			getMessages().add(msg);
			countMessage(msg);
		} catch (Exception e) {
			logger.error("can not add msg to queue, ignore["+msg.getDetail()+"] : " + e.getMessage());
//			e.printStackTrace();
		}
	}
	
	/********
	 * 执行过程中添加信息
	 * @param data
	 * @param state  自定义状态
	 * @return
	 */
//	abstract protected SimpleMessage createMessage(T data, AsynState state);
	protected SimpleMessage createMessage(Object data, String detailMsg, TaskState state) {
		SimpleMessage msg = new SimpleMessage(data);
		if(detailMsg!=null)
			msg.setDetail(detailMsg);
		msg.setState(state);
		return msg;
	}
	
	public SimpleMessage addMessage(Object data, String detailMsg, TaskState state){
		SimpleMessage msg = this.createMessage(data, detailMsg, state);
		addMessage(msg);
		return msg;
	}
	
	public SimpleMessage addMessage(Object data, TaskState state){
		SimpleMessage msg = this.createMessage(data, null, state);
		addMessage(msg);
		return msg;
	}
	

	
	protected void countMessage(SimpleMessage msg){
		if(msg.getState()!=null){
			Integer count = stateCounter.get(msg.getState());
			count = count==null?1:count+1;
			stateCounter.put(msg.getState(), count);
		}
	}


	public void clearMessages() {
		getMessages().clear();
	}
	
}
