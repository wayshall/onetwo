package org.onetwo.boot.core.shutdown;

import java.util.Arrays;
import java.util.Collection;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.shutdown.GraceKillSignalHandler.GraceKillProcessor;
import org.onetwo.boot.core.shutdown.GraceKillSignalHandler.SignalInfo;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
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
public class BootGraceKillProcessor implements GraceKillProcessor {
	@Autowired
	private ConfigurableApplicationContext context;
	@Autowired
	private BootJFishConfig config;


	public Collection<String> getSignals() {
		if(LangUtils.isNotEmpty(config.getGraceKill().getSignals())){
			return config.getGraceKill().getSignals();
		}
		String signal = LangUtils.getOsName().toLowerCase().startsWith("win") ? GraceKillProcessor.INT : GraceKillProcessor.USR2;
		return Arrays.asList(signal);
	}
	
	@Override
	public void handle(SignalInfo singal) {
		Logger logger = JFishLoggerFactory.getCommonLogger();
		context.close();
		logger.warn("boot will exit!");
//		LangUtils.await(1);
		System.exit(0);
	}

}
