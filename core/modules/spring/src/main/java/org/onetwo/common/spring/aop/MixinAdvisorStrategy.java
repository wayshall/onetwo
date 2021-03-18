package org.onetwo.common.spring.aop;

import org.springframework.aop.Advisor;

/**
 * mixin策略
 * @author wayshall
 * <br/>
 */
public interface MixinAdvisorStrategy {
	
	boolean isMixinInterface(Class<?> mixinInterface);

	/***
	 * 根据规则（接口名称+Impl或注解@Mixin）查找mixin接口（mixinInterface）的实现类
	 * 并创建Advisor
	 * @author weishao zeng
	 * @param mixinInterface
	 * @return
	 */
	Advisor createAdvisor(Class<?> mixinInterface);
	
}
