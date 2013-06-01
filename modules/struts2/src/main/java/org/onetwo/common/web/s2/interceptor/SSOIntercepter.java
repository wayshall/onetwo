package org.onetwo.common.web.s2.interceptor;

import javax.annotation.Resource;

import org.onetwo.common.sso.SSOService;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.utils.CookieUtil;
import org.onetwo.common.web.utils.StrutsUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/***
 * sso单点登录拦截器
 * @author weishao
 *
 */
@SuppressWarnings({"unchecked", "serial"})
@Deprecated
public class SSOIntercepter /*extends MethodFilterInterceptor*/{

	protected SSOService SSOService;
	
	//@Override
	public String doIntercept(ActionInvocation invocation) throws Exception{
		UserDetail authoritable = StrutsUtils.getCurrentLoginUser();
		
		String cookietoken = CookieUtil.getCookieToken();
//		System.out.println("======>>>>>  cookietoken : " + cookietoken);
		/*if (StringUtils.isBlank(cookietoken))
			cookietoken = StrutsUtils.getRequest().getParameter(CookieUtil.COOKIE_TOKENNAME);*/
		
//		SSOService.checkLogin(authoritable, cookietoken);
		
		return invocation.invoke();
	}
	/*
    protected boolean applyInterceptor(ActionInvocation invocation) {
        boolean applyMethod = super.applyInterceptor(invocation);
        if(!applyMethod)
        	return applyMethod;
        String method = invocation.getProxy().getMethod();
        String action = MyUtils.append("action[", invocation.getProxy().getActionName(), ".", method, "]");
        if(excludeMethods.contains(action)){
        	applyMethod = false;
        }
        return applyMethod;
    }
	*/
	//@Resource
	public void setSSOService(SSOService service) {
		SSOService = service;
	}
	
}
