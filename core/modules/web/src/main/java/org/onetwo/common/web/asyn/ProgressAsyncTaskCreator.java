package org.onetwo.common.web.asyn;


public interface ProgressAsyncTaskCreator<T> {

	public AsyncTask create(int taskIndex, T taskDatas); 
}
