package org.onetwo.cloud.env;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.onetwo.boot.core.jwt.JwtConfig;
import org.onetwo.boot.module.oauth2.util.OAuth2Utils;
import org.onetwo.cloud.canary.CanaryUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.WebHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author weishao zeng
 * <br/>
 */
@Slf4j
final public class AuthEnvs {

	final public static Set<String> DEFAULT_HEADER_NAMES = Sets.newHashSet(OAuth2Utils.OAUTH2_AUTHORIZATION_HEADER, "auth", CanaryUtils.HEADER_CLIENT_TAG);//
	private static final NamedThreadLocal<AuthEnv> CURRENT_ENVS = new NamedThreadLocal<>("auth env");
	private static final String AUTH_ENV_KEY = "__AUTH_WEB_ENV__";


	
	public static <T> T runInCurrent(AuthEnv authEnv, Supplier<T> supplier) {
		if (authEnv==null) {
			throw new IllegalArgumentException("authEnv can not be null");
		}
		setCurrent(authEnv);
		try {
			return supplier.get();
		} finally {
			removeCurrent();
		}
	}

	public static void setCurrent(AuthEnv env) {
		if (env==null) {
			removeCurrent();
			return ;
		}
		RequestAttributes req = RequestContextHolder.getRequestAttributes();
		if (req!=null) {
			req.setAttribute(AUTH_ENV_KEY, env, RequestAttributes.SCOPE_REQUEST);
		} else {
			CURRENT_ENVS.set(env);
		}
	}
	
	public static AuthEnv getCurrent() {
		RequestAttributes req = RequestContextHolder.getRequestAttributes();
		if (req!=null) {
			return (AuthEnv) req.getAttribute(AUTH_ENV_KEY, RequestAttributes.SCOPE_REQUEST);
		} else {
			return CURRENT_ENVS.get();
		}
	}
	
	public static void removeCurrent() {
		RequestAttributes req = RequestContextHolder.getRequestAttributes();
		if (req!=null) {
			req.removeAttribute(AUTH_ENV_KEY, RequestAttributes.SCOPE_REQUEST);
		} else {
			CURRENT_ENVS.remove();
		}
	}
	
	private Set<String> keepHeaders = DEFAULT_HEADER_NAMES;
	@Value("${"+JwtConfig.PREFIX+".authHeader:}")
	private String authHeader;//jfishConfig.getJwt().getAuthHeader()
	
	public AuthEnvs() {
		if (StringUtils.isNotBlank(authHeader)) {
			this.keepHeaders.add(authHeader);
		}
	}
	
	/****
	 * 从当前的web上下文（request）中获取所需要的header（keepHeaders配置）
	 * @author weishao zeng
	 * @param func
	 * @return
	 */
	/*public <T> T runInCurrentWebEnvs(Function<AuthEnv, T> func) {
		return runInCurrentWebEnvs(func, true);
	}*/
	
	/***
	 * 从给定的context上下文中获取所需要的header（keepHeaders配置）
	 * @author weishao zeng
	 * @param context
	 * @param action
	 * @return
	 */
	/*public <T> T runInContextEnvs(Map<String, String> context, Function<AuthEnv, T> action) {
		AuthEnv authEnv = createAuthEnv(header -> {
			return context.get(header);
		});
		return runInCurrentEnvs(authEnv, ()->action.apply(authEnv));
	}*/
	
	/****
	 * 
	 * @author weishao zeng
	 * @param contextExtractor header提取
	 * @param action 业务逻辑回调
	 * @return
	 */
	/*public <T> T runInContextEnvs(Function<String, String> contextExtractor, Function<AuthEnv, T> action) {
		AuthEnv authEnv = createAuthEnv(contextExtractor);
		return runInCurrentEnvs(authEnv, ()->action.apply(authEnv));
	}
	
	private <T> T runInCurrentWebEnvs(Function<AuthEnv, T> func, boolean throwIfWebEnvNotFound) {
		AuthEnv authEnv = createWebAuthEnv(throwIfWebEnvNotFound);
		return runInCurrentEnvs(authEnv, ()->func.apply(authEnv));
	}*/
	
	public <T> Consumer<T> decorate(Consumer<T> action) {
		if (!WebHolder.getRequest().isPresent()) {
			throw new BaseException("web environment not found!");
		}
		AuthEnv webAuthEnv = createWebAuthEnv(true);
		return data -> {
			RequestContextHolder.setRequestAttributes(webAuthEnv.getRequestAttributes());
			try {
				action.accept(data);
			} finally {
				RequestContextHolder.resetRequestAttributes();
			}
		};
	}
	public Runnable decorate(Runnable runnable) {
		if (!WebHolder.getRequest().isPresent()) {
			throw new BaseException("web environment not found!");
		}
		AuthEnv webAuthEnv = createWebAuthEnv(true);
		return () -> {
			RequestContextHolder.setRequestAttributes(webAuthEnv.getRequestAttributes());
			try {
				runnable.run();
			} finally {
				RequestContextHolder.resetRequestAttributes();
			}
		};
	}
	
	public <T> Supplier<T> decorate(Supplier<T> supplier) {
		if (!WebHolder.getRequest().isPresent()) {
			throw new BaseException("web environment not found!");
		}
		AuthEnv webAuthEnv = createWebAuthEnv(true);
		return () -> {
			RequestContextHolder.setRequestAttributes(webAuthEnv.getRequestAttributes());
			try {
				return supplier.get();
			} finally {
				RequestContextHolder.resetRequestAttributes();
			}
		};
	}
	
	public void runInCurrent(Map<String, String> map, Runnable runnable) {
		AuthEnv authEnv = create(map);
		runInCurrent(authEnv, () -> {
			runnable.run();
			return null;
		});
	}
	
	/****
	 * 从map中创建env
	 * @author weishao zeng
	 * @param map
	 * @return
	 */
	private AuthEnv create(Map<String, String> map) {
		return create(name -> map.get(name));
	}
	public AuthEnv create(Function<String, String> contextExtractor) {
		AuthEnv authEnv = createAuthEnv();
		keepHeaders.forEach(header -> {
			if(log.isDebugEnabled()){
				log.debug("set current request header[{}] to feign request...", header);
			}
			String value = contextExtractor.apply(header);
			if(StringUtils.isNotBlank(value)){
				authEnv.getHeaders().add(new AuthEnvHeader(header, value));
			}
		});
		
		return authEnv;
	}
	
	public AuthEnv createWebAuthEnv(boolean throwIfWebEnvNotFound) {
//		Optional<HttpServletRequest> requestOpt = WebHolder.getRequest();
//			return new BaseException("web environment not found");
		RequestAttributes req = RequestContextHolder.getRequestAttributes();
		if (req==null) {
			if (throwIfWebEnvNotFound) {
				throw new BaseException("web environment not found");
			} else {
				return null;
			}
		}
		
		ServletRequestAttributes sattr = (ServletRequestAttributes)req;
		AuthEnv authEnv = createAuthEnv();
		keepHeaders.forEach(header -> {
			if(log.isDebugEnabled()){
				log.debug("set current request header[{}] to feign request...", header);
			}
			String value = sattr.getRequest().getHeader(header);
			if(StringUtils.isNotBlank(value)){
				if (OAuth2Utils.OAUTH2_AUTHORIZATION_HEADER.equals(header)) {
					value = StringUtils.appendStartWith(value, OAuth2Utils.BEARER_TYPE + " ");
				}
				authEnv.getHeaders().add(new AuthEnvHeader(header, value));
			}
		});
		
		setCurrent(authEnv);
		
		return authEnv;
	}
	
	private AuthEnv createAuthEnv() {
		AuthEnv authEnv = new AuthEnv(RequestContextHolder.getRequestAttributes());
		return authEnv;
	}
	
	
	
	public Set<String> getKeepHeaders() {
		return keepHeaders;
	}
	public void setKeepHeaders(Set<String> keepHeaders) {
		this.keepHeaders = keepHeaders;
	}

	@ToString
	public static class AuthEnv {
		final private List<AuthEnvHeader> headers = Lists.newArrayList();
		final private RequestAttributes requestAttributes;
		
		public AuthEnv(RequestAttributes requestAttributes) {
			this.requestAttributes = requestAttributes;
		}
		
		/*public void setToCurrentContext() {
			if (requestAttributes!=null) {
				RequestContextHolder.setRequestAttributes(requestAttributes);
			}
		}*/
		
		public void addAuthEnvHeader(AuthEnvHeader header) {
			this.headers.add(header);
		}
		public List<AuthEnvHeader> getHeaders() {
			return headers;
		}
		public Optional<AuthEnvHeader> findAuthEnvHeader(String name) {
			return this.headers.stream().filter(header -> {
				return header.getName().equals(name);
			}).findFirst();
		}
		public Optional<AuthEnvHeader> findAuthorization() {
			return findAuthEnvHeader(OAuth2Utils.OAUTH2_AUTHORIZATION_HEADER);
		}
		public RequestAttributes getRequestAttributes() {
			return requestAttributes;
		}
		
	}
	
	@Data
	@AllArgsConstructor
	public static class AuthEnvHeader {
		final private String name;
		final private String value;
	}
	

}
