package org.onetwo.common.web.asyn2;


public interface ProgressAsyncTaskCreator<T> {

	public AsyncTask create(int taskIndex, T taskDatas); 
}
