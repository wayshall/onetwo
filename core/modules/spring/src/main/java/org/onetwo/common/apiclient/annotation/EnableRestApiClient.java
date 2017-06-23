package org.onetwo.common.apiclient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.common.apiclient.impl.RestApiClentRegistrar;
import org.springframework.context.annotation.Import;

/**
 * &#064;EnableRestApiClient
 * &#064;Configuration
 * class XxxxConfiguration {
 * }
 * <br/>
 * 1，自定义@EnableXxxxApiClient和@XxxxApiClient
 * 2，自定XxxxApiClentRegistrar
 * 3，自定义XxxxApiClientFactoryBean
 * @author wayshall
 * <br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({RestApiClentRegistrar.class})
public @interface EnableRestApiClient {
	
	String[] basePackages() default {};
	Class<?>[] basePackageClasses() default {};
	
	String baseUrl() default "";

}
