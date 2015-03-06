package org.onetwo.common.web.s2.security.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.s2.security.Authenticator;
import org.onetwo.common.web.s2.security.config.annotation.Authentic;

abstract public class AbstractConfigBuilder {

//	private Class<?> clazz;
//	private Method method;
//	private AuthenticConfig config;

	/*public AbstractConfigBuilder(Class<?> clazz, Method method) {
		this.clazz = clazz;
		this.method = method;
	}*/

	/*public Class<?> getAuthenticClass(){
		return clazz;
	}

	public Method getAuthenticMethod(){
		return method;
	}
	
	public AuthenticConfig retriveConfig(){
		return config;
	}*/
	
	public AuthenticConfig buildAuthenConfig(Class<?> clazz, Method method) {
		AuthenticConfig config = null;
		Authentic authentic = findAnnotation(clazz, method, Authentic.class);
		if (authentic == null) {
			config = this.getDefaultAuthenticConfig();
			return config;
		}

		config = buildAuthenticConfig(getAuthenticName(method), authentic);
		buildExtAnnotationConfig(config, clazz, method);

		return config;
	}
	
	protected String getAuthenticName(Method method){
		return method.toGenericString();
	}
	
	protected AuthenticConfig getDefaultAuthenticConfig(){
		return AuthenticConfig.DEFAULT_CONIFG;
	}

	protected AuthenticConfig newAuthenticConfig(String name, Authentic authentic){
		AuthenticConfig config = new AuthenticConfig(name, authentic.isOnlyAuthenticator());
		return config;
	}
	
	protected AuthenticConfig buildAuthenticConfig(String name, Authentic authentic){
		AuthenticConfig config = newAuthenticConfig(name, authentic);
		
		config.setType(AuthenticConfig.TYPE_ANNOTATION);
		config.setCheckLogin(authentic.checkLogin());
		config.setCheckTimeout(authentic.checkTimeout());
		config.setThrowIfTimeout(authentic.throwIfTimeout());
		config.setIgnore(authentic.ignore());
		config.setAuthenticationName(authentic.authenticationName());
		config.setRedirect(authentic.redirect());
		config.setPermissions(authentic.permissions());
		config.setRoles(authentic.roles());

		buildAuthenticator(config, authentic);
		
		return config;
	}
	
	protected void buildExtAnnotationConfig(AuthenticConfig config, Class<?> clazz, Method method) {
		//TODO
	}


	protected void buildAuthenticator(AuthenticConfig config, Authentic authentic) {
		String[] authenticatorNames = authentic.authenticator();
		if (authenticatorNames != null && authenticatorNames.length > 0) {
			for (String name : authenticatorNames) {
				Authenticator authenticator = getAuthenticator(name);
				config.addAuthenticator(authenticator);
			}
		}
		Class<? extends Authenticator>[] classes = authentic.authenticatorClass();
		this.buildAuthenticatorClasses(config, classes);
		/*if (!LangUtils.isEmpty(classes)) {
			for (Class<? extends Authenticator> cls : classes) {
				Authenticator authenticator = (Authenticator)getAuthenticator(cls);
				config.addAuthenticator(authenticator);
			}
		}*/
	}
	
	protected void buildAuthenticatorClasses(AuthenticConfig config, Class<? extends Authenticator>[] classes){
		if (!LangUtils.isEmpty(classes)) {
			for (Class<? extends Authenticator> cls : classes) {
				Authenticator authenticator = (Authenticator)getAuthenticator(cls);
				config.addAuthenticator(authenticator);
			}
		}
	}

	abstract protected Authenticator getAuthenticator(String name);
	abstract protected <T extends Authenticator> T getAuthenticator(Class<T> cls);

	protected <T extends Annotation> T findAnnotation(Class<?> clazz, Method method, Class<T> annotationClass) {
		return AnnotationUtils.findAnnotationWithStopClass(clazz, method, annotationClass, new Class[]{Object.class});
	}

}
