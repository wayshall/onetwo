package org.onetwo.ext.security.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.ext.security.jwt.JwtAuthStores.StoreContext;
import org.onetwo.ext.security.utils.CookieStorer;
import org.onetwo.ext.security.utils.SecurityConfig.CookieConfig;
import org.onetwo.ext.security.utils.SecurityConfig.JwtConfig;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * @author wayshall
 * <br/>
 */
public class JwtSecurityContextRepository implements SecurityContextRepository, InitializingBean {
	
	final private Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private JwtSecurityTokenService jwtTokenService;
	private boolean updateTokenOnResponse = false;
	private String authHeaderName;
	
	private JwtConfig jwtConfig;
	private CookieConfig cookieConfig;
	private JwtAuthStores authStore;
	
	private CookieStorer cookieStorer;

	@Override
	public void afterPropertiesSet() throws Exception {
		String authName = jwtConfig.getAuthKey();
		if(StringUtils.isBlank(authName)){
			// 默认为 JwtSecurityUtils.DEFAULT_HEADER_KEY
			authName = jwtConfig.getAuthHeader();
		}
		authHeaderName = authName;
		setAuthStore(jwtConfig.getAuthStore());
		CookieStorer storer = CookieStorer.builder()
										.cookieDomain(cookieConfig.getDomain())
										.cookiePath(cookieConfig.getPath())
										.build();
		setCookieStorer(storer);
	}

	
	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		/*HttpServletRequest request = WebHolder.getRequest().get();
		String url = request.getMethod() + "|" + request.getRequestURL();
		System.out.println("url:" +url);*/
		String token = authStore.getToken(requestResponseHolder.getRequest(), authHeaderName);

		if(logger.isDebugEnabled()){
			logger.debug("load context user token : {}", token);
		}
		
		if(StringUtils.isBlank(token)){
			return SecurityContextHolder.createEmptyContext();
		}
		
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = null;
		try {
			authentication = jwtTokenService.createAuthentication(token);
		} catch(CredentialsExpiredException e){
			logger.error("error token : " + e.getMessage(), e);
			if (authStore==JwtAuthStores.COOKIES) {
				cookieStorer.clear(requestResponseHolder.getRequest(), requestResponseHolder.getResponse(), authHeaderName);
			}
//			throw e;
		}
		if(authentication!=null){
			context.setAuthentication(authentication);
		}
		
		return context;
	}
	
	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
		if(!updateTokenOnResponse){
			return ;
		}
		
		if(response.getHeaders(authHeaderName)==null){
			JwtSecurityTokenInfo token = jwtTokenService.generateToken(context.getAuthentication());
//			response.addHeader(authHeaderName, token.getToken());
			StoreContext ctx = StoreContext.builder()
											.request(request)
											.response(response)
											.authKey(authHeaderName)
											.cookieStorer(cookieStorer)
											.build();
			authStore.saveToken(ctx);

			if(logger.isDebugEnabled()){
				logger.debug("saveContext user token : {}", token);
			}
		}
	}
	

	

	@Override
	public boolean containsContext(HttpServletRequest request) {
		return false;
	}
	public void setJwtTokenService(JwtSecurityTokenService jwtTokenService) {
		this.jwtTokenService = jwtTokenService;
	}
//	public void setUpdateTokenOnResponse(boolean updateTokenOnResponse) {
//		this.updateTokenOnResponse = updateTokenOnResponse;
//	}
//	public void setAuthHeaderName(String authHeaderName) {
//		this.authHeaderName = authHeaderName;
//	}

	public void setAuthStore(JwtAuthStores authStore) {
		this.authStore = authStore;
	}

	public void setCookieStorer(CookieStorer cookieStorer) {
		this.cookieStorer = cookieStorer;
	}

	public void setJwtConfig(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	public void setCookieConfig(CookieConfig cookieConfig) {
		this.cookieConfig = cookieConfig;
	}

}
