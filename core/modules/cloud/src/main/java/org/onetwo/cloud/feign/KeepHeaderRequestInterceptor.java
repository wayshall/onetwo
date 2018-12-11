package org.onetwo.cloud.feign;

import java.util.Set;

import org.onetwo.boot.core.web.utils.RemoteClientUtils;
import org.onetwo.boot.core.web.utils.RemoteClientUtils.ClientTypes;
import org.onetwo.boot.module.oauth2.util.OAuth2Utils;
import org.onetwo.cloud.canary.CanaryUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.WebHolder;

import com.google.common.collect.Sets;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class KeepHeaderRequestInterceptor implements RequestInterceptor {
	
	final public static Set<String> DEFAULT_HEADER_NAMES = Sets.newHashSet(OAuth2Utils.OAUTH2_AUTHORIZATION_HEADER, "auth", CanaryUtils.HEADER_CLIENT_TAG);
	private Set<String> keepHeaders = DEFAULT_HEADER_NAMES;

	@Override
	public void apply(RequestTemplate template) {
		// always add feign client type header
		template.header(RemoteClientUtils.HEADER_CLIENT_TYPE, ClientTypes.FEIGN.name());
		WebHolder.getRequest().ifPresent(request->{
			keepHeaders.forEach(header->{
				if(log.isDebugEnabled()){
					log.debug("set current request header[{}] to feign request...", header);
				}
				String value = request.getHeader(header);
				if(StringUtils.isNotBlank(value)){
					template.header(header, value);
				}
			});
		});
		OAuth2Utils.getCurrentToken().ifPresent(token -> {
			if(log.isInfoEnabled()){
				log.info("set current context header[{}] to feign ...", token);
			}
			if (StringUtils.isNotBlank(token)) {
				template.header(OAuth2Utils.OAUTH2_AUTHORIZATION_HEADER, token);
			}
		});
	}

	public void setKeepHeaders(Set<String> keepHeaders) {
		this.keepHeaders = keepHeaders;
	}

	public Set<String> getKeepHeaders() {
		return keepHeaders;
	}
	
}
