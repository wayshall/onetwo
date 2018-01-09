package org.onetwo.boot.core.web.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * @author wayshall
 * <br/>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface WebApi {

	@AliasFor("prefixPath")
	String value() default "${jfish.webApi.prefixPath:/api}";
	@AliasFor("value")
	String prefixPath() default "${jfish.webApi.prefixPath:/api}";

}
