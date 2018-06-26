package org.onetwo.cloud.feign;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * 插件机制会检测到当前controller的接口如果有@EnhanceFeignClient 注解时，不会插入插件路径。
 * 所以，最好加载最顶层的api接口，而非client接口，避免使用框架的插件开发功能时，自动注入了插件路径，污染feign的调用路径
 * @author wayshall
 * <br/>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Inherited
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
	
	Class<?> local() default void.class;
}
