package org.onetwo.common.web.asyn2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;

abstract public class AsynMessageHolder<T> implements Serializable {

	/*public static enum AsynState {
		SUCCEED("成功"),
		FAILED("失败");
		
		private final String name;
		
		private AsynState(String name){
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}*/
	
	public static class AsynState {
		
		private final int value;
		private int count = 0;
		private String name;
		private AsynState(int value, String name) {
			super();
			this.value = value;
			this.name = name;
		}
		private void increaseCount(int i) {
			count += i;
		}
		public int getValue() {
			return value;
		}
		public String getName() {
			return name;
		}
		public int getCount() {
			return count;
		}
		
		public String toString(){
			return name+": " + count;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + value;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AsynState other = (AsynState) obj;
			if (value != other.value)
				return false;
			return true;
		}
		
	}

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7009021457311801551L;
	private List<SimpleMessage<T>> messages = new ArrayList<SimpleMessage<T>>();
	private Object lock = new Object();
	private int totalCount = 0;
	private Map<Integer, AsynState> asynStates = new LinkedHashMap<Integer, AsynState>();
	
//	private int succeedCount = 0;
	
	public final AsynMessageHolder<?> registeState(AsynState state){
		this.asynStates.put(state.getValue(), state);
		return this;
	}

	public final AsynMessageHolder<?> registeState(int value, String name){
		this.registeState(createState(value, name));
		return this;
	}
	
	public static AsynState createState(int value, String name){
		return new AsynState(value, name);
	}

	/*******
	 * 
	 * @param state
	 * @param taskCount 当state是ProccessorState.afterSplitTas时，是被分割成的任务数量；
	 * 					当state是ProccessorState.executingTask时，是百分比
	 * 					其他情况为-1，没用。
	 * @param task
	 * @return
	 */
	abstract public String createTaskMessage(ProccessorState state, int taskCount, DeamonTask task);

	public String getStatesAsString(){
		StringBuilder str = new StringBuilder();
		int count = 0;
		Collection<AsynState> states = this.getAsynStates();
		for(AsynState state : states){
			if(count!=states.size()){
				str.append(", ");
			}
			str.append(state);
			count++;
		}
		return str.toString();
	}
	/********
	 * 执行过程中添加信息
	 * @param data
	 * @param state  自定义状态
	 * @return
	 */
//	abstract protected SimpleMessage createMessage(T data, AsynState state);
	protected SimpleMessage<T> createMessage(T data, String detailMsg, AsynState state) {
		SimpleMessage<T> msg = new SimpleMessage<T>(data);
		if(detailMsg!=null)
			msg.setDetail(detailMsg);
		msg.setState(state);
		return msg;
	}
	
	public SimpleMessage<T> addMessage(T data, String detailMsg, AsynState state){
		SimpleMessage<T> msg = this.createMessage(data, detailMsg, state);
		addMessage(msg);
		return msg;
	}
	
	public SimpleMessage<T> addMessage(T data, AsynState state){
		SimpleMessage<T> msg = this.createMessage(data, null, state);
		addMessage(msg);
		return msg;
	}

	public void addMessage(SimpleMessage<T> msg){
		if(msg==null)
			return;
		synchronized (lock) {
			this.messages.add(msg);
			this.totalCount++;
			this.triggerCounters(msg);
		}
	}

	protected void triggerCounters(SimpleMessage<T> msg){
		this.triggerStates(msg.getState());
	}
	
	protected void triggerStates(AsynState...states){
		for(AsynState state : states){
			if(state!=null)
				this.asynStates.get(state.getValue()).increaseCount(1);
		}
	}

	public List<SimpleMessage<T>> getMessages() {
		return messages;
	}

	public void clearMessages() {
		messages.clear();
	}

	public String getHtmlMessagesAndClear() {
		SimpleMessage<T>[] simpleMessages = null;
		synchronized (lock) {
			simpleMessages = messages.toArray(new SimpleMessage[messages.size()]);
			this.clearMessages();
		}
		if(LangUtils.isEmpty(simpleMessages))
			return "";
		StringBuilder sb = new StringBuilder();
		for(SimpleMessage<T> msg : simpleMessages){
			sb.append(msg.toString()+"<br/>");
		}
		return sb.toString();
	}
	

	public String toString() {
		SimpleMessage<T>[] simpleMessages = null;
		synchronized (lock) {
			simpleMessages = messages.toArray(new SimpleMessage[messages.size()]);
			this.clearMessages();
		}
		
		StringBuilder sb = new StringBuilder();
		for(SimpleMessage<T> msg : simpleMessages){
			sb.append(msg.toString());
		}
		return sb.toString();
	}

	public int getTotalCount() {
		return totalCount;
	}

	public Collection<AsynState> getAsynStates() {
		return asynStates.values();
	}
	
}
