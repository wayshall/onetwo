package org.onetwo.common.db.anno;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.onetwo.common.db.DashReplacement;
import org.onetwo.common.db.EntityMonitorAction;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface AutoUUID {

//	DashReplacement dashReplace() default DashReplacement.None; 
	String dashReplace() default DashReplacement.None;
	EntityMonitorAction[] on() default EntityMonitorAction.PrePersist;
	
}
