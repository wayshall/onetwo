package org.onetwo.common.ejb.jpa.anno;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.onetwo.common.db.event.EntityAction;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface EntityCascade {
	
	EntityAction[] action();

}
