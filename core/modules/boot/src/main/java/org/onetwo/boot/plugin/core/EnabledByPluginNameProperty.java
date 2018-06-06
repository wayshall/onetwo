package org.onetwo.boot.plugin.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * @author wayshall
 * <br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Conditional(ByPluginNameEanbledCondition.class)
public @interface EnabledByPluginNameProperty {
	
	String property();
	Class<? extends WebPlugin> pluginClass();

}
