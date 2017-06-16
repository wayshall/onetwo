package org.onetwo.boot.core.web.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.web.utils.BootWebHelper;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.date.NiceDate;
import org.onetwo.common.profiling.TimeProfileStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

public class BootFirstInterceptor extends WebInterceptorAdapter {
	

	/*final protected static LoadingCache<HandlerMethod, ApiClientMethod> API_METHOD_CACHES = CacheBuilder.newBuilder()
																.build(new CacheLoader<HandlerMethod, ApiClientMethod>() {
																	@Override
																	public ApiClientMethod load(HandlerMethod method) throws Exception {
																		ApiClientMethod apiMethod = new ApiClientMethod(method);
																		apiMethod.initialize();
																		return apiMethod;
																	}
																});*/
	
//	private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
	private static final String CONTROLLER_TIME_KEY = "mvc execute";
	public static final String NOW_KEY = "now";
	
//	private final List<JFishRequestValidator> requestValidators;
//	private RequestPreventor submitPreventor = PreventorFactory.getRepeateSubmitPreventor();
	
	@Autowired(required=false)
	private BootJFishConfig bootJFishConfig;
	
	private boolean isProfile(){
		return bootJFishConfig!=null && bootJFishConfig.isProfile();
	}
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = getHandlerMethod(handler);
		if(handlerMethod==null)
			return true;

		if(isProfile())
			TimeProfileStack.push(CONTROLLER_TIME_KEY);
		
		BootWebHelper helper = BootWebUtils.webHelper(request);
		helper.setControllerHandler(handlerMethod);
		
		/*if(BaseSiteConfig.getInstance().isPreventRepeateSubmit()){
			this.submitPreventor.validateToken(hm.getMethod(), request, response);
		}*/
		
//		this.validateRequest(request, response, hm);
		
		/*if(BeforeMethodHandler.class.isInstance(hm.getBean())){
			((BeforeMethodHandler)hm.getBean()).beforeHandler(request, hm.getMethod());
		}*/
		
		return true;
	}
	
	
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if(!isMethodHandler(handler))
			return ;
		
		if(modelAndView!=null){
			if(!modelAndView.getModel().containsKey(NOW_KEY))
				modelAndView.addObject(NOW_KEY, new NiceDate());
		}else{
			if(request.getAttribute(NOW_KEY)==null)
				request.setAttribute(NOW_KEY, new NiceDate());
		}
	}
	

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		if(!isMethodHandler(handler))
			return ;
		if(isProfile())
			TimeProfileStack.pop(CONTROLLER_TIME_KEY);
	}

	@Override
	public int getOrder() {
		return FIRST;
	}
}
