package org.onetwo.common.web.s2.security;


import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.UserActivityCheckable;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.s2.security.config.AuthenticConfig;
import org.onetwo.common.web.s2.security.config.AuthenticConfigService;
import org.onetwo.common.web.utils.StrutsUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/****
 * 安全验证拦截器
 * 验证用户是否登录和有权限访问当前链接
 * @author weishao
 *
 */
@SuppressWarnings({"serial"})
public class SecurityIntercepter extends MethodFilterInterceptor {

	protected static Logger logger = Logger.getLogger(SecurityIntercepter.class);
	
	public static final String SECURITY_RESULT = SecurityUtils.SECURITY_RESULT;

	private String authenticationName;
	private String authenticConfigServiceName;
	
	private AuthenticConfigService authenticConfigService;
	
	public SecurityIntercepter(){
	}

	public void setAuthenticationName(String authenticationName) {
		this.authenticationName = authenticationName;
	}


	public void setAuthenticConfigServiceName(String authenticConfigServiceName) {
		this.authenticConfigServiceName = authenticConfigServiceName;
	}

	@Override
	public String doIntercept(ActionInvocation invocation) throws Exception {
		String result = null;
		AuthenticConfig config = null;
		try {
			ServletActionContext.getRequest().setAttribute("URL", StrutsUtils.getRequestURI());
			
			/*String cookietoken = CookieUtil.getCookieToken();
			if(SSOService.checkTimeout(authoritable, cookietoken, true))
				throw new NotLoginException("登录超时", SystemErrorCode.LOGIN_ERROR_SESSION_TIMEOUT);*/
			
			// 验证
			AuthenticConfigService service = getAuthenticConfigService();
			
			StrutsSecurityTarget target = new StrutsSecurityTarget(invocation);
			config = service.getConfig(target);
			if(logger.isInfoEnabled())
				logger.info("config: " + config.toString());

			AuthenticationInvocation authentication = service.getAuthenticationInvocation(config);
			UserDetail authoritable = target.getAuthoritable();

			AuthenticationContext context = AuthenticationContext.create(config, target);
			authentication.authenticate(context);
			
			if(UserActivityCheckable.class.isInstance(authentication))
				((UserActivityCheckable)authoritable).setLastActivityTime(new Date());
			
			result = invocation.invoke();
			
		} catch (Exception e) {
			/*if(config!=null && StringUtils.isNotBlank(config.getRedirect())){
				SecurityUtils.setCurrentSecurityResult(config.getRedirect());
			}*/
			throw e;
		}
		
		return result;

	}
	
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
	

	/***
	 * 验证配置管理类
	 * @return
	 */
	public AuthenticConfigService getAuthenticConfigService() {
		if(SiteConfig.getInstance().isProduct() && this.authenticConfigService!=null)
			return this.authenticConfigService;
		
		AuthenticConfigService service = null;
		if(StringUtils.isNotBlank(this.authenticConfigServiceName))
			service = StrutsUtils.getBean(AuthenticConfigService.class, this.authenticConfigServiceName);
		if(service==null && StringUtils.isNotBlank(authenticationName))
			service = StrutsUtils.getBean(AuthenticConfigService.class, this.authenticationName);
		if(service==null)
			service = StrutsUtils.getBean(AuthenticConfigService.class, AuthenticConfigService.NAME);
		
		if(service == null)
			LangUtils.throwBaseException("can not find the AuthenticConfigService : ["+this.authenticConfigServiceName+", " + this.authenticationName+"]");
		this.authenticConfigService = service;
		return this.authenticConfigService;
	}
}
