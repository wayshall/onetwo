package org.onetwo.plugins.task.utils;

import java.io.Serializable;

import org.onetwo.plugins.task.entity.TaskQueue;

public class TaskResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3523016911350518264L;
	private final Object result;
	private final TaskQueue task;

	public TaskResult(Object data, TaskQueue task) {
		super();
		this.result = data;
		this.task = task;
	}

	public TaskQueue getTask() {
		return task;
	}

	public Object getResult() {
		return result;
	}

}
