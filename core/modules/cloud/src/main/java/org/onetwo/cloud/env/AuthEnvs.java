package org.onetwo.cloud.env;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.core.jwt.JwtConfig;
import org.onetwo.boot.module.oauth2.util.OAuth2Utils;
import org.onetwo.cloud.canary.CanaryUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.WebHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NamedThreadLocal;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author weishao zeng
 * <br/>
 */
@Slf4j
final public class AuthEnvs {

	final public static Set<String> DEFAULT_HEADER_NAMES = Sets.newHashSet(OAuth2Utils.OAUTH2_AUTHORIZATION_HEADER, "auth", CanaryUtils.HEADER_CLIENT_TAG);//
	private static final NamedThreadLocal<AuthEnv> CURRENT_ENVS = new NamedThreadLocal<>("auth env");
	

	public static AuthEnv getCurrent() {
		return CURRENT_ENVS.get();
	}
	public static Optional<AuthEnv> getCurrentOptional() {
		return Optional.ofNullable(getCurrent());
	}
	
	public static void setCurrent(AuthEnv authEnv) {
		if (authEnv==null) {
			removeCurrent();
		} else {
			CURRENT_ENVS.set(authEnv);
		}
	}
	
	public static void removeCurrent() {
		CURRENT_ENVS.remove();
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
	public <T> T runInCurrentWebEnvs(Function<AuthEnv, T> func) {
		return runInCurrentWebEnvs(func, true);
	}
	
	/***
	 * 从给定的context上下文中获取所需要的header（keepHeaders配置）
	 * @author weishao zeng
	 * @param context
	 * @param action
	 * @return
	 */
	public <T> T runInContextEnvs(Map<String, String> context, Function<AuthEnv, T> action) {
		AuthEnv authEnv = createAuthEnv(header -> {
			return context.get(header);
		});
		return runInCurrentEnvs(authEnv, ()->action.apply(authEnv));
	}
	
	/****
	 * 
	 * @author weishao zeng
	 * @param contextExtractor header提取
	 * @param action 业务逻辑回调
	 * @return
	 */
	public <T> T runInContextEnvs(Function<String, String> contextExtractor, Function<AuthEnv, T> action) {
		AuthEnv authEnv = createAuthEnv(contextExtractor);
		return runInCurrentEnvs(authEnv, ()->action.apply(authEnv));
	}
	
	private <T> T runInCurrentWebEnvs(Function<AuthEnv, T> func, boolean throwIfWebEnvNotFound) {
		AuthEnv authEnv = createWebAuthEnv(throwIfWebEnvNotFound);
		return runInCurrentEnvs(authEnv, ()->func.apply(authEnv));
	}
	
	private AuthEnv createAuthEnv(Function<String, String> contextExtractor) {
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
		Optional<HttpServletRequest> requestOpt = WebHolder.getRequest();
//			return new BaseException("web environment not found");
		if (!requestOpt.isPresent()) {
			if (throwIfWebEnvNotFound) {
				throw new BaseException("web environment not found");
			} else {
				return null;
			}
		}
		
		AuthEnv authEnv = createAuthEnv();
		keepHeaders.forEach(header -> {
			if(log.isDebugEnabled()){
				log.debug("set current request header[{}] to feign request...", header);
			}
			String value = requestOpt.get().getHeader(header);
			if(StringUtils.isNotBlank(value)){
				authEnv.getHeaders().add(new AuthEnvHeader(header, value));
			}
		});
		
		return authEnv;
	}
	
	public <T> T runInCurrentEnvs(AuthEnv authEnv, Supplier<T> supplier) {
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
	
	private AuthEnv createAuthEnv() {
		AuthEnv authEnv = new AuthEnv();
		return authEnv;
	}
	
	
	
	public Set<String> getKeepHeaders() {
		return keepHeaders;
	}
	public void setKeepHeaders(Set<String> keepHeaders) {
		this.keepHeaders = keepHeaders;
	}



	@Data
	public static class AuthEnv {
		final private List<AuthEnvHeader> headers = Lists.newArrayList();
		public AuthEnv() {
		}
	}
	
	@Data
	@AllArgsConstructor
	public static class AuthEnvHeader {
		final private String name;
		final private String value;
	}
	

}
