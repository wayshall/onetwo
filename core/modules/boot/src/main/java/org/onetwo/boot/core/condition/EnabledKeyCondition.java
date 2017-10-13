package org.onetwo.boot.core.condition;

import org.onetwo.boot.plugin.core.JFishWebPlugin;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author wayshall
 * <br/>
 */
abstract public class EnabledKeyCondition extends SpringBootCondition
									implements EnvironmentAware, BeanClassLoaderAware {

	
	private Environment environment;
	private ClassLoader beanClassLoader;


	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		AnnotationAttributes attrubutes = getAnnotationAttributes(metadata);
		String key = getEnabledKey(environment, attrubutes);
		if(!isEnabled(environment, key)){
			return ConditionOutcome.noMatch("property ["+key+"] is not enabled");
		}
		return ConditionOutcome.match();
	}

	abstract protected String getEnabledKey(Environment environment, AnnotationAttributes attrubutes);
	
	protected boolean isEnabled(Environment environment, String key){
		return new RelaxedPropertyResolver(environment).getProperty(key, Boolean.class, Boolean.TRUE);
	}
	
	protected AnnotationAttributes getAnnotationAttributes(AnnotatedTypeMetadata metadata){
		//support @AliasFor
		AnnotationAttributes attributes = SpringUtils.getAnnotationAttributes(metadata, JFishWebPlugin.class);
		return attributes;
	}
	
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public ClassLoader getBeanClassLoader() {
		return beanClassLoader;
	}

	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

}
