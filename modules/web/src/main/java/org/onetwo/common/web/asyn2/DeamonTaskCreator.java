package org.onetwo.common.web.asyn2;


public interface DeamonTaskCreator {

	public DeamonTask create(int taskIndex, Object taskDatas, AsynMessageHolder<?> asynMessageHolder); 
}
