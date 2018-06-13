package org.onetwo.cloud.zuul.limiter;

import org.onetwo.boot.limiter.InvokeContext;
import org.onetwo.boot.limiter.InvokeContext.InvokeType;
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
	protected InvokeContext createInvokeContext() {
		return createInvokeContext(InvokeType.BEFORE);
	}



	@Override
	public int filterOrder() {
		return FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;
	}

}
