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
	 * max upload size for item
	 * @author wayshall
	 * @return
	 */
	int maxUploadSize() default 1024*1024*10;//byte
//	String maxUploadSizeErrorMessage() default "error max upload file size!";
}
