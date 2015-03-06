package org.onetwo.common.profiling;

import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;


public class Log4jTimeLogger implements JFishLogger {
//	private static final Logger logger = MyLoggerFactory.getLogger(PROFILE_LOGGER);
	
	private JFishLogger outer = new TimerOutputer();
	private final Logger logger;
	

	public Log4jTimeLogger() {
//		this.logger = null;
		logger = MyLoggerFactory.getLogger(Log4jTimeLogger.class);
	}
	
	public Log4jTimeLogger(Logger logger) {
		super();
		this.logger = logger;
	}

	@Override
	public void log(String msg){
		if(logger!=null){
			logger.info(msg);
		}else{
			outer.log(msg);
		}
	}

	@Override
	public void log(Object logSource, String msg){
		Logger logger = MyLoggerFactory.getLogger(logSource.getClass());
		if(logger!=null){
			logger.info(msg);
		}else{
			outer.log(msg);
		}
	}

}
