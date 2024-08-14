package org.onetwo.dbm.ui;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({EnableDbmUISelector.class})
public @interface EnableDbmUI {
	
	/****
	 * package to scan model and repository
	 * @return
	 */
	String[] packagesToScan() default {};

	Class<?>[] basePackageClasses() default {};
	
}
