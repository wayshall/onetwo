package com.qyscard.task.test;

import org.onetwo.app.task.TaskListenerAdapter;
import org.onetwo.common.profiling.JFishLogger;
import org.onetwo.common.utils.LangUtils;

import com.qyscard.task.entity.TaskQueueEntity;

public class EmailListener extends TaskListenerAdapter<TaskQueueEntity> {

	@Override
	public Object onExecute(TaskQueueEntity task) {
		JFishLogger.INSTANCE.log("<<< execute task: " + task.getName());
		LangUtils.await(10);
		return null;
	}
}
