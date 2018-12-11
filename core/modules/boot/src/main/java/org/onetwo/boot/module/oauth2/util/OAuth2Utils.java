package org.onetwo.boot.module.oauth2.util;

import java.util.Optional;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.module.oauth2.clientdetails.ClientDetails;
import org.onetwo.boot.module.oauth2.clientdetails.ClientDetailsObtainService;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.Springs;
import org.onetwo.common.web.utils.WebHolder;
import org.springframework.core.NamedThreadLocal;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.util.Assert;

/**
 * @author wayshall
 * <br/>
 */
public abstract class OAuth2Utils {
	public static String BEARER_TYPE = "Bearer";
	
	public static final String OAUTH2_AUTHORIZATION_HEADER = "Authorization";
	public static final String OAUTH2_CLIENT_DETAILS_SERVICE = "oauth2ClientDetailsService";
	
	private static final String CLIENT_DETAILS_ATTR_KEY = "__CLIENT_DETAILS__";

	private static final NamedThreadLocal<ClientDetails> CURRENT_CLIENTS = new NamedThreadLocal<>("oauth2 context");
	private static final NamedThreadLocal<String> CURRENT_TOKENS = new NamedThreadLocal<>("oauth2 token");
	

	private static ClientDetailsObtainService getClientDetailsObtainService() {
		return Springs.getInstance().getBean(ClientDetailsObtainService.class);
	}
	
	public static Optional<String> getCurrentToken() {
		Optional<HttpServletRequest> req = WebHolder.getRequest();
		if(req.isPresent()){
			return getClientDetailsObtainService().getTokenValue(req.get());
		}
		return Optional.ofNullable(CURRENT_TOKENS.get());
	}

	@SuppressWarnings("unchecked")
	public static <T extends ClientDetails> Optional<T> getCurrentClientDetails() {
		// 线程变量优先
		T data = (T)CURRENT_CLIENTS.get();
		if (data!=null) {
			return Optional.of((T)data);
		}
		Optional<HttpServletRequest> req = WebHolder.getRequest();
		if(!req.isPresent()){
			return Optional.empty();
		}
		return getClientDetails(req.get());
	}
	
	public static void runInContext(ClientDetails clientDetail, Runnable runnalbe) {
		try {
			CURRENT_CLIENTS.set(clientDetail);
			runnalbe.run();
		} finally {
			CURRENT_CLIENTS.remove();
		}
	}
	
	public static <T> T runInContext(ClientDetails clientDetail, Supplier<T> supplier) {
		try {
			CURRENT_CLIENTS.set(clientDetail);
			return supplier.get();
		} finally {
			CURRENT_CLIENTS.remove();
		}
	}
	
	public static void runInToken(String token, Runnable runnalbe) {
		try {
			CURRENT_TOKENS.set(token);
			ClientDetails clientDetail = getClientDetailsObtainService().resolveClientDetails(token);
			runInContext(clientDetail, runnalbe);
		} finally {
			CURRENT_CLIENTS.remove();
		}
	}
	
	public static <T> T runInToken(String token, Supplier<T> supplier) {
		try {
			CURRENT_TOKENS.set(token);
			ClientDetails clientDetail = getClientDetailsObtainService().resolveClientDetails(token);
			return runInContext(clientDetail, supplier);
		} finally {
			CURRENT_TOKENS.remove();
		}
	}
	/*public static Optional<String> getCurrentToken() {
		return Optional.ofNullable(CURRENT_TOKENS.get());
	}*/
	
	public static Runnable runInThread(final Runnable runnable){
		Optional<ClientDetails> clientDetail = getCurrentClientDetails();
		if (!clientDetail.isPresent()) {
			throw new BaseException("oauth client detail not found in current context");
		}
		return ()->{
			runInContext(clientDetail.get(), runnable);
		};
	}
	
	public static <T extends ClientDetails> Optional<T> getClientDetails(HttpServletRequest request) {
		return getOrSetClientDetails(request, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends ClientDetails> Optional<T> getOrSetClientDetails(HttpServletRequest request, Supplier<T> supplier) {
		Object data = request.getAttribute(CLIENT_DETAILS_ATTR_KEY);
		if(data!=null){
			return Optional.of((T)data);
		}
		
		if(supplier==null){
			return Optional.empty();
		}
		
		T details = supplier.get();
		setCurrentClientDetails(request, details);
		return Optional.ofNullable(details);
	}
	
	public static void setCurrentClientDetails(HttpServletRequest request, Object clientDetail) {
		Assert.notNull(clientDetail, "clientDetail can not be null");
		request.setAttribute(CLIENT_DETAILS_ATTR_KEY, clientDetail);
	}
	
	
	public static Optional<String> getAccessTokenValue(TokenExtractor tokenExtractor,  HttpServletRequest request){
		String accessToken = (String)request.getAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE);
		if(accessToken==null){
			Authentication authentication = tokenExtractor.extract(request);
			accessToken = authentication==null?null:(String)authentication.getPrincipal();
		}
		return Optional.ofNullable(accessToken);
	}
	
	private OAuth2Utils(){
	}
	
}
