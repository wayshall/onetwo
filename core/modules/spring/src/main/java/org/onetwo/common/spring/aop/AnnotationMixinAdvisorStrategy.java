package org.onetwo.common.spring.aop;

import org.springframework.aop.Advisor;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author wayshall
 * <br/>
 */
public class AnnotationMixinAdvisorStrategy extends ClassNamePostfixMixinAdvisorStrategy implements MixinAdvisorStrategy {
	
	@Override
	public boolean isMixinInterface(Class<?> interfaceClass) {
		return interfaceClass.isInterface() && AnnotationUtils.findAnnotation(interfaceClass, Mixin.class)!=null;
	}


	@Override
	public Advisor createAdvisor(Class<?> mixinInterface) {
		Mixin mixin = AnnotationUtils.findAnnotation(mixinInterface, Mixin.class);
		if(mixin==null){
			throw new IllegalArgumentException("@Mixin not found on interface: " + mixinInterface);
		}
		MixinAttrs attrs = new MixinAttrs(mixin.value(), mixin.from());
		return createMixinAdvisor(mixinInterface, attrs);
	}
	
}
