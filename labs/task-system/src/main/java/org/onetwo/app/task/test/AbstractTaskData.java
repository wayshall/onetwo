package org.onetwo.app.task.test;

import org.onetwo.app.task.TaskData;
import org.onetwo.app.task.TaskType;

abstract public class AbstractTaskData implements TaskData {

	final private String name;
	final private TaskType type;
	
	public AbstractTaskData(String name, TaskType type) {
		super();
		this.name = name;
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public TaskType getType() {
		return type;
	}


}
