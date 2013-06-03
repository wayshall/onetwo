package org.onetwo.common.web.utils;

import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.slf4j.Logger;

abstract public class Debugger {

	public static boolean isDebugModel(){
		return BaseSiteConfig.getInstance().isDev();
	}
	
	public static void debug(String message){
		if(isDebugModel()){
			System.out.println(DateUtil.nowString()+":"+message);
		}
	}
	
	public static void debug(Logger logger, String message){
		if(isDebugModel()){
			logger.info(message);
		}
	}
	
	public static void debug(Exception e){
		if(isDebugModel()){
			e.printStackTrace();
		}
	}
	
	public static void debug(Logger logger, Exception e){
		if(isDebugModel()){
			logger.error("debug error : " + e.getMessage(), e);
		}
	}
	
	public static void debug(String message, Exception e){
		debug(message);
		debug(e);
	}
	
	public static void debug(Logger logger, String message, Exception e){
		debug(logger, message);
		debug(logger, e);
	}
}
