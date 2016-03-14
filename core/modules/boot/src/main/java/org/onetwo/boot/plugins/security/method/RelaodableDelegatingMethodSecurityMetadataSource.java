package org.onetwo.boot.plugins.security.method;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.onetwo.common.reflect.ReflectUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.MethodSecurityMetadataSource;

public class RelaodableDelegatingMethodSecurityMetadataSource implements MethodSecurityMetadataSource {
	final private MethodSecurityMetadataSource delegatingMethodSecurityMetadataSource;
	private boolean debug = true;

	public RelaodableDelegatingMethodSecurityMetadataSource(MethodSecurityMetadataSource methodSecurityMetadataSource) {
		super();
		this.delegatingMethodSecurityMetadataSource = methodSecurityMetadataSource;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		if(debug){
			Map<?, ?> attributeCache = (Map<?, ?>)ReflectUtils.getFieldValue(delegatingMethodSecurityMetadataSource, "attributeCache");
			attributeCache.clear();
		}
		return delegatingMethodSecurityMetadataSource.getAttributes(object);
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return delegatingMethodSecurityMetadataSource.getAllConfigAttributes();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return delegatingMethodSecurityMetadataSource.supports(clazz);
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Method method, Class<?> targetClass) {
		return delegatingMethodSecurityMetadataSource.getAttributes(method, targetClass);
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	
}
