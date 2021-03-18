package org.onetwo.boot.module.oauth2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wayshall
 * <br/>
 */

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface OAuth2ClientDetails {
	
	TokenNotFoundActions tokenNotFound() default TokenNotFoundActions.NONE;

	enum TokenNotFoundActions {
		THROWS,
		NONE;
	}
}
