package org.onetwo.app.tasksys.model;

import java.io.Serializable;

public interface TaskData extends Serializable {
	public String getName() ;
	public TaskType getTaskType();
	
}
