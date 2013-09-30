package org.onetwo.common.web.exception;

import java.lang.reflect.Method;

import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.BaseSiteConfig;

final public class ExceptionUtils {
	
	public static class WhenExceptionMap {
		private String page;
		private Class<?> throwableClass;
		
		public String getPage() {
			return page;
		}
		public Class<?> getThrowableClass() {
			return throwableClass;
		}
		public boolean match(Exception e){
			return this.throwableClass.isInstance(e);
		}
	}
	
	public static class ExceptionView {
		public static final String AUTHENTIC = "error_authentic";
//		public static final String BUSINESS = "error_business";
//		public static final String SERVICE = "error_service";
		public static final String CODE_EXCEPTON = "message";
		public static final String SYS_BASE = "error_base";
		public static final String UNDEFINE = "error";
	}
	
	
	private ExceptionUtils(){
	}
	
	public static WhenExceptionMap findWhenException(Class<?> clazz, Method method){
		WhenException we = AnnotationUtils.findAnnotationWithStopClass(clazz, method, WhenException.class, Object.class);
		if(we==null)
			return null;
		WhenExceptionMap wm = new WhenExceptionMap();
		wm.page = we.value();
		wm.throwableClass = we.throwableClass();
		return wm;
	}

	public static String findInSiteConfig(Exception e){
		Class<?> eclass = e.getClass();
		String viewName = null;
		while(eclass!=null && Throwable.class.isAssignableFrom(eclass)){
			viewName = BaseSiteConfig.getInstance().getProperty(eclass.getName());
			if(StringUtils.isNotBlank(viewName))
				return viewName;
			if(eclass.getSuperclass()!=null){
				eclass = eclass.getSuperclass();
			}
		} 
		return viewName;
	}
}
