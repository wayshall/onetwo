package org.onetwo.ext.security.redis;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.ext.security.utils.LoginUserDetails;
import org.onetwo.ext.security.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.ClassUtils;
import org.springframework.web.util.WebUtils;


public class RedisSecurityContextRepository implements SecurityContextRepository {

	public static final String BEAN_NAME = "redisSecurityContextRepository";
	public static final String SID_REQUEST_KEY = "__sid__";
//	public static final String SID_REQUEST_GENERATED_KEY = "__sid__generated__";
	
	static String getSecuritySessionKey(String sid){
		return SPRING_SECURITY_KEY + sid;
	}
	
	public static final String SPRING_SECURITY_KEY = "spring:security:session:";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RedisTemplate<String, SecurityContext> redisTemplate;
	
	private final Object contextObject = SecurityContextHolder.createEmptyContext();
	private boolean allowSessionCreation = true;
	private boolean disableUrlRewriting = false;
	private boolean isServlet3 = ClassUtils.hasMethod(ServletRequest.class, "startAsync");
	private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

	private String cookieName = "sid";
	private String cookiePath = "/";
	private String cookieDomain;
	
	
	public RedisSecurityContextRepository() {
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	protected String getSessionId(HttpServletRequest request){
		return getSessionId(request, false);
	}
	
	protected String getSessionId(HttpServletRequest request, boolean gneretedIfNotFound){
		String sid = RequestUtils.getCookieValue(request, cookieName);
		if(StringUtils.isBlank(sid) && gneretedIfNotFound){
			sid = UUID.randomUUID().toString();
			request.setAttribute(SID_REQUEST_KEY, sid);
		}
		return sid;
	}

	private void saveSessionCookies(HttpServletRequest request, HttpServletResponse response, String cookieSessionId) {
        Cookie sessionCookie = new Cookie(cookieName, cookieSessionId);
        configCookie(request, sessionCookie);
        response.addCookie(sessionCookie);
    }
	
	protected void clearSessionCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie sessionCookie = new Cookie(cookieName, null);
        configCookie(request, sessionCookie);
        sessionCookie.setMaxAge(0);
		response.addCookie(sessionCookie);
    }
	
	private BoundValueOperations<String, SecurityContext> getSessionBoundOps(String sid){
//		String sid = getSessionId(httpSession);
		String skey = getSecuritySessionKey(sid);
//		return this.redisTemplate.boundHashOps(skey);
		return this.redisTemplate.boundValueOps(skey);
	}
	
	private void saveSecurityContext(HttpServletRequest request, HttpServletResponse response, SecurityContext context){
		String sid = getSessionId(request);
		if(StringUtils.isBlank(sid)){
			SaveToSessionResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, SaveToSessionResponseWrapper.class);
			sid = responseWrapper.getSid();
			saveSessionCookies(request, response, sid);
		}
		
		LoginUserDetails loginUser = SecurityUtils.getCurrentLoginUser(context);
		if(loginUser!=null){
			loginUser.setToken(sid);
		}
		
		BoundValueOperations<String, SecurityContext> bondOps = getSessionBoundOps(sid);
		//当前spring-data-redis版本不支持setex，分成两个操作
		bondOps.set(context);
		setSecurityContextExpireTime(request);
	}
	
	private void setSecurityContextExpireTime(HttpServletRequest request){
		String sid = getSessionId(request);
		if(StringUtils.isBlank(sid))
			return ;
		BoundValueOperations<String, SecurityContext> bondOps = getSessionBoundOps(sid);
		int invalidTime = request.getSession().getMaxInactiveInterval();
		bondOps.expire(invalidTime, TimeUnit.SECONDS);
	}
	
	public void removeSecurityContext(HttpServletRequest request, HttpServletResponse response){
		String sid = getSessionId(request);
		if(StringUtils.isBlank(sid)){
			return ;
		}
		clearSessionCookie(request, response);
		
		String skey = getSecuritySessionKey(sid);
		redisTemplate.delete(skey);
	}
	
	private boolean isRedisContainsContext(HttpServletRequest request){
		String sid = getSessionId(request);
		String skey = getSecuritySessionKey(sid);
		return redisTemplate.hasKey(skey);
	}
	
	private SecurityContext readSecurityContextFromSession(HttpServletRequest request) {
		String sid = getSessionId(request);
		if(StringUtils.isBlank(sid)){
			return null;
		}
		SecurityContext context = getSessionBoundOps(sid).get();
		return context;
	}

	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		HttpServletRequest request = requestResponseHolder.getRequest();
		HttpServletResponse response = requestResponseHolder.getResponse();
		HttpSession httpSession = request.getSession(false);
		
		String sid = this.getSessionId(request, true);
		SecurityContext context = readSecurityContextFromSession(request);
		if (context == null) {
			context = SecurityContextHolder.createEmptyContext();
		}

		SaveToSessionResponseWrapper wrappedResponse = new SaveToSessionResponseWrapper(
				response, request, httpSession != null, context, sid);
		requestResponseHolder.setResponse(wrappedResponse);

		if (isServlet3) {
			requestResponseHolder.setRequest(new Servlet3SaveToSessionRequestWrapper(request, wrappedResponse));
		}
		
		return context;
	}

	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
		SaveToSessionResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, SaveToSessionResponseWrapper.class);
		if (responseWrapper == null) {
			throw new IllegalStateException(
					"Cannot invoke saveContext on response "
							+ response
							+ ". You must use the HttpRequestResponseHolder.response after invoking loadContext");
		}
		// saveContext() might already be called by the response wrapper
		// if something in the chain called sendError() or sendRedirect(). This ensures we
		// only call it
		// once per request.
		if (!responseWrapper.isContextSaved()) {
			responseWrapper.saveContext(context);
		}
	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
		return isRedisContainsContext(request);
	}

	
	private void configCookie(HttpServletRequest request, Cookie sessionCookie){
		if(isServlet3){
        	PropertyAccessorFactory.forBeanPropertyAccess(sessionCookie).setPropertyValue("httpOnly", true);
        }
        sessionCookie.setSecure(request.isSecure());
        sessionCookie.setPath(cookiePath(request));
        String domain = cookieDomain(request);
        if(StringUtils.isNotBlank(domain)){
        	sessionCookie.setDomain(domain);
        }
	}
	
	private String cookiePath(HttpServletRequest request) {
	   	if(StringUtils.isNotBlank(cookiePath)){
	   		return cookiePath;
	   	}
	   	return request.getContextPath() + "/";
   }

   private String cookieDomain(HttpServletRequest request) {
	   	if(StringUtils.isNotBlank(cookieDomain)){
	   		return cookieDomain;
	   	}
	   	return null;
   }

	// 去掉了final. by gkh
	public class SaveToSessionResponseWrapper extends
			SaveContextOnUpdateOrErrorResponseWrapper {

		private final HttpServletRequest request;
		private final HttpServletResponse response;
		private final boolean httpSessionExistedAtStartOfRequest;
		private final SecurityContext contextBeforeExecution;
		private final Authentication authBeforeExecution;
		private final String sid;
		
		SaveToSessionResponseWrapper(HttpServletResponse response,
				HttpServletRequest request, boolean httpSessionExistedAtStartOfRequest,
				SecurityContext context, String sid) {
			super(response, disableUrlRewriting);
			this.request = request;
			this.response = response;
			this.httpSessionExistedAtStartOfRequest = httpSessionExistedAtStartOfRequest;
			this.contextBeforeExecution = context;
			this.authBeforeExecution = context.getAuthentication();
			this.sid = sid;
		}
		
		public String getSid() {
			return sid;
		}

		@Override
		protected void saveContext(SecurityContext context) {
			final Authentication authentication = context.getAuthentication();
			HttpSession httpSession = request.getSession(false);
		
			// See SEC-776
			if (authentication == null || trustResolver.isAnonymous(authentication)) {
				if (logger.isDebugEnabled()) {
//					logger.debug("SecurityContext is empty or contents are anonymous - context will not be stored in HttpSession.");
				}
		
				if (httpSession != null && authBeforeExecution != null) {
					// SEC-1587 A non-anonymous context may still be in the session
					// SEC-1735 remove if the contextBeforeExecution was not anonymous
					removeSecurityContext(request, response);
				}
				return;
			}
		
			if (httpSession == null) {
				httpSession = createNewSessionIfAllowed(context);
			}
		
			// If HttpSession exists, store current SecurityContext but only if it has
			// actually changed in this thread (see SEC-37, SEC-1307, SEC-1528)
			if (httpSession != null) {
				// We may have a new session, so check also whether the context attribute
				// is set SEC-1561
				if (contextChanged(context)
						|| !isRedisContainsContext(request)) {
//					httpSession.setAttribute(springSecurityContextKey, context);
					saveSecurityContext(request, this, context);
		
					if (logger.isDebugEnabled()) {
						logger.debug("SecurityContext '" + context
								+ "' stored to HttpSession: '" + httpSession);
					}
				}else{
					setSecurityContextExpireTime(request);
				}
			}
		}
		
		private boolean contextChanged(SecurityContext context) {
			return context != contextBeforeExecution
					|| context.getAuthentication() != authBeforeExecution;
		}
		
		private HttpSession createNewSessionIfAllowed(SecurityContext context) {
			if (httpSessionExistedAtStartOfRequest) {
				return null;
			}
		
			if (!allowSessionCreation) {
				return null;
			}
			// Generate a HttpSession only if we need to
		
			if (contextObject.equals(context)) {
				if (logger.isDebugEnabled()) {
					logger.debug("HttpSession is null, but SecurityContext has not changed from default empty context: ' "
							+ context
							+ "'; not creating HttpSession or storing SecurityContext");
				}
		
				return null;
			}
		
		
			try {
				return request.getSession(true);
			}
			catch (IllegalStateException e) {
				// Response must already be committed, therefore can't create a new
				// session
				logger.warn("Failed to create a session, as response has been committed. Unable to store"
						+ " SecurityContext.");
			}
		
			return null;
		}
	}
	
	private static class Servlet3SaveToSessionRequestWrapper extends
			HttpServletRequestWrapper {
		private final SaveContextOnUpdateOrErrorResponseWrapper response;
		
		public Servlet3SaveToSessionRequestWrapper(HttpServletRequest request,
				SaveContextOnUpdateOrErrorResponseWrapper response) {
			super(request);
			this.response = response;
		}
		
		@Override
		public AsyncContext startAsync() {
			response.disableSaveOnResponseCommitted();
			return super.startAsync();
		}
		
		@Override
		public AsyncContext startAsync(ServletRequest servletRequest,
				ServletResponse servletResponse) throws IllegalStateException {
			response.disableSaveOnResponseCommitted();
			return super.startAsync(servletRequest, servletResponse);
		}
	}
}
