package org.onetwo.app.task.test;

import org.onetwo.app.task.TaskData;
import org.onetwo.app.task.TaskListener;
import org.onetwo.app.task.TaskListenerAdapter;
import org.onetwo.common.profiling.JFishLogger;
import org.onetwo.common.utils.LangUtils;

public class EmailListener extends TaskListenerAdapter {

	@Override
	public Object onExecute(TaskData task) {
		JFishLogger.INSTANCE.log("<<< execute task: " + task.getName());
		LangUtils.await(10);
		return null;
	}
}
