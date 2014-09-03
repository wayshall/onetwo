package org.onetwo.common.web.asyn2;

import java.util.Map;

import org.onetwo.common.utils.LangUtils;


public class TaskState {
	private static final Map<Integer, TaskState> STATES = LangUtils.newHashMap();
	public static final TaskState SUCCEED;
	public static final TaskState FAILED;
	static {
		SUCCEED = createState(1, "成功");
		FAILED = createState(0, "失败");
	}
//	public static final TaskState NONE = TaskState.createState(0, "进度");

	/*public static AsynState createSucceed(){
		return createState(1, "成功");
	}
	public static AsynState createFaild(){
		return createState(0, "失败");
	}*/
	 
	public static TaskState createState(int value, String name){
		TaskState state = STATES.get(value);
		if(state!=null){
			return state;
		}
		state = new TaskState(value, name);
		STATES.put(state.getValue(), state);
		return state;
	}
	
	
	private final int value;
//	private int count = 0;
	private final String name;
	private TaskState(int value, String name) {
		super();
		this.value = value;
		this.name = name;
	}
	/*void increaseCount(int i) {
		count += i;
	}*/
	public int getValue() {
		return value;
	}
	public String getName() {
		return name;
	}
	/*public int getCount() {
		return count;
	}*/
	
	public String toString(){
//		return name+": " + count;
		return name;
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
		TaskState other = (TaskState) obj;
		if (value != other.value)
			return false;
		return true;
	}
}
