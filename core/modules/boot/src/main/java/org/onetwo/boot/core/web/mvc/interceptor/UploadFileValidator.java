package org.onetwo.boot.core.web.mvc.interceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UploadFileValidator {
	boolean nullable() default true;
	String nullableErrorMessage() default "please select uplolad file!";
	
	String[] allowedPostfix() default {"jpg", "jpeg", "gif", "png", "bmp", "xls", "xlsx", "pdf", "doc", "txt"};
	String allowedPostfixErrorMessage() default "It's not allowed file type.";
	
	/****
	 * 每个文件上传大小限制，注意这个值不能超过容器的最大值
	 * 
	 * 这里的判断是通过拦截器判断，在这个之前还有配置限制，见：
	 * spring.http.multipart.maxFileSize
	 * spring.http.multipart.maxRequestSize
	 * 
	 * @author wayshall
	 * @return
	 */
	int maxUploadSize() default 1024*1024*10;//byte
	/***
	 * 通过配置文件来配置大小，如：module.upload.maxUploadSize: 50k
	 * 优先级比maxUploadSize高
	 * @author weishao zeng
	 * @return
	 */
	String maxUploadSizeConfigKey() default "";//byte
//	String maxUploadSizeErrorMessage() default "error max upload file size!";
	
}
