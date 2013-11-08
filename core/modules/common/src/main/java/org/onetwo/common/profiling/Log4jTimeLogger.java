package org.onetwo.common.profiling;

import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;


public class Log4jTimeLogger implements TimeLogger {
	private static final Logger logger = MyLoggerFactory.getLogger(PROFILE_LOGGER);
	
	private TimeLogger outer = new TimerOutputer();
	
	@Override
	public void log(String msg){
		if(logger!=null){
			logger.info(msg);
		}else{
			outer.log(msg);
		}
	}

}
