package org.onetwo.common.fish.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.common.fish.orm.MappedType;
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JFishEntity {

//	String name();
	String table();
	
	MappedType type() default MappedType.ENTITY;
	
}
