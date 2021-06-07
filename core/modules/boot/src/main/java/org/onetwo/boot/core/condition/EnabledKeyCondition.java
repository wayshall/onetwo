package org.onetwo.boot.core.condition;

import org.onetwo.boot.plugin.core.JFishWebPlugin;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author wayshall
 * <br/>
 */
abstract public class EnabledKeyCondition extends SpringBootCondition implements BeanClassLoaderAware {

	
	private ClassLoader beanClassLoader;
	private boolean defaultEnabledValue = true;

	final public void setDefaultEnabledValue(boolean defaultEnabledValue) {
		this.defaultEnabledValue = defaultEnabledValue;
	}

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
		AnnotationAttributes attrubutes = getAnnotationAttributes(metadata);
		
		String mainKey = getMainEnabledKey(context.getEnvironment(), attrubutes);
		if(StringUtils.isNotBlank(mainKey) && !isEnabled(context.getEnvironment(), mainKey)){
			return ConditionOutcome.noMatch("main property ["+mainKey+"] is not enabled");
		}
		
		String key = getEnabledKey(context.getEnvironment(), attrubutes);
		if(!isEnabled(context.getEnvironment(), key)){
			return ConditionOutcome.noMatch("property ["+key+"] is not enabled");
		}
		return ConditionOutcome.match("property ["+key+"] is enabled");
	}

	/****
	 * 主开关
	 * @author weishao zeng
	 * @param environment
	 * @param attrubutes
	 * @return
	 */
	protected String getMainEnabledKey(Environment environment, AnnotationAttributes attrubutes) {
		return "";
	}
	
	/***
	 * 次开关
	 * @author weishao zeng
	 * @param environment
	 * @param attrubutes
	 * @return
	 */
	abstract protected String getEnabledKey(Environment environment, AnnotationAttributes attrubutes);
	
	protected boolean isEnabled(Environment environment, String key){
//		return new RelaxedPropertyResolver(environment).getProperty(key, Boolean.class, defaultEnabledValue);
		// upgrade-sb2: 
		return environment.getProperty(key, Boolean.class, defaultEnabledValue);
	}
	
	protected AnnotationAttributes getAnnotationAttributes(AnnotatedTypeMetadata metadata){
		//support @AliasFor
		AnnotationAttributes attributes = SpringUtils.getAnnotationAttributes(metadata, JFishWebPlugin.class);
		return attributes;
	}
	
	public ClassLoader getBeanClassLoader() {
		return beanClassLoader;
	}

	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

}
