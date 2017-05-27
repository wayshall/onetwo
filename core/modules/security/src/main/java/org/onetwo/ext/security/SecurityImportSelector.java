package org.onetwo.ext.security;


import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.context.AbstractImportSelector;
import org.onetwo.ext.security.EnableSecurity.ConfigOptions;
import org.onetwo.ext.security.config.PermissionContextConfig;
import org.onetwo.ext.security.method.MethodBasedSecurityConfig;
import org.onetwo.ext.security.url.UrlBasedSecurityConfig;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class SecurityImportSelector extends AbstractImportSelector<EnableSecurity> {
	
	@Override
	protected List<String> doSelect(AnnotationMetadata metadata, AnnotationAttributes attributes) {
		ConfigOptions mode = (ConfigOptions)attributes.get("mode");
		List<String> classNames = new ArrayList<>();
		if(mode==ConfigOptions.URL){
			classNames.add(UrlBasedSecurityConfig.class.getName());
		}else if(mode==ConfigOptions.METHOD){
			classNames.add(MethodBasedSecurityConfig.class.getName());
		}else{
			Class<?>[] configClass = (Class<?>[])attributes.get("configClass");
			if(configClass==null || configClass.length==0){
				throw new BaseException("no security config class set!");
			}
			for(Class<?> cls : configClass){
				classNames.add(cls.getName());
			}
		}
		boolean enableJavaStylePermissionManage = (boolean)attributes.get("enableJavaStylePermissionManage");
		if(enableJavaStylePermissionManage){
			classNames.add(PermissionContextConfig.class.getName());
		}
		return classNames;
	}

}
