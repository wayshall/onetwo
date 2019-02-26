package org.onetwo.boot.utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.slf4j.Logger;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.PropertySource;
import org.springframework.util.ClassUtils;

final public class BootUtils {
	private static final Logger logger = JFishLoggerFactory.getLogger(BootUtils.class);
	

	private static final boolean isHystrixErrorPresent = ClassUtils.isPresent("com.netflix.hystrix.exception.HystrixRuntimeException", null);
	
	public static final int WEBAPP_INITIALIZER_ORDER = -1000;
//	private static final Locale DEFAULT_LOCAL = Locale.CHINA;
	

	private BootUtils(){
	}
	
	public static boolean isHystrixErrorPresent() {
		return isHystrixErrorPresent;
	}

	public static boolean isDmbPresent(){
		return ClassUtils.isPresent("org.onetwo.dbm.spring.DbmSpringConfiguration", ClassUtils.getDefaultClassLoader());
	}
	
	public static Locale getDefaultLocale(){
//		return DEFAULT_LOCAL;
		return LocaleContextHolder.getLocale();
	}

	public static String getMessage(MessageSource exceptionMessage, String code, Object[] args) {
		if(exceptionMessage==null)
			return "";
		try {
			return exceptionMessage.getMessage(code, args, getDefaultLocale());
		} catch (Exception e) {
			logger.error("getMessage ["+code+"] error :" + e.getMessage(), e);
		}
		return "";//SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
	}
	
	public static List<PropertySource<?>> loadYaml(String classpath){
		YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
		try {
			List<PropertySource<?>> props = loader.load(classpath, SpringUtils.newClassPathResource(classpath));
	        return props;
        } catch (IOException e) {
	        throw new BaseException("load yaml file error: " + classpath);
        }
	}
	
}
