package org.onetwo.cloud.feign;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * @author wayshall
 * <br/>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnhanceFeignClient {

	@AliasFor("value")
	String basePath() default "";

	/***
	 * 给feign加上basePath，默认根据FeignClient的name查找配置
	 * @author wayshall
	 * @return
	 */
	@AliasFor("basePath")
	String value() default "";
}
