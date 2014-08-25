package org.onetwo.common.spring.web.reqvalidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UploadFileTypeValidator {
	String[] allowed() default {};
	String message() default "It's not allowed file type.";
}
