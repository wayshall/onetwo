package org.onetwo.app.tasksys.model;

import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.springframework.util.Assert;


final public class TaskType {
	public static final TaskType EMAIL = new TaskType("EMAIL");
	public static final TaskType SMS = new TaskType("SMS");
	
	private static final Map<String, TaskType> TASKMAP = LangUtils.newHashMap();
	
	public static TaskType getType(String name){
		if(!TASKMAP.containsKey(name))
			throw new BaseException("no task found: " + name);
		return TASKMAP.get(name);
	}
	
	public static synchronized TaskType type(String name){
		Assert.notNull(name);
		if(!TASKMAP.containsKey(name)){
			TaskType task = new TaskType(name);
			TASKMAP.put(name, task);
			return task;
		}
		return TASKMAP.get(name);
	}

	private String name;
	
	private TaskType(String name){
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		TaskType other = (TaskType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
