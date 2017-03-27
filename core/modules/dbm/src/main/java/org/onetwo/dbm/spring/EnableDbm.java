package org.onetwo.dbm.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({DbmSpringConfiguration.class, DynamicQueryObjectRegisterConfigration.class})
public @interface EnableDbm {
	
	/****
	 * dataSource bean name
	 * @return
	 */
	String value() default "";
	/****
	 * package to scan model and repository
	 * @return
	 */
	String[] packagesToScan() default {};
	
	boolean enableRichModel() default true;
}
