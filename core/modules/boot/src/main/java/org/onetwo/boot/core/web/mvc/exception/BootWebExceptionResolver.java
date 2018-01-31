package org.onetwo.boot.core.web.mvc.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.service.impl.ExceptionMessageAccessor;
import org.onetwo.boot.core.web.utils.BootWebHelper;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.data.DataResult;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/************
 * 异常处理
 * instead of BasicErrorController
 * @author wayshall
 *
 */
public class BootWebExceptionResolver extends SimpleMappingExceptionResolver implements InitializingBean, ExceptionMessageFinder {
//	public static final String MAX_UPLOAD_SIZE_ERROR = "MVC_MAX_UPLOAD_SIZE_ERROR";

	public static final String ERROR_MESSAGE_OBJECT_KEY = "__ERROR_MESSAGE_OBJECT__";
	
	private static final String EXCEPTION_STATCK_KEY = "__exceptionStack__";
	private static final String ERROR_CODE_KEY = "__errorCode__";
	private static final String PRE_URL = "preurl";
	private static final String AJAX_RESULT_PLACEHOLDER = "result";
	
	public static final int RESOLVER_ORDER = -9999;

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
//	private Map<String, WhenExceptionMap> whenExceptionCaches = new WeakHashMap<String, WhenExceptionMap>();
	protected final Logger mailLogger = JFishLoggerFactory.findMailLogger();
	
//	resouce: exception-messages
	/*@Qualifier(BootContextConfig.BEAN_EXCEPTION_MESSAGE)
	@Autowired
	private MessageSource exceptionMessage;*/
	
	private List<String> notifyThrowables;// = BaseSiteConfig.getInstance().getErrorNotifyThrowabbles();
	
	@Autowired
	private BootSiteConfig bootSiteConfig;

	@Autowired(required=false)
	private ExceptionMessageAccessor exceptionMessageAccessor;
	
	private ResponseEntityExceptionHandler responseEntityExceptionHandler = new ResponseEntityExceptionHandler(){};
	/***
	 * WebRequest inject by WebApplicationContextUtils#registerWebApplicationScopes WebRequestObjectFactory
	 */
	@Autowired(required=false)
	private WebRequest webRequest;
	
//	protected String defaultRedirect;
	
	public BootWebExceptionResolver(){
		setOrder(RESOLVER_ORDER);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		initResolver();
		this.notifyThrowables = bootSiteConfig.getNotifyThrowables();
	}
	
	protected void initResolver(){
//		defaultRedirect = BaseSiteConfig.getInstance().getLoginUrl();
	}
	

	protected void beforeReturnModelAndView(HttpServletRequest request, Object handlerMethod, ModelAndView mv, ErrorMessage errorMessage){
		RequestContextHolder.getRequestAttributes().setAttribute(ERROR_MESSAGE_OBJECT_KEY, errorMessage, RequestAttributes.SCOPE_REQUEST);
		if(mv!=null){
			this.doLog(request, errorMessage);
		}
	}
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handlerMethod, Exception ex) {
		ModelMap model = new ModelMap();
		ErrorMessage errorMessage = this.getErrorMessage(ex, bootSiteConfig.isLogErrorDetail());
		String viewName = determineViewName(ex, request);
		errorMessage.setViewName(viewName);
		
		
//		Object req = RequestContextHolder.getRequestAttributes().getAttribute(WebHelper.WEB_HELPER_KEY, RequestAttributes.SCOPE_REQUEST);
		
		if(BootWebUtils.isAjaxRequest(request) || BootWebUtils.isAjaxHandlerMethod(handlerMethod)){
			DataResult<?> result = DataResults.error(errorMessage.getMesage()).build();
			model.put(AJAX_RESULT_PLACEHOLDER, result);
			ModelAndView mv = new ModelAndView("error", model);
			beforeReturnModelAndView(request, handlerMethod, mv, errorMessage);
			return mv;
//			BootWebUtils.webHelper(request).setAjaxErrorResult(result);// for  BootWebExceptionHandler
			//return null for post exceptionHandler to process
//			return null;
		}
		
		String msg = errorMessage.getMesage();
		if(!model.containsKey(AbstractBaseController.ERROR)){
			model.put(AbstractBaseController.ERROR, msg);
			model.put(AbstractBaseController.MESSAGE_TYPE, AbstractBaseController.MESSAGE_TYPE_ERROR);
		}
		if(BootWebUtils.isRedirect(errorMessage.getViewName())){
			ModelAndView mv = this.createModelAndView(errorMessage.getViewName(), model, request, response, ex);
			beforeReturnModelAndView(request, handlerMethod, mv, errorMessage);
			return mv;
		}

		String eInfo = "";
		if(!bootSiteConfig.isProduct() && errorMessage.isDetail()){
			eInfo = LangUtils.toString(ex, true);
//			WebContextUtils.attr(request, EXCEPTION_STATCK_KEY, eInfo);
//			WebContextUtils.attr(request, EXCEPTION_STATCK_KEY2, eInfo);
		}else{
//			WebContextUtils.attr(request, EXCEPTION_STATCK_KEY, "");
//			WebContextUtils.attr(request, EXCEPTION_STATCK_KEY2, "");
		}
//		eInfo = errorMessage.toString()+"  "+ eInfo;
		model.put(EXCEPTION_STATCK_KEY, eInfo);
		model.put(ERROR_CODE_KEY, errorMessage.getCode());
		
		ModelAndView mv = createModelAndView(errorMessage.getViewName(), model, request, response, ex);
		beforeReturnModelAndView(request, handlerMethod, mv, errorMessage);
		return mv;
	}

	/*protected String getUnknowError(){
		return SystemErrorCode.DEFAULT_SYSTEM_ERROR_CODE;
	}*/
	
	
	protected String getPreurl(HttpServletRequest request){
		String preurl = StringUtils.isBlank(request.getParameter(PRE_URL))?BootWebUtils.requestUri():request.getParameter(PRE_URL);
//		return encode(preurl);
		return preurl;
	}
	
	protected ModelAndView createModelAndView(String viewName, ModelMap model, HttpServletRequest request, HttpServletResponse response, Exception ex){
//		return new ModelAndView(viewName, model);
		if (viewName != null) {
			// Apply HTTP status code for error views, if specified.
			// Only apply it if we're processing a top-level request.
			Integer statusCode = determineStatusCode(ex, request, viewName);
			if (statusCode != null) {
				applyStatusCodeIfPossible(request, response, statusCode);
			}
			return getModelAndView(viewName, ex, request);
		}
		return null;
	}
	
	
	protected Integer determineStatusCode(Exception ex, HttpServletRequest request, String viewName) {
		Integer statusCode = super.determineStatusCode(request, viewName);
		if(statusCode==null){
			ResponseEntity<Object> reponse = responseEntityExceptionHandler.handleException(ex, webRequest);
			statusCode = reponse.getStatusCodeValue();
		}
		return statusCode;
	}

	protected void doLog(HttpServletRequest request, ErrorMessage errorMessage){
		BootWebHelper helper = BootWebUtils.webHelper(request);
		String msg = RequestUtils.getServletPath(request);
		Exception ex = errorMessage.getException();
		errorMessage.logErrorContext(logger);
		if(errorMessage.isDetail()){
			msg += " ["+helper.getControllerHandler()+"] error: " + ex.getMessage();
			logger.error(msg, ex);
			JFishLoggerFactory.mailLog(notifyThrowables, ex, msg);
		}else{
			logger.error(msg + " code[{}], message[{}]", LangUtils.getBaseExceptonCode(ex), ex.getMessage());
		}
	}

	@Override
	public ExceptionMessageAccessor getExceptionMessageAccessor() {
		return this.exceptionMessageAccessor;
	}
	/*@Deprecated
	private String findInSiteConfig(Exception ex){
		Class<?> eclass = ex.getClass();
		String viewName = null;
		while(eclass!=null && Throwable.class.isAssignableFrom(eclass)){
			viewName = bootSiteConfig.getConfig(eclass.getName(), "");
			if(StringUtils.isNotBlank(viewName))
				return viewName;
			eclass = eclass.getSuperclass();
		} 
		return viewName;
	}*/


}
