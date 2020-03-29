package org.onetwo.ext.security.metadata;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.google.common.collect.Sets;

/**
 * 默认的DefaultFilterInvocationSecurityMetadataSource匹配到了立刻返回，
 * 这里提供一个匹配所有到实现
 * @author weishao zeng
 * <br/>
 */
public class MatchAllFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	protected final Logger logger = JFishLoggerFactory.getLogger(getClass());

	private final Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

	public MatchAllFilterInvocationSecurityMetadataSource(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap) {
		this.requestMap = requestMap;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap
				.entrySet()) {
			allAttributes.addAll(entry.getValue());
		}

		return allAttributes;
	}

	public Collection<ConfigAttribute> getAttributes(Object object) {
		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
		Collection<ConfigAttribute> auths = Sets.newHashSet();
		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
			if (entry.getKey().equals(AnyRequestMatcher.INSTANCE)) {
				if (logger.isDebugEnabled()) {
					logger.debug("ignore any matcher for: {}", entry.getValue());
				}
				continue;
			}
//			if (entry.getKey().toString().contains("/productCategory/treeList*")) {
//				System.out.println("test");
//			}
			if (entry.getKey().matches(request)) {
				auths.addAll(entry.getValue());
			}
		}
		return auths;
	}

	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
}
