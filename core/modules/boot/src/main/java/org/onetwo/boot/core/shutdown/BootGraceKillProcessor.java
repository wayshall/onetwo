package org.onetwo.boot.core.shutdown;

import org.onetwo.boot.core.shutdown.GraceKillSignalHandler.SignalInfo;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * windows:
 * taskkill /f /pid pid
 * 
 * linux:
 * kill -s USR2 pid = kill -12 pid
 * 
 * kill默认为SIGTERM，会执行shutdownHook
 * @author wayshall
 * <br/>
 */
public class BootGraceKillProcessor extends AbstractGraceKillProcessor {
	@Autowired
	private ConfigurableApplicationContext context;
	
	@Override
	public void handle(SignalInfo singal) {
		Logger logger = JFishLoggerFactory.getCommonLogger();
		context.close();
		logger.warn("boot will exit!");
//		LangUtils.await(1);
		System.exit(0);
	}

}
