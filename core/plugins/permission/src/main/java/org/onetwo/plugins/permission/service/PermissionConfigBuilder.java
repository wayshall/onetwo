package org.onetwo.plugins.permission.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.spring.web.AbstractBaseController;
import org.onetwo.common.spring.web.authentic.SpringConfigBuilder;
import org.onetwo.common.utils.AnnotationUtils;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.s2.security.config.AuthenticConfig;
import org.onetwo.common.web.s2.security.config.annotation.Authentic;
import org.onetwo.plugins.permission.MenuInfoParser;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.onetwo.plugins.permission.anno.ByFunctionClass;

public class PermissionConfigBuilder extends SpringConfigBuilder {

	@Resource
	private MenuInfoParser menuInfoParser;
	
	public AuthenticConfig buildAuthenConfig(Class<?> clazz, Method method) {
		AuthenticConfig config = null;
		List<String> perms = LangUtils.newArrayList(3);
		
		if (method.getAnnotation(ByMenuClass.class)!=null) {
			ByMenuClass menu = method.getAnnotation(ByMenuClass.class);
			
			/*Authentic authentic = method.getAnnotation(Authentic.class);
			if(authentic!=null){
				config = this.buildAuthenticConfig(getAuthenticName(method), authentic);
			}else{
				config = this.buildAuthenticConfig(getAuthenticName(method), ByMenuClass.class.getAnnotation(Authentic.class));
			}*/
			config = buildConfigByAuthenticAnnotation(method, ByMenuClass.class);

			for(Class<?> codeCls : menu.codeClass()){
				perms.add(menuInfoParser.parseCode(codeCls));
			}
			buildExtAnnotationConfig(config, clazz, method);
			
		}else if(method.getAnnotation(ByFunctionClass.class)!=null){
			ByFunctionClass permClass = method.getAnnotation(ByFunctionClass.class);
			
//			config = this.buildAuthenticConfig(getAuthenticName(method), ByFunctionClass.class.getAnnotation(Authentic.class));
			config = buildConfigByAuthenticAnnotation(method, ByFunctionClass.class);

			for(Class<?> codeCls : permClass.codeClass()){
				perms.add(menuInfoParser.parseCode(codeCls));
			}
			buildExtAnnotationConfig(config, clazz, method);
			
		}else{
			config = super.buildAuthenConfig(clazz, method);
		}

		if(!perms.isEmpty()){
			String[] permissionCodes = perms.toArray(new String[perms.size()]);
			if(config.getPermissions()!=null)
				permissionCodes = (String[])ArrayUtils.addAll(permissionCodes, config.getPermissions());
			config.setPermissions(permissionCodes);
		}

		return config;
	}
	
	protected AuthenticConfig buildConfigByAuthenticAnnotation(Method method, Class<? extends Annotation> annotationClass){
		AuthenticConfig config = null;
//		Authentic authentic = method.getAnnotation(Authentic.class);
		Authentic authentic = AnnotationUtils.findAnnotationWithStopClass(method.getDeclaringClass(), method, Authentic.class, AbstractBaseController.class);
		if(authentic!=null){
			config = this.buildAuthenticConfig(getAuthenticName(method), authentic);
		}else{
			config = this.buildAuthenticConfig(getAuthenticName(method), annotationClass.getAnnotation(Authentic.class));
		}
		return config;
	}

}
