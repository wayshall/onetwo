package org.onetwo.common.profiling;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.helpers.NOPLogger;


public class Slf4jTimeLogger implements TimeLogger {
	private final Logger logger;
	

	public Slf4jTimeLogger() {
//		this.logger = null;
		logger = JFishLoggerFactory.getLogger(Slf4jTimeLogger.class);
	}
	
	public Slf4jTimeLogger(Logger logger) {
		super();
		this.logger = logger;
	}

	@Override
	public void log(Class<?> logSource, String msg, Object...args){
		Logger logger = this.logger;
		if(logger==null || NOPLogger.class.isInstance(logger)){
			logger = JFishLoggerFactory.getLogger(logSource);
		}
		logger.info(msg, args);
	}

	public Logger getLogger() {
		return logger;
	}


}
