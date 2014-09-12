package org.onetwo.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final public class MyLoggerFactory {
	private MyLoggerFactory(){}
	public static Logger getLogger(Class<?> clazz){
		Logger logger =  LoggerFactory.getLogger(clazz);
		return logger;
	}
	
	public static Logger getLogger(String name){
		return LoggerFactory.getLogger(name);
	}

}
