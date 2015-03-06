package org.onetwo.common.log;

import org.slf4j.Logger;

@Deprecated
final public class MyLoggerFactory {
	private MyLoggerFactory(){}
	public static Logger getLogger(Class<?> clazz){
		return JFishLoggerFactory.getLogger(clazz);
	}
	
	public static Logger getLogger(String name){
		return JFishLoggerFactory.getLogger(name);
	}

}
