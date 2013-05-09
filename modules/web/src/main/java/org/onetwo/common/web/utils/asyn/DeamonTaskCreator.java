package org.onetwo.common.web.utils.asyn;


public interface DeamonTaskCreator {

	public DeamonTask<?> create(int taskIndex, Object taskDatas, AsynMessageHolder<?> asynMessageHolder); 
}
