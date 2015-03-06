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
	private final Throwable throwable;

	public TaskResult(TaskQueue task, Object data) {
		super();
		this.result = data;
		this.task = task;
		this.throwable = null;
	}
	
	public TaskResult(TaskQueue task, Throwable throwable) {
		super();
		this.task = task;
		this.throwable = throwable;
		this.result = null;
	}


	public Throwable getThrowable() {
		return throwable;
	}

	public TaskQueue getTask() {
		return task;
	}

	public Object getResult() {
		return result;
	}

}
