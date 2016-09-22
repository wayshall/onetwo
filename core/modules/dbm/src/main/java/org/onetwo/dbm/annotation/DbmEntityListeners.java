package org.onetwo.dbm.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.dbm.event.DbmEntityListener;
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DbmEntityListeners {

	Class<? extends DbmEntityListener>[] value();
	
}
