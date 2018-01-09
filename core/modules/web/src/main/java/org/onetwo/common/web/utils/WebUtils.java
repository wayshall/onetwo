package org.onetwo.common.web.utils;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.exception.ExceptionCodeMark;
import org.onetwo.common.spring.mvc.utils.DataResults.SimpleResultBuilder;


final public class WebUtils {

//	public static final String DEFAULT_TOKEN_FIELD_NAME = "org.onetwo.jfish.form.token";

	public static final String FORWARD_KEY = "forward:";
	public static final String REDIRECT_KEY = "redirect:";
	
	private WebUtils(){}
	

	public static boolean hasForwardPrefix(String path){
		return path.startsWith(FORWARD_KEY);
	}
	
	public static String getNoForward(String path){
		if(path.startsWith(FORWARD_KEY)){
			return path.substring(FORWARD_KEY.length());
		}
		return path;
	}

	public static String forwardPrefix(String path){
		if(!path.startsWith(FORWARD_KEY)){
			return FORWARD_KEY + path;
		}
		return path;
	}

	public static String redirectPrefix(String path){
		if(!path.startsWith(REDIRECT_KEY)){
			return REDIRECT_KEY + path;
		}
		return path;
	}
	
	public static <T> SimpleResultBuilder<T> buildErrorCode(SimpleResultBuilder<T> builder, HttpServletRequest request, Exception exception){
		if(ExceptionCodeMark.class.isInstance(exception)){
			String code = ((ExceptionCodeMark)exception).getCode();
			builder.code(code);
		}else{
			String key =  exception.getClass().getSimpleName();
			Object codeValue = Optional.ofNullable(request.getAttribute(key)).orElse(key);
			builder.code(codeValue.toString());
		}
		return builder;
	}
	
}
