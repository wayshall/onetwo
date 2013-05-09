package org.onetwo.plugins.fmtag.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JFieldViewMeta {

	String label() default "";
	int order() default 0;
	org.onetwo.plugins.fmtag.JFieldShowable[] showable() default {org.onetwo.plugins.fmtag.JFieldShowable.grid, org.onetwo.plugins.fmtag.JFieldShowable.create, org.onetwo.plugins.fmtag.JFieldShowable.update, org.onetwo.plugins.fmtag.JFieldShowable.show};
}
