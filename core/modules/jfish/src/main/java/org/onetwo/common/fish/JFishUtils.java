package org.onetwo.common.fish;

import java.lang.reflect.Method;
import java.util.Locale;

import org.onetwo.common.exception.SystemErrorCode;
import org.onetwo.common.fish.exception.JFishException;
import org.onetwo.common.fish.exception.JFishInvokeRestException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.map.CasualMap;
import org.onetwo.common.web.config.BaseSiteConfig;
import org.slf4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;

final public class JFishUtils {
	private static final Logger logger = JFishLoggerFactory.getLogger(JFishUtils.class);
	
	public static final int WEBAPP_INITIALIZER_ORDER = -1000;
	private static final Locale DEFAULT_LOCAL = Locale.CHINA;

	private JFishUtils(){
	}

	public static JFishException asJFishException(Exception e){
		if(JFishException.class.isInstance(e))
			return (JFishException)e;
		return new JFishException(e);
	}


	public static JFishException asJFishException(String msg, Exception e){
		if(e==null)
			return new JFishException(msg);
		else if(e instanceof JFishException)
			return (JFishException) e;
		
		JFishException se = null;
		if(msg==null)
			se = new JFishException(e.getMessage(), e);
		else
			se = new JFishException(msg, e);
		return se;
	}
	

	public static JFishInvokeRestException asJFishInvokeRestException(Exception e){
		return asJFishInvokeRestException(null, e);
	}

	public static JFishInvokeRestException asJFishInvokeRestException(String msg, Exception e){
		if(e==null){
			return msg==null?new JFishInvokeRestException():new JFishInvokeRestException(msg);
		}else if(e instanceof JFishInvokeRestException){
			return (JFishInvokeRestException) e;
		}
		
		JFishInvokeRestException se = null;
		if(msg==null)
			se = new JFishInvokeRestException(e.getMessage(), e);
		else
			se = new JFishInvokeRestException(msg, e);
		return se;
	}
	
	public static String getControllerPath(Class<? extends AbstractBaseController> controllerClass, String methodName, Object...params){
		Assert.notNull(controllerClass);
		String path = "";
		RequestMapping rm = controllerClass.getAnnotation(RequestMapping.class);
		String p = "";
		if(rm!=null){
			p = StringUtils.getFirstNotBlank(rm.value());
			if(StringUtils.isNotBlank(p)){
				p = StringUtils.trimEndWith(p, "/");
				path += p;
			}
		}
		Method method = ReflectUtils.findMethod(controllerClass, methodName);
		rm = method.getAnnotation(RequestMapping.class);
		p = StringUtils.getFirstNotBlank(rm.value());
		if(StringUtils.isNotBlank(p)){
			p = StringUtils.appendStartWith(p, "/");
			path += p;
		}
		if(BaseSiteConfig.getInstance().hasAppUrlPostfix()){
			path = BaseSiteConfig.getInstance().appendAppUrlPostfix(path);
		}
		if(!LangUtils.isEmpty(params)){
			CasualMap map = new CasualMap(LangUtils.asMap(params));
			path += "?"+map.toParamString();
		}
		return path;
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
}
