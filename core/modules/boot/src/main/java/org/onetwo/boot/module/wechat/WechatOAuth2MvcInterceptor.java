package org.onetwo.boot.module.wechat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.mvc.interceptor.MvcInterceptorAdapter;
import org.onetwo.ext.apiclient.wechat.serve.web.WechatOAuth2Hanlder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;

/**
 * @author wayshall
 * <br/>
 */
public class WechatOAuth2MvcInterceptor extends MvcInterceptorAdapter {
	
	@Autowired
	private WechatOAuth2Hanlder wechatOAuth2Hanlder;

	@Override
	public void preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
		wechatOAuth2Hanlder.preHandle(request, response, handler);
	}
}
