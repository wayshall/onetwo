package org.onetwo.common.log;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.list.JFishList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;

public class JFishLoggerFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(JFishLoggerFactory.class);
	
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
		return findLogger("mailLogger");
	}
	
	public static boolean mailLog(Collection<String> notifyThrowables, Throwable ex, String msg){
		return instance.mailIfNecessary(notifyThrowables, msg, ex);
	}
	
	private final static JFishLoggerFactory instance = new JFishLoggerFactory();
	
	public static JFishLoggerFactory getInstance() {
		return instance;
	}

	private Collection<String> notifyThrowables = Collections.EMPTY_LIST;

	protected List<? extends Logger> getLoggerInsts(){
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		return loggerContext.getLoggerList();
	}

	protected boolean hasLogger(String name){
		return findLogger(name)!=null;
	}
	protected Logger findLoggerInst(String name){
		JFishList<? extends Logger> loggers = JFishList.wrap(getLoggerInsts());
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
		return mailIfNecessary(notifyThrowables, msg, ex);
	}

	/***
	 * return ture if send log email
	 * @param notifyThrowables
	 * @param msg
	 * @param ex
	 * @return
	 */
	boolean mailIfNecessary(Collection<String> notifyThrowables, String msg, Throwable ex){
		if(LangUtils.isEmpty(notifyThrowables))
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
