package org.onetwo.common.profiling;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.helpers.NOPLogger;


public class Slf4jTimeLogger implements TimeLogger {
	private TimeLogger outer = new TimerOutputer();
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
	public void log(String msg){
		if(logger!=null && !NOPLogger.class.isInstance(logger)){
			logger.info(msg);
		}else{
			outer.log(msg);
		}
	}

	@Override
	public void log(Object logSource, String msg){
		Logger logger = JFishLoggerFactory.getLogger(logSource.getClass());
		if(logger!=null){
			logger.info(msg);
		}else{
			outer.log(msg);
		}
	}

}
