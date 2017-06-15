package org.onetwo.common.spring.aop;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author wayshall
 * <br/>
 */
public interface MixinableInterfaceCreator {

	Object createMixinObject(MethodInterceptor interceptor);

	void addMixinInterfaces(Class<?>... mixinInterfaces);

}