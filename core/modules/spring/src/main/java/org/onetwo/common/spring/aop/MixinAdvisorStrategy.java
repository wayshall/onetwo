package org.onetwo.common.spring.aop;

import org.springframework.aop.Advisor;

/**
 * mixin策略
 * @author wayshall
 * <br/>
 */
public interface MixinAdvisorStrategy {
	
	boolean isMixinInterface(Class<?> mixinInterface);

	Advisor createAdvisor(Class<?> mixinInterface);
	
}
