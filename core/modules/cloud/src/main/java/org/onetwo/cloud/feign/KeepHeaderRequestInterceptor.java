package org.onetwo.cloud.feign;

import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.WebHolder;

import com.google.common.collect.Sets;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class KeepHeaderRequestInterceptor implements RequestInterceptor {
	
	final private Set<String> DEFAULT_HEADER_NAMES = Sets.newHashSet("Authorization", "auth");
	private Set<String> keepHeaders = DEFAULT_HEADER_NAMES;

	@Override
	public void apply(RequestTemplate template) {
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
	}

	public void setKeepHeaders(Set<String> keepHeaders) {
		this.keepHeaders = keepHeaders;
	}
	
}
