package org.onetwo.boot.core.web.service.impl;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
public class SimpleLoggerManager {
	
	final private org.slf4j.Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private ConcurrentMap<Logger, Optional<Level>> loggerOriginLevelHoders = Maps.newConcurrentMap();
	
	public void changeLevel(String loggerName, String level){
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger targetLogger = loggerContext.getLogger(loggerName);
		if(targetLogger==null || StringUtils.isBlank(level)){
			throw new ServiceException("logger not found or error level!");
		}
		Level targetLevel = Level.toLevel(level);
		if(targetLevel.equals(targetLogger.getLevel())){
			throw new ServiceException("logger's level is equals target level");
		}
		loggerOriginLevelHoders.putIfAbsent(targetLogger, Optional.ofNullable(targetLogger.getLevel()));
		targetLogger.setLevel(targetLevel);
		logger.info("logger[{}] change level to {}", loggerName, level);
	}
	
	public void resetLevels(){
		loggerOriginLevelHoders.forEach((logger, level)->{
			logger.setLevel(level.orElse(null));
		});
	}

}
