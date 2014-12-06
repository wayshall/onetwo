package org.onetwo.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JFishLoggerFactory {
	
	public static Logger logger(Class<?> clazz){
		return instance.getLogger(clazz);
	}
	
	public static Logger logger(String name){
		return instance.getLogger(name);
	}
	
	private final static JFishLoggerFactory instance = new JFishLoggerFactory();
	
	public Logger getLogger(Class<?> clazz){
		Logger logger =  LoggerFactory.getLogger(clazz);
		return logger;
	}
	
	public Logger getLogger(String name){
		return LoggerFactory.getLogger(name);
	}
	
	private JFishLoggerFactory(){}
}
