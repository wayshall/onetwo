package org.onetwo.cloud.feign;

import org.onetwo.boot.core.web.utils.RemoteClientUtils;
import org.onetwo.boot.core.web.utils.RemoteClientUtils.ClientTypes;
import org.onetwo.boot.module.oauth2.util.OAuth2Utils;
import org.onetwo.cloud.env.AuthEnvs;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.WebHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class KeepHeaderRequestInterceptor implements RequestInterceptor {
	
//	final public static Set<String> DEFAULT_HEADER_NAMES = Sets.newHashSet(OAuth2Utils.OAUTH2_AUTHORIZATION_HEADER, "auth", CanaryUtils.HEADER_CLIENT_TAG);
//	private Set<String> keepHeaders; // = DEFAULT_HEADER_NAMES;

	private AuthEnvs authEnvs;
	
	@Override
	public void apply(RequestTemplate template) {
		OAuth2Utils.getCurrentToken().ifPresent(token -> {
			if(log.isDebugEnabled()){
				log.debug("set current context header[{}] to feign ...", token);
			}
			if (StringUtils.isNotBlank(token)) {
				template.header(OAuth2Utils.OAUTH2_AUTHORIZATION_HEADER, StringUtils.appendStartWith(token, OAuth2Utils.BEARER_TYPE + " "));
			}
		});
		// always add feign client type header
		template.header(RemoteClientUtils.HEADER_CLIENT_TYPE, ClientTypes.FEIGN.name());
		WebHolder.getRequest().ifPresent(request->{
//			keepHeaders.forEach(header->{
			authEnvs.getKeepHeaders().forEach(header->{
				String value = request.getHeader(header);
				if(StringUtils.isNotBlank(value)){
					template.header(header, value);
					if(log.isDebugEnabled()){
						log.debug("set current request header[{}] to feign request...", header);
					}
				}
			});
		});
		
		// 如果调用了runInCurrentWebEnvs，则会执行，并覆盖
		AuthEnvs.getCurrentOptional().ifPresent(authEnv -> {
			authEnv.getHeaders().forEach(header -> {
				String value = header.getValue();
				if (StringUtils.isNotBlank(value)){
					if (OAuth2Utils.OAUTH2_AUTHORIZATION_HEADER.equals(header.getName())) {
						value = StringUtils.appendStartWith(value, OAuth2Utils.BEARER_TYPE + " ");
					}
					template.header(header.getName(), value);
					if(log.isDebugEnabled()){
						log.debug("set current env header[{}] to feign request...", header);
					}
				}
			});
		});
	}

//	public void setKeepHeaders(Set<String> keepHeaders) {
//		this.keepHeaders = keepHeaders;
//	}


	public void setAuthEnvs(AuthEnvs authEnvs) {
		this.authEnvs = authEnvs;
	}
	
}
