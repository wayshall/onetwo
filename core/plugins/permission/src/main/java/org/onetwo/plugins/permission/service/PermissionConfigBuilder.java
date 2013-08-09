package org.onetwo.plugins.permission.service;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.spring.web.authentic.SpringConfigBuilder;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.s2.security.config.AuthenticConfig;
import org.onetwo.common.web.s2.security.config.annotation.Authentic;
import org.onetwo.plugins.permission.MenuInfoParser;
import org.onetwo.plugins.permission.anno.ByMenuClass;
import org.onetwo.plugins.permission.anno.ByPermissionClass;

public class PermissionConfigBuilder extends SpringConfigBuilder {

	@Resource
	private MenuInfoParser menuInfoParser;
	
	public AuthenticConfig buildAuthenConfig(Class<?> clazz, Method method) {
		AuthenticConfig config = null;
		List<String> perms = LangUtils.newArrayList(3);
		
		if (method.getAnnotation(ByMenuClass.class)!=null) {
			ByMenuClass menu = method.getAnnotation(ByMenuClass.class);
			config = this.buildAuthenticConfig(getAuthenticName(method), ByMenuClass.class.getAnnotation(Authentic.class));

			for(Class<?> codeCls : menu.codeClass()){
				perms.add(menuInfoParser.parseCode(codeCls));
			}
			buildExtAnnotationConfig(config, clazz, method);
			
		}else if(method.getAnnotation(ByPermissionClass.class)!=null){
			ByPermissionClass permClass = method.getAnnotation(ByPermissionClass.class);
			config = this.buildAuthenticConfig(getAuthenticName(method), ByPermissionClass.class.getAnnotation(Authentic.class));

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

}
