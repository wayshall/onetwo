package org.onetwo.common.spring.mcache;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

public interface CacheKeyGenerator {

	Serializable generateKey(MethodInvocation methodInvocation);
	public Serializable generateKey(Method method, Object... methodArguments);
	
}
