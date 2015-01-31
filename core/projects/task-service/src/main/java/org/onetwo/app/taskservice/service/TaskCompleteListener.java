package org.onetwo.app.taskservice.service;

import org.onetwo.plugins.task.utils.TaskResult;

public interface TaskCompleteListener extends TaskTypeMapper {

	public void onComplete(TaskResult result);

}
