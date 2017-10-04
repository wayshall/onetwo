package org.onetwo.boot.plugin.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

/**
 * @author wayshall
 * <br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(WebPluginImportSelector.class)
@Conditional(WebPluginImportSelector.class)
public @interface JFishWebPlugin {
	
	@AliasFor("webPluginClass")
	Class<? extends WebPlugin> value() default WebPlugin.class;
	
	@AliasFor("value")
	Class<? extends WebPlugin> webPluginClass() default WebPlugin.class;

}
