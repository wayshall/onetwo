package org.onetwo.boot.plugins.data.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dictionary {

	String name() default "";
}
