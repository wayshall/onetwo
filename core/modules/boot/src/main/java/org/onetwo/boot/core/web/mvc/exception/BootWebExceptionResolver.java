package org.onetwo.boot.core.web.mvc.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.service.impl.ExceptionMessageAccessor;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.data.DataResult;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.mvc.utils.DataResults;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseType;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

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
	
//	private List<String> notifyThrowables;// = BaseSiteConfig.getInstance().getErrorNotifyThrowabbles();
	
//	@Autowired
//	private BootSiteConfig bootSiteConfig;
//	@Autowired
	private BootJFishConfig jfishConfig;

	@Autowired(required=false)
	private ExceptionMessageAccessor exceptionMessageAccessor;
	
	private ResponseEntityExceptionHandler responseEntityExceptionHandler = new ResponseEntityExceptionHandler(){};
	/***
	 * WebRequest inject by WebApplicationContextUtils#registerWebApplicationScopes WebRequestObjectFactory
	 */
	@Autowired(required=false)
	private WebRequest webRequest;
	private View errorView;
	@Autowired(required = false)
	private ErrorLogHandler errorLogHandler;
	
//	protected String defaultRedirect;
	
	public BootWebExceptionResolver(){
		setOrder(RESOLVER_ORDER);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		initResolver();
//		this.notifyThrowables = bootSiteConfig.getNotifyThrowables();
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
		ErrorMessage errorMessage = this.getErrorMessage(ex);
		String viewName = determineViewName(ex, request);
		errorMessage.setViewName(viewName);
		
		
//		Object req = RequestContextHolder.getRequestAttributes().getAttribute(WebHelper.WEB_HELPER_KEY, RequestAttributes.SCOPE_REQUEST);
		
		if(BootWebUtils.isAjaxRequest(request) || BootWebUtils.isAjaxHandlerMethod(handlerMethod)){
			DataResult<?> result = DataResults.error(errorMessage.getMesage())
											.code(errorMessage.getCode())
											.data(errorMessage.getErrorData())
											.build();
			model.put(AJAX_RESULT_PLACEHOLDER, result);
			ModelAndView mv = new ModelAndView(errorView, model);
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
		if(!errorMessage.isDetail()){
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
		
		// Apply HTTP status code for error views, if specified.
		// Only apply it if we're processing a top-level request.
		Integer statusCode = determineStatusCode(ex, request, viewName);
		if (statusCode != null) {
			applyStatusCodeIfPossible(request, response, statusCode);
		}
		
		ModelAndView mv = null;
		if (viewName != null) {
			mv = getModelAndView(viewName, ex, request);
			mv.addObject("statusCode", statusCode);
		} else if (StringUtils.isNotBlank(jfishConfig.getErrorView()) && isResponsePage(request)){
			mv = getModelAndView(jfishConfig.getErrorView(), ex, request);
			mv.addObject("statusCode", statusCode);
		} else {
			//will forward next HandlerExceptionResolver, see DispatcherServlet#processHandlerException
			mv = null;
		}
		return mv;
	}

	protected boolean isResponsePage(HttpServletRequest request){
		ResponseType responseType = RequestUtils.getResponseType(request);
		return responseType==ResponseType.PAGE;
	}
	
	
	protected Integer determineStatusCode(Exception ex, HttpServletRequest request, String viewName) {
		Integer statusCode = super.determineStatusCode(request, viewName);
		if(statusCode==null){
			ResponseEntity<Object> response;
			try {
				response = responseEntityExceptionHandler.handleException(ex, webRequest);
			} catch (Exception e) {
				// upgrade-sb2: 
				response = handleExceptionInternal(e);
			}
			statusCode = response.getStatusCodeValue();
		}
		return statusCode;
	}
	
	/***
	 * upgrade-sb2: 
	 * @author weishao zeng
	 * @param ex
	 * @return
	 */
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex) {
		HttpHeaders headers = new HttpHeaders();
		if (logger.isWarnEnabled()) {
			logger.warn("Unknown exception type: " + ex.getClass().getName());
		}
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		return handleExceptionInternal(ex, null, headers, status, webRequest);
	}
	
	/***
	 * upgrade-sb2: 
	 * copy from spring 4.x
	 * spring 5后去掉了此方法，copy过来兼容
	 *  
	 * @param ex
	 * @param body
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
		}
		return new ResponseEntity<Object>(body, headers, status);
	}

	protected void doLog(HttpServletRequest request, ErrorMessage errorMessage){
//		errorMessage.setNotifyThrowables(jfishConfig.getNotifyThrowables());
		errorMessage.setNotify(jfishConfig.isNotifyThrowable(errorMessage.getException()));
		logError(request, errorMessage);
		if (errorLogHandler!=null) {
			errorLogHandler.handle(request, errorMessage);
		}
	}

	@Override
	public ExceptionMessageAccessor getExceptionMessageAccessor() {
		return this.exceptionMessageAccessor;
	}

	public Logger getErrorLogger() {
		return JFishLoggerFactory.findErrorLogger(logger);
	}

	public ExceptionMessageFinderConfig getExceptionMessageFinderConfig() {
		return this.jfishConfig;
	}

	public void setJfishConfig(BootJFishConfig jfishConfig) {
		this.jfishConfig = jfishConfig;
	}

	public void setErrorView(View errorView) {
		this.errorView = errorView;
	}

}
