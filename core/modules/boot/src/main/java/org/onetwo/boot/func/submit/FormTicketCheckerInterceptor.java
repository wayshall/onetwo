package org.onetwo.boot.func.submit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

/**
 * 需要事先调用SubmitTokenService#get方法生成token,
 * 然后在提交的时候，用此token作为ticket参数的值
 * @author weishao zeng
 * <br/>
 */
public class FormTicketCheckerInterceptor implements MvcInterceptor {
	
	@Autowired
	private SubmitTokenService submitTokenService;
	private String tokenParameterName = "ticket";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		String ticket = request.getParameter(tokenParameterName);
		this.submitTokenService.check(ticket);
		return true;
	}

}

