package org.onetwo.common.spring.condition;

import java.util.Map;
import java.util.stream.Stream;

import org.onetwo.common.utils.LangUtils;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author wayshall
 * <br/>
 */
public class OnBeanCondition implements ConfigurationCondition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		if(metadata.isAnnotated(OnMissingBean.class.getName())){
			Map<String, Object> attrs = metadata.getAnnotationAttributes(OnMissingBean.class.getName());
			Class<?>[] missingTypes = (Class<?>[])attrs.get("value");
			if(LangUtils.isEmpty(missingTypes)){
				return true;
			}
			return Stream.of(missingTypes)
							.allMatch(type->{
								String[] beanNames = context.getBeanFactory().getBeanNamesForType(type);
								return LangUtils.isEmpty(beanNames);
							});
		}
		if (metadata.isAnnotated(OnExistingBean.class.getName())){
			Map<String, Object> attrs = metadata.getAnnotationAttributes(OnExistingBean.class.getName());
			Class<?>[] existingTypes = (Class<?>[])attrs.get("value");
			if(LangUtils.isEmpty(existingTypes)){
				return true;
			}
			return Stream.of(existingTypes)
							.anyMatch(type->{
								String[] beanNames = context.getBeanFactory().getBeanNamesForType(type);
								return !LangUtils.isEmpty(beanNames);
							});
		}
		return true;
	}

	@Override
	public ConfigurationPhase getConfigurationPhase() {
		return ConfigurationPhase.REGISTER_BEAN;
	}
	
	

}
