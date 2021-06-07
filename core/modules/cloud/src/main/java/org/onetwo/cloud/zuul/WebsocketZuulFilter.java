package org.onetwo.cloud.zuul;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;


/**
 * @author wayshall
 * <br/>
 */
public class WebsocketZuulFilter extends ZuulFilter implements InitializingBean {
	private static final String HEADER_WEBSOCKET_NAME = "Upgrade";
	private static final String HEADER_WEBSOCKET_VALUE = "websocket";
	
//	private final Logger logger = JFishLoggerFactory.getLogger(getClass());
//	private final List<String> websocketheaderNames = Lists.newArrayList("Upgrade", "upgrade");

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}
	
	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String upgradeHeader = request.getHeader(HEADER_WEBSOCKET_NAME);
//        String upgradeHeader2 = request.getHeader("upgrade");
        if (HEADER_WEBSOCKET_VALUE.equalsIgnoreCase(upgradeHeader)) {
            context.addZuulRequestHeader("connection", HEADER_WEBSOCKET_NAME);
        }
        return null;
	}
	

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

}
