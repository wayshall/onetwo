package org.onetwo.plugins.task.utils;

import java.io.Serializable;

public interface TaskData extends Serializable {
	public String getName() ;
	public TaskType getTaskType();
	public boolean isReply();
	
}
