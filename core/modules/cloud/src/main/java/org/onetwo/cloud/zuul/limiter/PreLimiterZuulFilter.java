package org.onetwo.cloud.zuul.limiter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;


/**
 * @author wayshall
 * <br/>
 */
public class PreLimiterZuulFilter extends AbstractLimiterZuulFilter {

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;
	}

}
