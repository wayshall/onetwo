package org.onetwo.dbm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;

/**********
 * only for named query
 * @see {@linkplain org.onetwo.dbm.mapping.MappedType MappedType.QUERYABLE_ONLY}
 * @author wayshall
 *
 */
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface DbmQueryable {

	String table() default "";
	
}
