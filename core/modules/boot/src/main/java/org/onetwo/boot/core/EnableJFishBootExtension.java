package org.onetwo.boot.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * @author wayshall
 * <br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({EnableJFishBootExtensionSelector.class})
public @interface EnableJFishBootExtension {
	
	AppcationType appcationType() default AppcationType.WEB_SERVICE;
	boolean enableCommonService() default true;
	boolean enableCacheExtension() default false;
	
	enum AppcationType {
		WEB_SERVICE,
		WEB_UI;
	}
}
