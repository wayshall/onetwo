package org.onetwo.cloud.zuul.limiter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;


/**
 * @author wayshall
 * <br/>
 */
public class PostLimiterZuulFilter extends AbstractLimiterZuulFilter {

	@Override
	public String filterType() {
		return FilterConstants.POST_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 10;
	}

}
