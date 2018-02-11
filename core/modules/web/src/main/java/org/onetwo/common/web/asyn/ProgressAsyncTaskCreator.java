package org.onetwo.common.web.asyn;

import java.util.List;


public interface ProgressAsyncTaskCreator<T> {

	public AsyncTask create(CreateContext<T> ctx);

	public class CreateContext<T> {
		final int taskIndex;
		final List<T> taskDatas;
		final AsyncMessageHolder messageHolder;
		public CreateContext(int taskIndex, List<T> taskDatas,
				AsyncMessageHolder messageHolder) {
			super();
			this.taskIndex = taskIndex;
			this.taskDatas = taskDatas;
			this.messageHolder = messageHolder;
		}
		public int getTaskIndex() {
			return taskIndex;
		}
		public List<T> getTaskDatas() {
			return taskDatas;
		}
		public AsyncMessageHolder getMessageHolder() {
			return messageHolder;
		}
	}
}
