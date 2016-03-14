package org.onetwo.common.jfishdbm.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.common.jfishdbm.event.DbmEntityListener;
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DbmEntityListeners {

	Class<? extends DbmEntityListener>[] value();
	
}
