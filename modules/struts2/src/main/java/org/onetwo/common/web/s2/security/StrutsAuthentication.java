package org.onetwo.common.web.s2.security;


import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.exception.LoginException;
import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.SessionTimeoutException;
import org.onetwo.common.exception.SystemErrorCode;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.web.s2.BaseAction;
import org.onetwo.common.web.s2.security.config.AuthenticConfig;
import org.onetwo.common.web.utils.CookieUtil;
import org.onetwo.common.web.utils.StrutsUtils;
import org.onetwo.common.web.utils.UserKeyManager;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("unchecked")
/********
 * 验证器，
 * 子类覆盖getSSOService方法，提供一个最简单的实现
 * 默认将会查找 AuthenticationInvocation.NAME 名字的实现
 */
abstract public class StrutsAuthentication extends AbstractAuthenticationInvocation {

	
	public static final String LONGIN_VIEW = "login";
	public static final String LOGIN_REDIRECT = "login_redirect";
	
	
	/*@Override
	public UserDetail getCurrentLoginUser(HttpServletRequest request) {
		return StrutsUtils.getCurrentLoginUser();
	}*/
	
	/**************
	 * 异常处理
	 * @param context
	 * @param e
	 */
	@SuppressWarnings("rawtypes")
	public void handException(AuthenticationContext context, Exception e){
		StrutsSecurityTarget st = (StrutsSecurityTarget)context.getTarget();
		BaseAction action = (BaseAction) st.getInvocation().getAction();
        Map session = (Map) st.getInvocation().getInvocationContext().get(ActionContext.SESSION);
        String message = MyUtils.getCauseServiceException(e).getMessage();
        session.put(SystemErrorCode.APP_ERROR_MESSAGE, message);
        action.addActionMessage(message);

        if(LoginException.class.isInstance(e)){
    		this.loginError(context, e);
        }else if(NotLoginException.class.isInstance(e)){
    		this.loginError(context, e);
        }else if(SessionTimeoutException.class.isInstance(e)){
        	if(!context.getConfig().isThrowIfTimeout()){
    			//TODO
    			context.authoritable = null;
    			context.cookieToken = null;
    			this.loginError(context, e);
    		}
        }
        
        AuthenticConfig config = context.getConfig();
		if(config!=null && StringUtils.isNotBlank(config.getRedirect())){
			SecurityUtils.setCurrentSecurityResult(config.getRedirect());
		}
		
		super.handException(context, e);
	}

	protected void loginError(AuthenticationContext context, Exception e){
		StrutsSecurityTarget st = (StrutsSecurityTarget)context.getTarget();
		
		String errorCode = ((ServiceException)e).getCode();
		st.getInvocation().getStack().getContext().put("errorCode", errorCode);
		st.getInvocation().getStack().getContext().put("errorMsg", e.getMessage());
		
		StrutsUtils.removeCurrentLoginUser();
		CookieUtil.removeAllCookies();
		SecurityUtils.getCurrentSecurityResult(UserKeyManager.getCurrentUserKeyManager().getSecurityResult());
	}

	
	@Override
	public void authenticate(AuthenticationContext context) throws Exception {
		try {
			_authenticate(context);
		} catch (Exception e) {
			handException(context, e);
		}
	}

	
	protected Object getContextValue(String expr){
		return ActionContext.getContext().getValueStack().findValue(expr);
	}
	
	@SuppressWarnings("rawtypes")
	protected Object getContextValue(String expr, Class toType){
        if (expr.indexOf("%{")!=-1 && expr.indexOf("}")!=-1 && toType == String.class) {
        	return TextParseUtil.translateVariables('%', expr, getStack());
        } else {
            expr = expr.substring(2, expr.length() - 1);
            return ActionContext.getContext().getValueStack().findValue(expr, toType);
        }
	}
	
	protected ValueStack getStack(){
		return ActionContext.getContext().getValueStack();
	}
	
}
