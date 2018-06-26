package org.onetwo.cloud.zuul;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.web.utils.RequestUtils;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;

/**
 * @author wayshall
 * <br/>
 */
final public class ZuulUtils {

    public static Optional<Route> getRoute(RouteLocator routeLocator, HttpServletRequest request) {
        String requestURI = RequestUtils.getUrlPathHelper().getPathWithinApplication(request);
        return Optional.ofNullable(routeLocator.getMatchingRoute(requestURI));
    }
    
	private ZuulUtils(){
	}

}
