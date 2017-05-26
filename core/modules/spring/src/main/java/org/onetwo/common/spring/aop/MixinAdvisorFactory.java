package org.onetwo.common.spring.aop;

import org.springframework.aop.Advisor;

/**
 * @author wayshall
 * <br/>
 */
public interface MixinAdvisorFactory {
	
	boolean isMixinInterface(Class<?> mixinInterface);

	Advisor createAdvisor(Class<?> mixinInterface);
	
}
