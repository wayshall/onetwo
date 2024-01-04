package org.onetwo.boot.core.web.filter;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/*****
 * Host头攻击漏洞保护
 * @author way
 *
 */
public class HostPreventFilter implements Filter {
	public static final String FILTER_NAME = "hostPreventFilter";
	
	@Autowired
	private BootJFishConfig config;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String host = httpRequest.getHeader("Host");
		if (!isAllowHost(host)) {
//			httpResponse.setStatus(config.getHostfilter().getDenyStatus(), config.getHostfilter().getDenyMessage());
//			httpResponse.sendError(config.getHostfilter().getDenyStatus(), config.getHostfilter().getDenyMessage());
			httpResponse.setStatus(config.getHostfilter().getDenyStatus());
			httpResponse.flushBuffer();
			return ;
		}
        chain.doFilter(request, httpResponse);
	}
	
	private boolean isAllowHost(String host) {
		List<String> allowIps = this.config.getHostfilter().getAllowIps();
		if (LangUtils.isEmpty(allowIps)) {
			// 若不配置，则允许所有
			return true;
		}
		String[] hostAndPort = StringUtils.split(host, ":");
		boolean allow = allowIps.contains(hostAndPort[0]);
		return allow;
	}

	@Override
	public void destroy() {
	}
	
}
