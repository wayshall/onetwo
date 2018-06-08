package org.onetwo.cloud.zuul.limiter;

import java.util.Set;

import org.onetwo.boot.limiter.InvokeContext;
import org.onetwo.boot.limiter.InvokeLimiter;
import org.springframework.core.Ordered;

import com.google.common.collect.Sets;


/**
 * @author wayshall
 * <br/>
 */
public class PreLimiterZuulFilter extends AbstractLimiterZuulFilter {

	private Set<InvokeLimiter> preLimiters = Sets.newHashSet();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}

	@Override
	public Object run() {
		InvokeContext invokeContext = createInvokeContext();
		
		for(InvokeLimiter limiter : preLimiters){
			if(limiter.match(invokeContext)){
				limiter.consume(invokeContext);
			}
		}
		
		return null;
	}
	
	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	public void setPreLimiters(Set<InvokeLimiter> preLimiters) {
		this.preLimiters = preLimiters;
	}

}
