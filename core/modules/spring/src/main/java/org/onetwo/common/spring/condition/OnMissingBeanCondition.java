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
public class OnMissingBeanCondition implements ConfigurationCondition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String annotationName = OnMissingBean.class.getName();
		Map<String, Object> attrs = metadata.getAnnotationAttributes(annotationName);
//		MultiValueMap<String, Object> attrsValues = metadata.getAllAnnotationAttributes(annotationName);
		Class<?>[] missingTypes = (Class<?>[])attrs.get("value");
		return Stream.of(missingTypes)
						.allMatch(type->{
							String[] beanNames = context.getBeanFactory().getBeanNamesForType(type);
							return LangUtils.isEmpty(beanNames);
						});
	}

	@Override
	public ConfigurationPhase getConfigurationPhase() {
		return ConfigurationPhase.REGISTER_BEAN;
	}
	
	

}
