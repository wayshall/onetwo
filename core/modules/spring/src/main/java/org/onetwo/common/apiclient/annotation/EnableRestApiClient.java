package org.onetwo.common.apiclient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.onetwo.common.apiclient.impl.RestApiClentRegistrar;
import org.onetwo.common.apiclient.impl.RestExecutorConfiguration;
import org.springframework.context.annotation.Import;

/**
 * 
 * 支持  @PathVariable @RequestBody @RequestParam 注解
 * @PathVariable：解释路径参数
 * @RequestBody：body，一般会转为json，一次请求 只允许一个requestbody
 * @RequestParam：转化为queryString参数
 * 
 * 没有注解的方法参数：如果为get请求，则所有参数都转为queryString参数，效果和使用了@RequestParam一样；
 * 					 如果为post请求，则除了@RequestParam的参数外，自动包装为类型为Map的requestBody
 * 
 * 如果没有指定requestBody，则根据规则查找可以作为requestBody的参数
 * 方法多于一个参数时，使用参数名称作为参数前缀；
 * 只有一个参数的时候，否则前缀为空，直接把对象转化为map作为键值对参数
 * 
 * 
 * get请求忽略requestBody
 * post请求会把非url参数转化为requestBody
 * 
 * consumes -> contentType，指定提交请求的convertor，详见：HttpEntityRequestCallback
 * produces -> acceptHeader，指定accept header，从而通过response的contentType头指定读取响应数据的convertor，详见：ResponseEntityResponseExtractor
 * 
 * <br/><br/>
 * 详见：ApiClientMethod
 * 
 * <br/><br/>
 * 自定义错误处理器，见接口：{@linkplain org.onetwo.common.apiclient.ApiErrorHandler ApiErrorHandler}
 * 
 * <br/><br/>
 * 自定义响应处理器，见接口：{@linkplain org.onetwo.common.apiclient.CustomResponseHandler CustomResponseHandler}
 * 
 * 扩展：<br/>
 * &#064;EnableRestApiClient<br/>
 * &#064;Configuration<br/>
 * class XxxxConfiguration {
 * }
 * <br/><br/>
 * 1，自定义@EnableXxxxApiClient和@XxxxApiClient <br/>
 * 2，自定XxxxApiClentRegistrar <br/>
 * 3，自定义XxxxApiClientFactoryBean <br/>
 * @author wayshall
 * <br/>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({RestApiClentRegistrar.class, RestExecutorConfiguration.class})
public @interface EnableRestApiClient {
	
	String[] basePackages() default {};
	Class<?>[] basePackageClasses() default {};
	
	String baseUrl() default "";
	
//	Class<? extends RestExecutorFactory> restExecutorFactory() default RestExecutorFactory.class;

}
