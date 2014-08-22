package org.onetwo.plugins.task.utils;

import java.io.Serializable;

public class TaskResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3523016911350518264L;
	private final Object result;
	private final TaskData task;

	public TaskResult(Object data, TaskData task) {
		super();
		this.result = data;
		this.task = task;
	}

	public TaskData getTask() {
		return task;
	}

	public Object getResult() {
		return result;
	}

}
