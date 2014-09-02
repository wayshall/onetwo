package org.onetwo.common.web.asyn2;


public interface DeamonTaskCreator<T> {

	public AsyncTask create(int taskIndex, T taskDatas, AsynMessageHolder<?> asynMessageHolder); 
}
