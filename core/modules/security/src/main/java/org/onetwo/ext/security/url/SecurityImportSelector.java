package org.onetwo.ext.security.url;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.ext.security.EnableSecurity;
import org.onetwo.ext.security.EnableSecurity.InterceptMode;
import org.onetwo.ext.security.config.PermissionContextConfig;
import org.onetwo.ext.security.method.MethodBasedSecurityConfig;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class SecurityImportSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableSecurity.class.getName(), false);
		InterceptMode mode = (InterceptMode)attributes.get("mode");
		List<String> classNames = new ArrayList<>();
		if(mode==InterceptMode.URL){
			classNames.add(UrlBasedSecurityConfig.class.getName());
		}else if(mode==InterceptMode.METHOD){
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
		return classNames.toArray(new String[0]);
	}
	

}
