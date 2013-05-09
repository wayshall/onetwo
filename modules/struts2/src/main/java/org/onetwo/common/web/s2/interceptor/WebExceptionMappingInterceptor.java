package org.onetwo.common.web.s2.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.ajaxanywhere.AAUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.onetwo.common.exception.AuthenticationException;
import org.onetwo.common.utils.NiceDate;
import org.onetwo.common.utils.SiteInfo;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.s2.AjaxAction;
import org.onetwo.common.web.s2.BaseAction;
import org.onetwo.common.web.s2.ext.LocaleUtils;
import org.onetwo.common.web.s2.ext.PortalConventionUnknowHandler;
import org.onetwo.common.web.s2.security.SecurityIntercepter;
import org.onetwo.common.web.s2.security.SecurityUtils;
import org.onetwo.common.web.utils.RequestTypeUtils;
import org.onetwo.common.web.utils.RequestTypeUtils.RequestType;
import org.onetwo.common.web.utils.SessionUtils;
import org.onetwo.common.web.utils.StrutsUtils;
import org.onetwo.common.web.utils.Tool;
import org.onetwo.common.web.utils.UserKeyManager;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor;

/****
 * 处理请求过程中的各种异常的拦截器
 * 如一般的异常直接跳转到错误也
 * ajax和flash请求的异常则返回json数据等等
 * @author weishao
 *
 */
@SuppressWarnings({"serial", "unchecked"})
public class WebExceptionMappingInterceptor extends ExceptionMappingInterceptor{

	public static final String ERROR_RESULT_PREFIX = "result:";

	public static final String WORKSPACE_PREFIX = "/workspace";
	public static final String ADMIN_PREFIX = "/admin";
	public static final String WORKSPACE_RESULT = "workspace-error";
	
	
	protected Logger logger = Logger.getLogger(this.getClass());

    static boolean devMode = false;
    
    @Inject("devMode")
    public static void setDevMode(String mode) {
        devMode = "true".equals(mode);
    }
    
    protected UserKeyManager getUserKeyManager(){
    	return UserKeyManager.defaultUserKeyManager;
    }
    
    protected void setCurrentUserKeyManager(ActionInvocation invocation){
    	UserKeyManager.setCurrentUserKeyManager(getUserKeyManager());
    }

	@SuppressWarnings("rawtypes")
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		this.setCurrentUserKeyManager(invocation);
		
//		invocation.getStack().getContext().put(Tool.class.getSimpleName(), Tool.getInstance());
//		Tool tool = SpringApplication.getInstance().getBean(Tool.class);
		invocation.getStack().getContext().put(Tool.BEAN_NAME, Tool.getInstance());
		invocation.getStack().getContext().put(Tool.ALIAS_NAME, Tool.getInstance());
		invocation.getStack().getContext().put("niceDate", new NiceDate());
		invocation.getStack().getContext().put(SiteConfig.CONFIG_NAME, SiteConfig.getInstance());
		invocation.getStack().getContext().put(LocaleUtils.DATA_LOCALE_ATTRIBUTE_KEY, StrutsUtils.getDataLanguage());
		SiteInfo siteinfo = SessionUtils.getSiteInfo();
		if(siteinfo!=null)
			invocation.getStack().getContext().put(SiteInfo.KEY, siteinfo);
		
		/*if(invocation.getAction() instanceof CrudBaseAction){
			invocation.addPreResultListener(new SetDataLocaleBeforeResult());
		}*/
		
		String result = super.intercept(invocation);
		Exception e = (Exception) invocation.getStack().findValue("exception");
		
		String reqeustKey = StrutsUtils.getRequest().getHeader(RequestTypeUtils.HEADER_KEY);
		RequestType requestType = RequestTypeUtils.getRequestType(reqeustKey);
		BaseAction action = (BaseAction) invocation.getAction();

//		logger.info("请求： "+StrutsUtils.getRequestURI());
		if(e==null){
			if(RequestType.Ajax.equals(requestType) && action.hasErrors()){
				Map rs = new HashMap();
				rs.put(AjaxAction.MESSAGE_KEY, "操作失败："+ StringUtils.join(action.getActionErrors(), ","));
				rs.put(AjaxAction.MESSAGE_CODE_KEY, AjaxAction.RESULT_FAILED);
				action.outJson(rs);
				return null;
			}
			return result;
		}
		
        	
		if(RequestType.Ajax.equals(requestType)){
			Map rs = new HashMap();
			rs.put(AjaxAction.MESSAGE_KEY, "操作失败："+ e.getMessage());
			rs.put(AjaxAction.MESSAGE_CODE_KEY, AjaxAction.RESULT_FAILED);
			((BaseAction) invocation.getAction()).outJson(rs);
			return null;
		}else if(RequestType.Flash.equals(requestType)){
			Map rs = new HashMap();
			rs.put(AjaxAction.MESSAGE_KEY, "error request: "+e.getMessage());
			StrutsUtils.outJson(rs);
			return null;
		}else if(AAUtils.isAjaxRequest(StrutsUtils.getRequest())){
			logger.error("分页请求发生错误[page request error]", e);
			throw e;
		}
		
		boolean authenticError = false;
		if(e instanceof NoSuchMethodException || e instanceof ConfigurationException){
			ServletActionContext.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
			result = null;
		}else if(e instanceof AuthenticationException){
			String rs = (String) ActionContext.getContext().get(SecurityIntercepter.SECURITY_RESULT);
			if(StringUtils.isNotBlank(rs)){
				result = rs;
			}else{
//				result = StrutsAuthentication.LONGIN_VIEW;
			}
			authenticError = true;
		}else{
			result = this.handLoginException(invocation, e, result);
		}

		String errorMsg = String.format("execute action error : [actionName=%s, actionClass=%s, method=%s", invocation.getProxy().getActionName(), invocation.getAction().toString(), invocation.getProxy().getConfig().getMethodName());
		if(authenticError){
			logger.error("验证错误： "+StrutsUtils.getRequestURI()+", "+errorMsg);
		}else{
			logger.error("请求错误： "+StrutsUtils.getRequestURI()+", "+errorMsg, e);
		}
		
		
		/*String ns = invocation.getProxy().getNamespace();
		if(ns.startsWith(WORKSPACE_PREFIX) || ns.startsWith(ADMIN_PREFIX))
			result = WORKSPACE_RESULT;*/
		
		return result;
	}
	
	public String handLoginException(ActionInvocation invocation, Exception e, String result){
		String rs = result;
		/*BaseAction action = (BaseAction) invocation.getAction();
        Map session = (Map) invocation.getInvocationContext().get(ActionContext.SESSION);
        String message = MyUtils.getCauseServiceException(e).getMessage();
        session.put(SystemErrorCode.APP_ERROR_MESSAGE, message);
        action.addActionMessage(message);*/

		/*boolean isLoginError = false;
		boolean isNoAuthentication = false;
		if(e instanceof LoginException){
			isLoginError = true;
		}else if(e instanceof NotLoginException){
			isLoginError = true;
		}else if(e instanceof AuthenticationException){
			isNoAuthentication = true;
		}
		else if(e instanceof ServiceException){
			String code = ((ServiceException) e).getCode();
			if(StringUtils.isNotBlank(code) && code.startsWith(SystemErrorCode.ES_LOGIN_FAILED)){
				isLoginError = true;
			}
		}
		if(isLoginError){
			StrutsUtils.removeCurrentLoginUser();
			CookieUtil.removeAllCookies();
			
			String errorCode = ((ServiceException)e).getCode();
			invocation.getStack().getContext().put("errorCode", errorCode);
			invocation.getStack().getContext().put("errorMsg", e.getMessage());
//			action.addActionError(e.getMessage());
			
			rs = SecurityUtils.getCurrentSecurityResult(UserKeyManager.getCurrentUserKeyManager().getSecurityResult());
		}else if(isNoAuthentication){
			rs = findErrorResult(invocation, rs);
		}else{*/
		
		
		rs = findErrorResult(invocation, rs);
		
		//if can not find error result, process by siteConfig
		if(SiteConfig.getInstance().isProduct() || StringUtils.isBlank(rs)){
			int statusCode = SiteConfig.getInstance().getErrorPageCode();
			if(statusCode==-1)
				statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			try {
				ServletActionContext.getResponse().sendError(statusCode);
				rs = null;//返回null
			} catch (IOException e1) {
				logger.error("send status error: " + statusCode, e1);
			}
			return null;
		}
		
		
//		}
		return processResult(invocation, rs);
	}
	
	protected String findErrorResult(ActionInvocation invocation, String def){
		String globalResult = StrutsUtils.findInputConfig(invocation, null);//1 find input
		if(StringUtils.isBlank(globalResult))
			globalResult = (String)invocation.getStack().getContext().get(BaseAction.GLOBAL_RESULT);//2 find global-result
		/*if(StringUtils.isBlank(globalResult))
			globalResult = SiteConfig.getInstance().getErrorPage().trim();*///3
		if(StringUtils.isBlank(globalResult))
			globalResult = SecurityUtils.getCurrentSecurityResult(null); //3 find suecurity-result
		if(StringUtils.isBlank(globalResult)) // 4 default
			globalResult = def;
		
		return globalResult;
	}
	
	protected String processResult(ActionInvocation invocation, String globalResult){
		if(StringUtils.isBlank(globalResult))
			return null;
		if(globalResult.startsWith(PortalConventionUnknowHandler.REDIRECT_PREFIX)){
			String path = PortalConventionUnknowHandler.handleRedirectPath(invocation, globalResult);
			StrutsUtils.redirect(path);
			globalResult = null;
		}else if(globalResult.startsWith(PortalConventionUnknowHandler.FORWARD_PREFIX)){
			String path = PortalConventionUnknowHandler.handleForwardPath(invocation, globalResult);
			if(SiteConfig.inst().isStrutsAutoLanguage())
				path = "/"+SiteConfig.getInstance().getLanguage()+path;
			StrutsUtils.forward(path);
			globalResult = null;
		}
		
		return globalResult;
	}
	
}
