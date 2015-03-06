package org.onetwo.common.log;

import java.util.List;

import org.onetwo.common.utils.list.JFishList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;

public class JFishLoggerFactory {
	
	public static Logger logger(Class<?> clazz){
		return instance.getLoggerInst(clazz);
	}
	
	public static Logger logger(String name){
		return instance.getLoggerInst(name);
	}

	public static Logger getLogger(Class<?> clazz){
		return instance.getLoggerInst(clazz);
	}
	
	public static Logger getLogger(String name){
		return instance.getLoggerInst(name);
	}
	
	private final static JFishLoggerFactory instance = new JFishLoggerFactory();

	protected List<? extends Logger> getLoggerInsts(){
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		return loggerContext.getLoggerList();
	}

	protected boolean hasLogger(String name){
		return findLogger(name)!=null;
	}
	protected Logger findLogger(String name){
		JFishList<? extends Logger> loggers = JFishList.wrap(getLoggerInsts());
		for(Logger logger : loggers){
			if(logger.getName().equals(name))
				return logger;
		}
		return null;
	}
	
	protected Logger getLoggerInst(Class<?> clazz){
		Logger logger =  LoggerFactory.getLogger(clazz);
		return logger;
	}
	
	protected Logger getLoggerInst(String name){
		return LoggerFactory.getLogger(name);
	}
	
	private JFishLoggerFactory(){}
}
