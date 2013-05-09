package org.onetwo.common.web.s2.security.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.web.s2.BaseAction;
import org.onetwo.common.web.s2.security.Authenticator;
import org.onetwo.common.web.utils.StrutsUtils;

import com.opensymphony.xwork2.ActionSupport;

public class ConfigBuilder extends AbstractConfigBuilder {

	public ConfigBuilder(Class<?> clazz, Method method) {
		super(clazz, method);
	}

	@Override
	protected Authenticator getAuthenticator(String name) {
		return StrutsUtils.getBean(Authenticator.class, name);
	}

	protected <T extends Authenticator> T getAuthenticator(Class<T> cls) {
		return (T)StrutsUtils.getBean(cls);
	}
	
	protected <T extends Annotation> T findAnnotation(Class<?> clazz, Method method, Class<T> annotationClass) {
		return AnnotationUtils.findAnnotationWithStopClass(clazz, method, annotationClass, new Class[]{Object.class, ActionSupport.class, BaseAction.class});
	}

}
