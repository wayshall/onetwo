package org.onetwo.app.tasksys.model.service;

import org.onetwo.app.tasksys.model.TaskData;

public interface TaskExecuteListener<T>{

	public T execute(TaskData task);

}
