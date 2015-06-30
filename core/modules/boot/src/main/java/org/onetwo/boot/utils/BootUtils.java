package org.onetwo.boot.utils;

import java.io.IOException;
import java.util.Locale;

import org.onetwo.boot.core.matcher.MatcherUtils;
import org.onetwo.boot.core.matcher.MutipleRequestMatcher;
import org.onetwo.boot.security.CommonReadMethodMatcher;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.slf4j.Logger;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.MessageSource;
import org.springframework.core.env.PropertySource;
import org.springframework.security.web.util.matcher.RequestMatcher;

final public class BootUtils {
	private static final Logger logger = JFishLoggerFactory.getLogger(BootUtils.class);
	
	public static final int WEBAPP_INITIALIZER_ORDER = -1000;
	private static final Locale DEFAULT_LOCAL = Locale.CHINA;
	

	private BootUtils(){
	}
	
	public static Locale getDefaultLocale(){
		return DEFAULT_LOCAL;
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
	
	public static PropertySource<?> loadYaml(String classpath){
		YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
		try {
	        PropertySource<?> props = loader.load("test", SpringUtils.newClassPathResource(classpath), null);
	        return props;
        } catch (IOException e) {
	        throw new BaseException("load yaml file error: " + classpath);
        }
	}

	
	public static RequestMatcher csrfNotMatch(String...paths){
		RequestMatcher m = new CommonReadMethodMatcher();
		MutipleRequestMatcher mutiple = new MutipleRequestMatcher(m);
		return MatcherUtils.notMatcher(mutiple);
	}
}
