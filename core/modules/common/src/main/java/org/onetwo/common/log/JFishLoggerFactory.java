package org.onetwo.common.log;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;

import ch.qos.logback.classic.LoggerContext;

public class JFishLoggerFactory {
	
	private static final String MAIL_LOGGER = "mailLogger";
	private static final String ERROR_LOGGER = "errorLogger";
	
	public static final String COMMON_LOGGER_NAME = "org.onetwo.common.log.CommonLog";
	
	private static Logger mailLogger = null;
	private static Logger errorLogger = null;
	
	public static Logger getCommonLogger(){
		return getLogger(COMMON_LOGGER_NAME);
	}
	
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
	
	public static Logger findLogger(String name){
		return instance.findLoggerInst(name);
	}
	
	public static Logger findMailLogger(){
		Logger logger = mailLogger;
		if (logger == null) {
			logger = findLogger(MAIL_LOGGER);
			if (logger == null) {
				logger = NOPLogger.NOP_LOGGER;
			}
			mailLogger = logger;
		}
		return logger;
	}

	public static Logger findErrorLogger(){
		return findErrorLogger(NOPLogger.NOP_LOGGER);
	}
	
	public static Logger findErrorLogger(Logger def){
		Logger logger = errorLogger;
		if (logger == null) {
			logger = findLogger(ERROR_LOGGER);
			if (logger == null) {
				logger = def;
			}
			errorLogger = logger;
		}
		return logger;
	}
	
	public static boolean mailLog(Collection<String> notifyThrowables, Throwable ex, String msg){
		return instance.mailIfNecessary(msg, ex, notifyThrowables);
	}

	public static boolean mailLog(String msg, Throwable ex, Throwable... notifyThrowables){
		return instance.mailIfNecessary(msg, ex, notifyThrowables);
	}
	
	private final static JFishLoggerFactory instance = new JFishLoggerFactory();
	
	public static JFishLoggerFactory getInstance() {
		return instance;
	}

	private Collection<String> notifyThrowables = Collections.emptyList();

	protected List<? extends Logger> getLoggerInsts(){
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		return loggerContext.getLoggerList();
	}

	protected boolean hasLogger(String name){
		return findLogger(name)!=null;
	}
	protected Logger findLoggerInst(String name){
		List<? extends Logger> loggers = getLoggerInsts();
		for(Logger logger : loggers){
			if(logger.getName().equals(name))
				return proxyLogger(logger);
		}
		return null;
	}
	
	protected Logger getLoggerInst(Class<?> clazz){
		Logger logger =  LoggerFactory.getLogger(clazz);
		return proxyLogger(logger);
	}
	
	protected Logger getLoggerInst(String name){
		return proxyLogger(LoggerFactory.getLogger(name));
	}
	
	private Logger proxyLogger(Logger logger){
		return logger;
//		return new JFishProxyLogger(logger);
	}
	
	boolean mailIfNecessary(String msg, Throwable ex){
		return mailIfNecessary(msg, ex, notifyThrowables);
	}


	boolean mailIfNecessary(String msg, Throwable ex, String... notifyThrowables){
		return mailIfNecessary(msg, ex, Arrays.asList(notifyThrowables));
	}
	boolean mailIfNecessary(String msg, Throwable ex, Throwable... notifyThrowables){
		String[] names = (String[])Stream.of(notifyThrowables).map(e-> 
																	Arrays.asList(e.getClass().getName(), e.getClass().getSimpleName()))
																.flatMap(list -> list.stream()).toArray();
		return mailIfNecessary(msg, ex, names);
	}

	/***
	 * return ture if send log email
	 * @param msg
	 * @param ex
	 * @param notifyThrowables
	 * @return
	 */
	boolean mailIfNecessary(String msg, Throwable ex, Collection<String> notifyThrowables){
		Collection<String> notifyThrowableList = LangUtils.isEmpty(notifyThrowables)?this.notifyThrowables:notifyThrowables;
		if(LangUtils.isEmpty(notifyThrowableList))
			return false;
		Logger mailLogger = findMailLogger();
		if(mailLogger==null){
//			logger.info("mail logger not found!");
			return false;
		}
		boolean mail = CUtils.containsAnyOne(notifyThrowables, ex.getClass().getName(), ex.getClass().getSimpleName());
		Throwable thr = ex.getCause();
		if(!mail && thr!=null){
			mail = CUtils.containsAnyOne(notifyThrowables, thr.getClass().getName(), thr.getClass().getSimpleName());
		}
		if(mail){
			mailLogger.error("send email for error: " + msg, ex);
			return true;
		}
		return false;
	}
	private JFishLoggerFactory(){}
}
