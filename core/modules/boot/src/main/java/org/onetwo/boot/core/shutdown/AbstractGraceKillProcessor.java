package org.onetwo.boot.core.shutdown;

import java.util.Arrays;
import java.util.Collection;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.shutdown.GraceKillSignalHandler.GraceKillProcessor;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

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
abstract public class AbstractGraceKillProcessor implements GraceKillProcessor {
	protected final Logger logger = JFishLoggerFactory.getCommonLogger();
	
	@Autowired
	private BootJFishConfig config;

	public Collection<String> getSignals() {
		if(LangUtils.isNotEmpty(config.getGraceKill().getSignals())){
			return config.getGraceKill().getSignals();
		}
		String signal = LangUtils.getOsName().toLowerCase().startsWith("win") ? GraceKillProcessor.INT : GraceKillProcessor.USR2;
		return Arrays.asList(signal);
	}

}
