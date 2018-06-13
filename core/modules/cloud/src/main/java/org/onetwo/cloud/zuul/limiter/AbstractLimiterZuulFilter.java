package org.onetwo.cloud.zuul.limiter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.boot.limiter.InvokeContext;
import org.onetwo.boot.limiter.InvokeContext.DefaultInvokeContext;
import org.onetwo.boot.limiter.InvokeContext.InvokeType;
import org.onetwo.boot.limiter.InvokeLimiter;
import org.onetwo.boot.limiter.LimitInvokeException;
import org.onetwo.boot.module.oauth2.clientdetails.Oauth2ClientDetailManager;
import org.onetwo.cloud.zuul.ZuulUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.web.utils.RequestUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;


/**
 * @author wayshall
 * <br/>
 */
abstract public class AbstractLimiterZuulFilter extends ZuulFilter implements InitializingBean {
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	@Autowired(required=false)
	protected Oauth2ClientDetailManager oauth2ClientDetailManager;
	@Autowired
	private RouteLocator routeLocator;
	private List<InvokeLimiter> limiters;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(routeLocator, "routeLocator cant not be null");
		Assert.notNull(limiters, "limiters can not be null");
		if(!limiters.isEmpty()){
			AnnotationAwareOrderComparator.sort(limiters);
		}
	}
	
	@Override
	public Object run() {
		InvokeContext invokeContext = createInvokeContext();
		
		for(InvokeLimiter limiter : limiters){
			try {
				if(limiter.match(invokeContext)){
					limiter.consume(invokeContext);
				}
			} catch (LimitInvokeException e) {
				ZuulException ze = new ZuulException(e, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
				throw new ZuulRuntimeException(ze);
			}
		}
		
		return null;
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

	abstract protected InvokeContext createInvokeContext();
	
	protected InvokeContext createInvokeContext(InvokeType invokeType) {
        final RequestContext ctx = RequestContext.getCurrentContext();
//        final HttpServletResponse response = ctx.getResponse();
        final HttpServletRequest request = ctx.getRequest();
        
		String requestPath = RequestUtils.getUrlPathHelper().getLookupPathForRequest(request);
		String clientIp = RequestUtils.getRemoteAddr(request);
//		ZuulUtils.getRoute(routeLocator, request);
		DefaultInvokeContext invokeContext = DefaultInvokeContext.builder()
														  .invokeType(invokeType)
														  .requestPath(requestPath)
														  .clientIp(clientIp)
//														  .serviceId(serviceId)
														  .clientUser(null)
														  .invokeTimes(1)
														  .build();
		if(oauth2ClientDetailManager!=null){
			oauth2ClientDetailManager.getCurrentClientDetail().ifPresent(clientDetail->{
				invokeContext.setClientId(clientDetail.getClientId());
			});
		}

//		String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
		ZuulUtils.getRoute(routeLocator, request).ifPresent(route->{
			invokeContext.setServiceId(route.getId());
		});
		
		return invokeContext;
	}

	public void setLimiters(List<InvokeLimiter> limiters) {
		this.limiters = limiters;
	}

}
