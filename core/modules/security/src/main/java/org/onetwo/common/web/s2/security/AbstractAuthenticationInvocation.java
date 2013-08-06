package org.onetwo.common.web.s2.security;

import java.util.List;

import org.apache.log4j.Logger;
import org.onetwo.common.exception.AuthenticationException;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ErrorRoleException;
import org.onetwo.common.exception.LoginException;
import org.onetwo.common.exception.NoAuthorizationException;
import org.onetwo.common.exception.NotLoginException;
import org.onetwo.common.exception.SessionTimeoutException;
import org.onetwo.common.exception.SystemErrorCode.LoginErrorCode;
import org.onetwo.common.sso.SSOService;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.PermissionDetail;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.UserDetail;
import org.onetwo.common.web.s2.security.config.AuthenticConfig;


abstract public class AbstractAuthenticationInvocation implements AuthenticationInvocation {

	private static final String EL_PREFIX = "EL:";
	
	protected final Logger logger = Logger.getLogger(this.getClass());


	abstract public SSOService getSSOService();
	

	@Override
	public void authenticate(AuthenticationContext context) throws Exception {
		try {
			_authenticate(context);
		} catch (Exception e) {
			handException(context, e);
		}
	}
	
	protected boolean checkSessionTimeout(SecurityTarget target){
		boolean timeout = getSSOService().checkTimeout(target, true);
		if(timeout){
			throw new SessionTimeoutException();
			/*if(config.isThrowIfTimeout())
				throw new NotLoginException("登录超时", SystemErrorCode.ES_LOGIN_TIMEOUT);
			else{
				authoritable = null;
				cookietoken = null;
				StrutsUtils.removeCurrentLoginUser();
				CookieUtil.removeCookieToken();
			}*/
		}
		return timeout;
	}
	
	protected void checkRoles(AuthenticConfig config, SecurityTarget target){
		if(!LangUtils.isEmpty(config.getRoles())){
			if(LangUtils.isEmpty(target.getRoles())){
				throw new ErrorRoleException("角色不能为空！");
			}
			boolean acceptTheRole = false;
			for(String role : config.getRoles()){
				if(target.containsRole(role)){
					acceptTheRole = true;
					break;
				}
			}
			if(!acceptTheRole){
				throw new ErrorRoleException(LangUtils.toString(target.getRoles()));
			}
		}
	}
	
	protected void _authenticate(AuthenticationContext context) throws Exception {
		AuthenticConfig config = context.getConfig();
		SecurityTarget target = context.getTarget();
		UserDetail authoritable = target.getAuthoritable();
//		String cookietoken = target.getCookieToken();
		
		//验证超时
		if(config.isCheckTimeout()){
			this.checkSessionTimeout(target);
		}
		
		//要先检查单点登陆，然后才去验证
		if(config.isCheckLogin()){
			authoritable = this.getSSOService().checkLogin(target);
		}
		
		//首先判断被请求的方法的安全配置是否需要验证，如果不需要，直接调用action被请求的方法
		if (config.isIgnore())
			return ;

		//判断被请求的action是否配置了只通过自定义的Authenticator验证器来验证，如果是，就调用自定义的Authenticator
		if(config.isOnlyAuthenticator()){
			if(config.getAuthenticators()==null)
				throw new BaseException("Authentic[OnlyAuthenticator=true] but  Authenticator is null.");
			invokeBeforeAuthenticator(context);
			return ;
		}

		//通过在session取得的用户信息authoritable去验证用户是否已登陆
		if (!authenticatLogin(authoritable)){
			logger.error("NotLoginException: "+config.toString()+"");
			throw new NotLoginException();
		}

		//验证角色
		this.checkRoles(config, target);
		
		if (!authenticatPermission(config, authoritable)){
			logger.error("NoAuthorizationException: [user:"+authoritable.toString()+", config:"+config.toString()+"]");
			throw new NoAuthorizationException();
		}
		
		afterAuthenticatPermission(config, target, authoritable);
		invokeBeforeAuthenticator(context);
		
	}

	
	protected void afterAuthenticatPermission(AuthenticConfig config, SecurityTarget target, UserDetail authoritable){
	}

	public boolean authenticatLogin(UserDetail authoritable) {
		if (authoritable == null)
			return false;
		return true;
	}
	
	public void invokeBeforeAuthenticator(AuthenticationContext context) throws Exception{
		List<Authenticator> authenticators = context.getConfig().getAuthenticators();
		if(LangUtils.isEmpty(authenticators))
			return ;
		
		for(Authenticator auth : authenticators){
			boolean isAllow = auth.beforeTarget(context);//invokeBefore((BeforeAuthenticator)auth, config, authoritable, target);
			if(!isAllow)
				throw new AuthenticationException();
		}
		
	}
	
//	public void invokeAfterAuthenticator(AuthenticationContext context) throws Exception{
//		List<Object> authenticators = context.getConfig().getAuthenticators();
//		if(LangUtils.isEmpty(authenticators))
//			return ;
//		
//		for(Object auth : authenticators){
//			if(!(auth instanceof AfterAuthenticator))
//				continue;
//			boolean isAllow = ((AfterAuthenticator)auth).afterTarget(context);//invokeAfter((AfterAuthenticator)auth, config, authoritable, target);
//			if(!isAllow)
//				throw new AuthenticationException();
//		}
//	}
	
	/*public boolean invokeBefore(BeforeAuthenticator auth, AuthenticConfig config, UserDetail authoritable, SecurityTarget target){
		try {
			return auth.beforeTarget(config, authoritable, target);
		} catch (AuthenticationException e) {
			target.addMessage(e.getMessage());
		}
		return false;
	}
	
	public boolean invokeAfter(AfterAuthenticator auth, AuthenticConfig config, UserDetail authoritable, SecurityTarget target){
		try {
			return auth.afterTarget(config, authoritable, target);
		} catch (AuthenticationException e) {
			target.addMessage(e.getMessage());
		}
		return false;
	}*/

	public boolean authenticatPermission(AuthenticConfig config, UserDetail authoritable) {
		if (!config.isAnnotationAuthentic())
			return true;
		
		/*for (String expr : config.getPermissions()) {
			boolean isAutheic = false;
			if(expr.startsWith("%{") && expr.endsWith("}")){
				isAutheic = authenticateExpression(expr.substring(2, expr.length()-1), authoritable);
			}else{
				isAutheic = expr.equals(authoritable.getUserId());
			}
			if(isAutheic)
				return true;
		}*/
		if(LangUtils.isEmpty(config.getPermissions()))
			return true;
		if(!PermissionDetail.class.isInstance(authoritable)){
			return false;
		}
		PermissionDetail pUser = (PermissionDetail) authoritable;
		for(String p : config.getPermissions()){
			if(p.startsWith(EL_PREFIX)){
				p = p.substring(EL_PREFIX.length());
				if(authenticateExpression(p, authoritable))//pass if any one is true
					return true;
			}else{
				if(pUser.getPermissions().contains(p))//pass if user permission contains any one of action permisson
					return true;
			}
		}
		return false;
	}
	
	
	protected boolean authenticateExpression(String expr, UserDetail authoritable){
		Object value = null;
		try {
			value = getContextValue(expr);
			if(value==null)
				return false;
			if(value instanceof Boolean)
				return (Boolean) value;
			return String.valueOf(authoritable.getUserId()).equals(value.toString());
		} catch (Exception e) {
			logger.error("authenticateExpression error: " + e.getMessage(), e);
		}
		return false;
	}
	
	abstract protected Object getContextValue(String expr);
	

	/**************
	 * 异常处理
	 * @param context
	 * @param e
	 */
	public void handException(AuthenticationContext context, Exception e){
		if(e instanceof LoginException){
			throw (LoginException)e;
		}else if(e instanceof NotLoginException){
			if(e instanceof SessionTimeoutException){
				if(!context.getConfig().isThrowIfTimeout()){
					//TODO
					context.authoritable = null;
					context.cookieToken = null;
				}
			}
			throw (NotLoginException)e;
		}else if(e instanceof AuthenticationException){
			throw (AuthenticationException)e;
		}else if(e instanceof BaseException){
			String code = ((BaseException) e).getCode();
			if(StringUtils.isNotBlank(code) && code.startsWith(LoginErrorCode.BASE_CODE)){
				throw new LoginException(e.getMessage(), e);
			}else{
				throw (BaseException) e;
			}
		}else{
			if(RuntimeException.class.isInstance(e)){
				throw (RuntimeException)e;
			}else{
				throw LangUtils.asBaseException(e);
			}
		}
	}
	
	
}
