package org.onetwo.common.fish.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;

/**********
 * only for named query
 * @author wayshall
 *
 */
@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JFishQueryable {

	String table() default "";
	
}
