package org.onetwo.boot.core.web.filter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.utils.PostfixMatcher;
import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.web.filter.RequestContextFilter;

public class BootRequestContextFilter extends RequestContextFilter {
	private PostfixMatcher ignorePostfixMatcher = new PostfixMatcher();
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String uri = RequestUtils.getServletPath(request);
		return !ignorePostfixMatcher.isMatch(uri);
	}
}
