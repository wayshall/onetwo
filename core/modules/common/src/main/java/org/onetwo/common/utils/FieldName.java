package org.onetwo.common.utils;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({METHOD, FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldName {

	String value();
	
}
