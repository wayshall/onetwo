package org.onetwo.cloud.zuul;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.cloud.core.BootJfishCloudConfig.FixHeadersConfig;
import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.core.Ordered;
import org.springframework.util.AntPathMatcher;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;


/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class FixHeaderZuulFilter extends ZuulFilter {
	
	private List<FixHeadersConfig> fixHeaders;
	private AntPathMatcher pathMatcher = new AntPathMatcher();

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		if(fixHeaders==null){
			return null;
		}
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
		String path = RequestUtils.getServletPath(request);
		
		fixHeaders.stream().forEach(fix->{
			boolean match = fix.getPathPatterns().stream().anyMatch(pattern->{
				return pathMatcher.match(pattern, path);
			});
			if(match){
				log.info("add header[{}] for path {}", fix.getHeader(), path);
				RequestContext.getCurrentContext().addZuulRequestHeader(fix.getHeader(), fix.getValue());
			}
		});
		
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	public void setFixHeaders(List<FixHeadersConfig> fixHeaders) {
		this.fixHeaders = fixHeaders;
	}
	

}
