package org.onetwo.plugins.rest.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.web.bind.annotation.RequestMethod;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RestClient {
	String value();
	RequestMethod method() default RequestMethod.GET;
}
