package org.onetwo.boot.plugin.mvc.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.boot.plugin.core.WebPlugin;
import org.springframework.stereotype.Controller;

@Target({TYPE, METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Controller
public @interface WebPluginController {
	
	Class<? extends WebPlugin> value();
//	String contextPath();

}
