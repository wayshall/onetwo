package org.onetwo.cloud.zuul;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.boot.limiter.InvokeContext;
import org.onetwo.boot.limiter.InvokeLimiter;
import org.onetwo.cloud.core.BootJfishCloudConfig.FixHeadersConfig;
import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;

import com.google.common.collect.Sets;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;


/**
 * @author wayshall
 * <br/>
 */
@Slf4j
public class LimiterZuulFilter extends ZuulFilter implements InitializingBean {
	

	private Set<InvokeLimiter> preLimiters = Sets.newHashSet();
	
	

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}
	
	protected String getRequestPath(){
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
		String path = RequestUtils.getServletPath(request);
		return path;
	}

	@Override
	public Object run() {
		InvokeContext invokeContext = null;
		for(InvokeLimiter limiter : preLimiters){
			if(limiter.match(invokeContext)){
				
			}
		}
		
		return null;
	}
	
	private void doAntMatcher(FixHeadersConfig fix, String path){
		boolean match = fix.getPathPatterns().stream().anyMatch(pattern->{
			return pathMatcher.match(pattern, path);
		});
		if(match){
			if(log.isDebugEnabled()){
				log.debug("add header[{}] for path {}", fix.getHeader(), path);
			}
			RequestContext.getCurrentContext().addZuulRequestHeader(fix.getHeader(), fix.getValue());
		}
	}
	
	private void doRegexMatcher(FixHeadersConfig fix, String path){
		for(Entry<String, Pattern> entry : fix.getPatterns().entrySet()){
			Matcher matcher = entry.getValue().matcher(path);
			boolean isMatch = matcher.matches();
			if(isMatch){
				int count = matcher.groupCount();
				List<String> groups = new ArrayList<>(count);
				for (int i = 0; i <= count; i++) {
					String val = matcher.group(i);
					groups.add(val);
				}
				String value = expression.parse(fix.getValue(), groups);
				if(log.isDebugEnabled()){
					log.debug("add header[{}] for path {}", fix.getHeader(), path);
				}
				RequestContext.getCurrentContext().addZuulRequestHeader(fix.getHeader(), value);
			}
		}
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
