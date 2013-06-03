package org.onetwo.common.spring.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.fish.exception.JFishInvalidTokenException;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.onetwo.common.utils.FileUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

public class JFishFirstInterceptor extends WebInterceptorAdapter  {
	
	private static final UrlPathHelper urlPathHelper = new UrlPathHelper();

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(!isMethodHandler(handler))
			return true;
		
		String requestUri = urlPathHelper.getLookupPathForRequest(request);
		String reqUri = WebUtils.extractFullFilenameFromUrlPath(requestUri);
		String extension = FileUtils.getExtendName(reqUri);
		JFishWebUtils.requestUri(requestUri);
		JFishWebUtils.requestExtension(extension);
		JFishWebUtils.currentHandler(handler);
		JFishWebUtils.webHelper(request);
		
		HandlerMethod hm = getHandlerMethod(handler);
		if(!JFishWebUtils.validateToken(request)){
			throw new JFishInvalidTokenException();
		}
		if(BeforeMethodHandler.class.isInstance(hm.getBean())){
			((BeforeMethodHandler)hm.getBean()).beforeHandler(hm.getMethod());
		}
		return true;
	}

	@Override
	public int getOrder() {
		return InterceptorOrder.FIRST;
	}
}
