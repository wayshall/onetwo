package org.onetwo.boot.module.oauth2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * @author wayshall
 * <br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({EnableJFishOauth2Selector.class})
public @interface EnableJFishOauth2 {
	
	OAuth2Role[] value() default {OAuth2Role.AUTHORIZATION_SERVER, OAuth2Role.RESOURCE_SERVER};
	/*boolean authorizationServer() default true;
	boolean resourceServer() default true;*/
	
	enum OAuth2Role {
		AUTHORIZATION_SERVER,
		RESOURCE_SERVER;
	}
}
