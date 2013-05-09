package org.onetwo.common.web.s2.security.ext.acl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AclAuthentic {

	String resourceEntity() default "";
	String permissions() default "";	
	String operator() default "";
	String resourceId() default "";
	String entityId()  default "";
	boolean ignore() default false;
	boolean isGetRights() default false;
	
}
